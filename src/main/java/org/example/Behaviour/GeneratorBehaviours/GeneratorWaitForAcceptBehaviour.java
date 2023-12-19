package org.example.Behaviour.GeneratorBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneratorWaitForAcceptBehaviour extends ParallelBehaviour {
    private final double minPrice;
    private Behaviour wakeupBeh; //Под-поведение WakerBehaviour
    private GeneratorAuctionBehaviour receiveBeh; //Под-поведение Behaviour
    private final String topicName;
    private AID buyer;

    public GeneratorWaitForAcceptBehaviour(String topicName, double minPrice, AID buyer) {
        super(WHEN_ANY);
        this.minPrice = minPrice;
        this.topicName = topicName;
        this.buyer = buyer;
    }

    @Override
    public void onStart() {
        receiveBeh = new GeneratorAuctionBehaviour(topicName, minPrice);
        wakeupBeh = new WakerBehaviour(myAgent, 5000) {
            boolean wake = false;

            @Override
            protected void onWake() {
                wake = true;
                log.info("TIME IS UP " + this.myAgent.getLocalName());
            }

            @Override
            public int onEnd() {
                return wake ? 0 : 1;
            }
        };

        this.addSubBehaviour(receiveBeh);
        this.addSubBehaviour(wakeupBeh);
    }

    @Override
    public int onEnd() {
        if (wakeupBeh.done()) {
            if (receiveBeh.onEnd() == 0) {
                log.info("Отсылаю свою цену дистрибьютору " + myAgent.getLocalName());
                sendBet(receiveBeh.getCurrentPrice());
                return 0;
            } else {
                log.info("Ничего не отсылаю");
                return 1;
            }
        } else {
            return 1;
        }
    }

    private void sendBet(double price) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.AGREE);
        firstMsg.setContent(price + "");
        firstMsg.addReceiver(buyer);
        myAgent.send(firstMsg);
    }
}
