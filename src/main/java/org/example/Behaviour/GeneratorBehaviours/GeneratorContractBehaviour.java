package org.example.Behaviour.GeneratorBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneratorContractBehaviour extends Behaviour {
    private double power = 10;

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CFP));
        if (message != null) {
            log.info("Получил сообщение от {} с предложением о заключении контракта на мощность", message.getSender().getLocalName());
            if (power > 0) {
                sendBet(Double.parseDouble(message.getContent()), message.getSender());
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    private void sendBet(double price, AID agent) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.CONFIRM);
        firstMsg.setContent(price + "");
        firstMsg.addReceiver(agent);
        myAgent.send(firstMsg);
    }
}
