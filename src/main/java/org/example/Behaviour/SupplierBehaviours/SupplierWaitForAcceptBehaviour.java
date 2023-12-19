package org.example.Behaviour.SupplierBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupplierWaitForAcceptBehaviour extends ParallelBehaviour {
    private Behaviour wakeupBeh; //Под-поведение WakerBehaviour
    private SupplierReceiveChoiceBehaviour receiveBeh; //Под-поведение Behaviour

    public SupplierWaitForAcceptBehaviour() {
        super(WHEN_ANY);
    }

    @Override
    public void onStart() {
        receiveBeh = new SupplierReceiveChoiceBehaviour();
        wakeupBeh = new WakerBehaviour(myAgent, 10000) {
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
            log.info("Отправляю победителю торгов {} предложение о заключении контракта. Отправил - {} ", receiveBeh.getAgentMinMin(), this.myAgent.getLocalName());
            sendBet(receiveBeh.getMinPriceCons(), receiveBeh.getAgentMin());
            return 0;
        } else {
            log.info("Победителя торгов нет...");
            return 1;
        }
    }

    private void sendBet(double price, AID agent) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.CFP);
        firstMsg.setContent(price + "");
        firstMsg.addReceiver(agent);
        myAgent.send(firstMsg);
    }
}
