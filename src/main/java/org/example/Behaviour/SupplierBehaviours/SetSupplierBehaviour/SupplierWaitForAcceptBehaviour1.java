package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.SupplierSave;

@Slf4j
public class SupplierWaitForAcceptBehaviour1 extends ParallelBehaviour {
    private Behaviour wakeupBeh;
    private SupplierReceiveChoiceBehaviour1 receiveBeh;
    private double power;
    private double price;
    private long times;
    private SupplierSave supplierSave;
    private String topic;

    public SupplierWaitForAcceptBehaviour1(double power, double price, long times, SupplierSave supplierSave, String topic) {
        super(WHEN_ANY);
        this.power = power;
        this.times = times;
        this.price = price;
        this.supplierSave = supplierSave;
        this.supplierSave.setPower(power);
        this.supplierSave.setPrice(price);
        this.topic = topic;
    }

    @Override
    public void onStart() {
        receiveBeh = new SupplierReceiveChoiceBehaviour1(supplierSave, topic);
        wakeupBeh = new WakerBehaviour(myAgent, times) {
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
            if (supplierSave.getPrice() > supplierSave.getMinPrice()) {
                log.info("Отправляю победителю торгов {} предложение о заключении контракта. Отправил - {} ", supplierSave.getAgentNamNam(), this.myAgent.getLocalName());
                sendBet();
                return 0;
            } else {
                log.info("Нет выгодной цены... Отчитался {}", this.myAgent.getLocalName());
                return 1;
            }
        } else {
            log.info("Победителя торгов нет...");
            return 1;
        }
    }

    private void sendBet() {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.CFP);
        firstMsg.setContent(supplierSave.getMinPrice() + "," + supplierSave.getPower());
        firstMsg.setProtocol("Auction " + topic);
        firstMsg.addReceiver(supplierSave.getAgent());
        myAgent.send(firstMsg);
    }
}
