package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupplierWaitForAuctionBehaviour extends ParallelBehaviour {
    private Behaviour wakeupBeh; //Под-поведение WakerBehaviour
    private SupplierReceiveAuctionBehaviour receiveBeh; //Под-поведение Behaviour
    private int countMsg;
    public SupplierWaitForAuctionBehaviour(int countMsg) {
        super(WHEN_ANY);
        this.countMsg = countMsg;
    }

    @Override
    public void onStart() {
        receiveBeh = new SupplierReceiveAuctionBehaviour(countMsg);
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
                log.info("Никто из генераторов энергии не может обеспечить требуемый запас энергии. Отчитался {}", this.myAgent.getLocalName());
                log.info("Нужно разделить контракт.");
                return 0;
            } else {
                log.info("Есть производители, которые могут обеспечить требуемую энергию");
                return 1;
            }
        } else {
            if (receiveBeh.onEnd() == 0) {
                log.info("Никто из генераторов энергии не может обеспечить требуемый запас энергии. Отчитался {}", this.myAgent.getLocalName());
                log.info("Нужно разделить контракт.");
                return 0;
            } else {
                log.info("Есть производители, которые могут обеспечить требуемую энергию");
                return 1;
            }
        }
    }

//    private void sendBet(double price, AID agent) {
//        ACLMessage firstMsg = new ACLMessage(ACLMessage.CFP);
//        firstMsg.setContent(price + "");
//        firstMsg.addReceiver(agent);
//        myAgent.send(firstMsg);
//    }
}
