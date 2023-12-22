package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviourSecond.FSMSupplierSecondBehaviour;
import org.example.Model.SupplierSave;

/**
 * SupplierParallelSecondBehaviour(ParallelBehaviour) - аналог SupplierParallelFirstBehaviour(ParallelBehaviour)
 */
@Slf4j
public class SupplierParallelSecondBehaviour extends ParallelBehaviour {
    private Behaviour wakeupBeh;
    private FSMSupplierSecondBehaviour fsmBeh;
    private double power;
    private double price;
    private String nameCons;
    private long times;
    private String topic;
    private SupplierSave supplierSave;

    public SupplierParallelSecondBehaviour(double power, double price, String nameCons, String topic, long times, SupplierSave supplierSave) {
        super(WHEN_ANY);
        this.power = power;
        this.price = price;
        this.nameCons = nameCons;
        this.topic = topic;
        this.times = times;
        this.supplierSave = supplierSave;
    }

    @Override
    public void onStart() {
        fsmBeh = new FSMSupplierSecondBehaviour(power, price, nameCons, topic, times, this.supplierSave);
        wakeupBeh = new WakerBehaviour(myAgent, times) {
            boolean wake = false;

            @Override
            protected void onWake() {
                wake = true;
                log.info("TIME IS UP MIN FSM" + this.myAgent.getLocalName());
            }

            @Override
            public int onEnd() {
                return wake ? 0 : 1;
            }
        };

        this.addSubBehaviour(fsmBeh);
        this.addSubBehaviour(wakeupBeh);
    }
}
