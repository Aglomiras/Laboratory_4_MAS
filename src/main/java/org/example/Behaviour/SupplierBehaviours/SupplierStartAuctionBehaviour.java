package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Support.DfHelper;
import org.example.Support.TopicHelper;
@Slf4j
public class SupplierStartAuctionBehaviour extends Behaviour {
    private MessageTemplate messageTemplate;
    private boolean flagStartAuc = false;

    @Override
    public void onStart() {
        messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROXY);
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(messageTemplate);
        if (message != null) {
            String power = message.getContent().split(",")[0];
            startAuction(Double.parseDouble(power));
            flagStartAuc = true;
            log.info("{} отправил производителям предложение о начале аукциона", this.myAgent.getLocalName());
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return flagStartAuc;
    }

    public void startAuction(double power){
        var ags = DfHelper.search(myAgent, "generator");
        String topicName = "topic_" + this.myAgent.getLocalName();
        TopicHelper.register(myAgent, topicName);
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.setContent(topicName + "," + power);
        msg.setProtocol("Auction");
        ags.forEach(e -> msg.addReceiver(e));
        myAgent.send(msg);
    }
}
