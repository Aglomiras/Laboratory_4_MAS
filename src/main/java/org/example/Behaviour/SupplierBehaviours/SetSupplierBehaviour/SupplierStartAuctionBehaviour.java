package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.example.Support.DfHelper;
import org.example.Support.TopicHelper;

/**
 * SupplierStartAuctionBehaviour(OneShotBehaviour) -> отправляет производителям энергии сообщение об участии в аукционе
 */
public class SupplierStartAuctionBehaviour extends OneShotBehaviour {
    private double power;
    private double price;
    private String modifier;

    public SupplierStartAuctionBehaviour(double power, double price, String modifier) {
        this.power = power;
        this.price = price;
        this.modifier = modifier;
    }

    /**Формирование сообщения*/
    @Override
    public void action() {
        var ags = DfHelper.search(myAgent, "generator");
        String topicName = "topic_" + this.myAgent.getLocalName() + this.modifier;
        TopicHelper.register(myAgent, topicName);

        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.setContent(topicName + "," + this.power + "," + this.price);
        msg.setProtocol("Auction " + modifier);
        ags.forEach(e -> msg.addReceiver(e));
        myAgent.send(msg);
    }

}
