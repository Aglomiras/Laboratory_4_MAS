package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Support.DfHelper;
import org.example.Support.TopicHelper;

@Slf4j
public class SupplierDivisionOfContractBehaviour extends OneShotBehaviour {
    private double power;
    private boolean flagDivis = false;
//    private double pricePower;

    public SupplierDivisionOfContractBehaviour(double power) {
        this.power = power;
    }

    @Override
    public void action() {
        log.info("Деление контракта {}", this.myAgent.getLocalName());
        double powerDel = power / 2;
        startAuction(powerDel, 1);
        startAuction(powerDel, 2);
        flagDivis = true;
    }

    @Override
    public int onEnd() {
        if (flagDivis) {
            return 0;
        } else {
            return 1;
        }
    }

    public void startAuction(double power, int del) {
        var ags = DfHelper.search(myAgent, "generator");
        String topicName = "topic_" + this.myAgent.getLocalName() + del;
        TopicHelper.register(myAgent, topicName);
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.setContent(topicName + "," + power);
        msg.setProtocol("Auction");
        ags.forEach(e -> msg.addReceiver(e));
        myAgent.send(msg);
    }
}
