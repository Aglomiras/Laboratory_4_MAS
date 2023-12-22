package org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviourSecond;

import jade.core.behaviours.FSMBehaviour;
import org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour.*;
import org.example.Model.SupplierSave;

public class FSMSupplierSecondBehaviour extends FSMBehaviour {
    private double power;
    private double price;
    private String nameConsumer;
    private long times;
    private String topic;
    private SupplierSave supplierSave;

    public FSMSupplierSecondBehaviour(double power, double price, String nameConsumer, String topic, long times, SupplierSave supplierSave) {
        this.power = power;
        this.price = price;
        this.nameConsumer = nameConsumer;
        this.times = times;
        this.topic = topic;
        this.supplierSave = supplierSave;
    }

    private final String
            FIRST_START = "first_start", //
            WAIT_FIRST_AUCTION = "wait_first_auction", //
            WAIT_SECOND_AUCTION = "wait_second_auction", //
            CONTRACT = "contract", //
            FAIL = "fail", //
            SUCCESS = "success"; //

    @Override
    public void onStart() {
        this.registerFirstState(new SupplierStartAuctionBehaviour1(power, price, topic), FIRST_START);
        this.registerState(new SupplierWaitForAuctionBehaviour1(6, times/5), WAIT_FIRST_AUCTION);
        this.registerState(new SupplierWaitForAcceptBehaviour1(power, price, times/4, this.supplierSave, topic), WAIT_SECOND_AUCTION);
        this.registerState(new SupplierContractBehaviour1(), CONTRACT);

        this.registerLastState(new SupplierFail(), FAIL);
        this.registerLastState(new SupplierSuccess(), SUCCESS);

        this.registerDefaultTransition(FIRST_START, WAIT_FIRST_AUCTION);
        this.registerTransition(WAIT_FIRST_AUCTION, FAIL,0); //Вместо FAIL что-то другое бы
        this.registerTransition(WAIT_FIRST_AUCTION, WAIT_SECOND_AUCTION,1);

        this.registerTransition(WAIT_SECOND_AUCTION, CONTRACT,0);
        this.registerTransition(WAIT_SECOND_AUCTION, FAIL,1);
    }
}
