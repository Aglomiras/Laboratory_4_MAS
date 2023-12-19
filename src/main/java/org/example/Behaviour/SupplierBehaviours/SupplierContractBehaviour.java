package org.example.Behaviour.SupplierBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupplierContractBehaviour extends Behaviour {
    private boolean flagContract = false;

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        if (message != null) {
            log.info("Получил сообщение от {} с согласием на заключение контракта", message.getSender().getLocalName());
            sendCons(Double.parseDouble(message.getContent()), consCopy(this.myAgent.getLocalName()));
            flagContract = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return flagContract;
    }

    @Override
    public int onEnd() {
        if (flagContract) {
            return 0;
        } else {
            return 1;
        }
    }
    public String consCopy(String agent){
        char[] crh = agent.toCharArray();
        String nameGenerator = "AgentConsumer" + crh[crh.length-1];
        return nameGenerator;
    }

    private void sendCons(double price, String agent) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.DISCONFIRM);
        firstMsg.setContent(price + "");
        firstMsg.addReceiver(new AID(agent, false));
        myAgent.send(firstMsg);
    }
}
