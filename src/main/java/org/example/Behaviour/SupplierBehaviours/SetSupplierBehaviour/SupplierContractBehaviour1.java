package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupplierContractBehaviour1 extends Behaviour {
    private boolean flagContract = false;
    private MessageTemplate messageTemplate;

    @Override
    public void onStart() {
        messageTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(messageTemplate);
        if (message != null) {
            if (message.getProtocol().equals("Auction grand")){
                log.info("Информация по полному контракту! {}", this.myAgent.getLocalName());
                createMsg(message);
            } else if (message.getProtocol().equals("Auction min1")) {
                log.info("Информация по первой части контракта! {}", this.myAgent.getLocalName());
                createMsg(message);
            } else {
                log.info("Информация по второй части контракта! {}", this.myAgent.getLocalName());
                createMsg(message);
            }
//            if (message.getPerformative() == ACLMessage.CONFIRM) {
//                log.info("Получил сообщение от {} с согласием на заключение контракта", message.getSender().getLocalName());
//                sendCons(Double.parseDouble(message.getContent()), "Yes", consCopy(this.myAgent.getLocalName()));
//            } else {
//                log.info("Получил сообщение об отказе сделки от {} ", message.getSender().getLocalName());
//                sendCons(Double.parseDouble(message.getContent()), "No", consCopy(this.myAgent.getLocalName()));
//            }
//            flagContract = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

//    @Override
//    public int onEnd() {
//        if (flagContract) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }
    public void createMsg(ACLMessage message){
        if (message.getPerformative() == ACLMessage.CONFIRM) {
            log.info("Получил сообщение от {} с согласием на заключение контракта", message.getSender().getLocalName());
            sendCons(Double.parseDouble(message.getContent()), "Yes", consCopy(this.myAgent.getLocalName()));
        } else {
            log.info("Получил сообщение об отказе сделки от {} ", message.getSender().getLocalName());
            sendCons(Double.parseDouble(message.getContent()), "No", consCopy(this.myAgent.getLocalName()));
        }
    }
    public String consCopy(String agent){
        char[] crh = agent.toCharArray();
        String nameGenerator = "AgentConsumer" + crh[crh.length-1];
        return nameGenerator;
    }

    private void sendCons(double price, String msg, String agent) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.DISCONFIRM);
        firstMsg.setContent(price + "," + msg);
        firstMsg.addReceiver(new AID(agent, false));
        myAgent.send(firstMsg);
    }
}
