package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.behaviours.ParallelBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviourSecond.FSMSupplierSecondBehaviour;
import org.example.Behaviour.SupplierBehaviours.SupplierParallelSecondBehaviour;
import org.example.Model.SupplierSave;

@Slf4j
public class SupplierDivisionOfContractBehaviour1 extends ParallelBehaviour {
    private double power;
    private double price;
    private String nameConsumer;
    private long times;
    private SupplierParallelSecondBehaviour fsm1;
    private SupplierParallelSecondBehaviour fsm2;
    private SupplierSave supplierSave1 = new SupplierSave();
    private SupplierSave supplierSave2 = new SupplierSave();
    public SupplierDivisionOfContractBehaviour1(double power, double price, String nameConsumer, long times) {
        super(WHEN_ALL);
        this.power = power;
        this.price = price;
        this.nameConsumer = nameConsumer;
        this.times = times;
    }

    @Override
    public void onStart() {
        log.info("Деление контракта {}", this.myAgent.getLocalName());
        fsm1 = new SupplierParallelSecondBehaviour(power*0.7, price*0.7, nameConsumer, "min1",times * 7/10, supplierSave1);
        fsm2 = new SupplierParallelSecondBehaviour(power*0.3, price*0.3, nameConsumer, "min2", times* 7/10, supplierSave2);
        this.addSubBehaviour(fsm1);
        this.addSubBehaviour(fsm2);
    }

}
