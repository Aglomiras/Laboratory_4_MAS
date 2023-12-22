package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviourFirst.FSMSupplierFirstBehaviour;

/**
 * SupplierParallelFirstBehaviour(ParallelBehaviour):
 * -> FSMSupplierFirstBehaviour(FSMBehaviour)
 * -> WakerBehaviour(задаем время работы 0.85)
 */
@Slf4j
public class SupplierParallelFirstBehaviour extends ParallelBehaviour {
    private Behaviour wakeupBeh;
    private FSMSupplierFirstBehaviour fsmBeh;
    private double power;
    private double price;
    private String nameCons;
    private long times;

    public SupplierParallelFirstBehaviour(double power, double price, String nameCons, long times) {
        super(WHEN_ANY);
        this.power = power;
        this.price = price;
        this.nameCons = nameCons;
        this.times = times;
    }

    @Override
    public void onStart() {
        fsmBeh = new FSMSupplierFirstBehaviour(power, price, nameCons, "grand", times);
        wakeupBeh = new WakerBehaviour(myAgent, times) {
            boolean wake = false;

            @Override
            protected void onWake() {
                wake = true;
                log.info("TIME IS UP GRAND FSM" + this.myAgent.getLocalName());
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
