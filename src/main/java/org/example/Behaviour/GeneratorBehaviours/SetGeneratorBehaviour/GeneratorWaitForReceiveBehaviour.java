package org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.GeneratorSave;

/**
 * GeneratorWaitForReceiveBehaviour(ParallelBehaviour) -> запускает торги и останавливает их по окончанию времени
 */
@Slf4j
public class GeneratorWaitForReceiveBehaviour extends ParallelBehaviour {
    private final double minPrice;
    private Behaviour wakeupBeh; //Под-поведение WakerBehaviour
    private GeneratorTopicAuctionBehaviour receiveBeh; //Под-поведение Behaviour
    private final String topicName;
    private AID buyer;
    private double powerCons;
    private double priceCons;
    private String protocol;
    private long times;
    private GeneratorSave generatorSave = new GeneratorSave();

    public GeneratorWaitForReceiveBehaviour(String topicName, double minPrice, AID buyer, double power, double price, String protocol, long times) {
        super(WHEN_ANY);
        this.topicName = topicName;
        this.minPrice = minPrice;
        this.buyer = buyer;
        this.powerCons = power;
        this.priceCons = price;
        this.protocol = protocol;
        this.times = times;
    }

    @Override
    public void onStart() {
        receiveBeh = new GeneratorTopicAuctionBehaviour(topicName, minPrice, powerCons, priceCons, generatorSave);
        wakeupBeh = new WakerBehaviour(myAgent, times) {
            boolean wake = false;

            @Override
            protected void onWake() {
                wake = true;
                log.info("TIME IS UP TOPIC " + topicName + " " + this.myAgent.getLocalName());
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
                sendBet(generatorSave.getCurrentPrice(), generatorSave.getPrice());
                return 0;
            } else {
                log.info("Ничего не отсылаю");
                return 1;
            }
        } else {
            return 1;
        }
    }

    private void sendBet(double price, double priceCons) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.AGREE);
        firstMsg.setContent(price + "," + priceCons);
        firstMsg.setProtocol(protocol);
        firstMsg.addReceiver(buyer);
        myAgent.send(firstMsg);
    }
}
