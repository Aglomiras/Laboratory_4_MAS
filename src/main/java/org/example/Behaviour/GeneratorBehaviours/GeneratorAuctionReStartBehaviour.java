package org.example.Behaviour.GeneratorBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneratorAuctionReStartBehaviour extends Behaviour {
    private final double myMinPrice;
    private final double powerGen;
    private MessageTemplate messageTemplate;

    public GeneratorAuctionReStartBehaviour(double myMinPrice, double powerGen) {
        this.myMinPrice = myMinPrice;
        this.powerGen = powerGen;
    }

    @Override
    public void onStart() {
         messageTemplate = MessageTemplate.or(MessageTemplate.MatchProtocol("Auction1"), MessageTemplate.MatchProtocol("Auction2"));
    }

    @Override
    public void action() {
        ACLMessage auctionMsg = myAgent.receive(messageTemplate);
        if (auctionMsg != null) {
//            if (auctionMsg.getProtocol().equals("Auction1")) {
//
//            } else {
//
//            }

            String topic = auctionMsg.getContent().split(",")[0];
            double powerCons = Double.parseDouble(auctionMsg.getContent().split(",")[1]);
            if (powerCons <= this.powerGen) {
                this.myAgent.addBehaviour(new GeneratorWaitForAcceptBehaviour(topic, myMinPrice, auctionMsg.getSender()));
            } else {
                rejectSend(auctionMsg.getSender()); //Отправка сообщений в случае нехватки мощности
                log.info("Нету необходимой мощности. {}", this.myAgent.getLocalName());
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public int onEnd() {
        return super.onEnd();
    }

    public void rejectSend(AID aidAgent){
        ACLMessage msgReject = new ACLMessage(ACLMessage.FAILURE);
        msgReject.setContent("Нет столько мощности!");
        msgReject.addReceiver(aidAgent);
        myAgent.send(msgReject);
    }

}
