package org.example.Behaviour.GeneratorBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.Support.TopicHelper;

@Slf4j
@Data
public class GeneratorAuctionBehaviour extends Behaviour {
    private final String topicName;
    private final double minPrice;
    private double currentPrice;
    private AID topic;
    private boolean finishAuction = false;
    private boolean wake = true;

    public GeneratorAuctionBehaviour(String topicName, double minPrice) {
        this.minPrice = minPrice;
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, topicName);
        sendBet(minPrice * 2);
    }


    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchTopic(topic));
        if (msg != null && !msg.getSender().equals(myAgent.getAID())) {
            double otherBet = Double.parseDouble(msg.getContent());
            if (otherBet < currentPrice) {
                if (otherBet < minPrice) {
                    finishAuction = true;
                    wake = false;
                    log.info("Received a bet {} which is less than my minimal price {}. Quit the auction. ", otherBet, minPrice);
                } else {
                    double random = 0.85 + 0.1 * Math.random();
                    double myNewBet = otherBet * random;
                    log.info("Received a bet {} which less than my prev bet  {}. Sending the new one {}", otherBet, currentPrice, myNewBet);
                    sendBet(myNewBet);
                }
            } else {
                log.info("Received bet {} from {} but the msg is ignored cause my prev bet {} is less", otherBet, msg.getSender().getLocalName(), currentPrice);
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return finishAuction;
    }

    private void sendBet(double price) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.INFORM);
        firstMsg.setContent(price + "");
        currentPrice = price;
        firstMsg.addReceiver(topic);
        myAgent.send(firstMsg);
    }

    @Override
    public int onEnd() {
        if (wake) {
            return 0;
        } else {
            return 1;
        }
    }
}