package org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviourFirst;

import jade.core.behaviours.FSMBehaviour;
import org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour.*;
import org.example.Model.SupplierSave;

public class FSMSupplierFirstBehaviour extends FSMBehaviour {
    private double power;
    private double price;
    private String nameConsumer;
    private String modifier;
    private long times;
    private SupplierSave supplierSave = new SupplierSave();

    public FSMSupplierFirstBehaviour(double power, double price, String nameConsumer, String modifier, long times) {
        this.power = power;
        this.price = price;
        this.nameConsumer = nameConsumer;
        this.modifier = modifier;
        this.times = times;
    }

    private final String
            FIRST_START = "first_start", //
            WAIT_FIRST_AUCTION = "wait_first_auction", //
            WAIT_SECOND_AUCTION = "wait_second_auction", //
            CONTRACT = "contract", //
            DIVISION = "division", //
            FAIL = "fail", //
            SUCCESS = "success"; //

    @Override
    public void onStart() {
        this.registerFirstState(new SupplierStartAuctionBehaviour1(power, price, modifier), FIRST_START);
        this.registerState(new SupplierWaitForAuctionBehaviour1(3, times / 8), WAIT_FIRST_AUCTION);
        this.registerState(new SupplierWaitForAcceptBehaviour1(power, price, times / 6, supplierSave, modifier), WAIT_SECOND_AUCTION);

        this.registerState(new SupplierContractBehaviour1(), CONTRACT);
        this.registerLastState(new SupplierDivisionOfContractBehaviour1(power, price, nameConsumer, times), DIVISION);
        this.registerLastState(new SupplierFail(), FAIL);
        this.registerLastState(new SupplierSuccess(), SUCCESS);

        this.registerDefaultTransition(FIRST_START, WAIT_FIRST_AUCTION);
        this.registerTransition(WAIT_FIRST_AUCTION, DIVISION, 0);
        this.registerTransition(WAIT_FIRST_AUCTION, WAIT_SECOND_AUCTION, 1);

        this.registerTransition(WAIT_SECOND_AUCTION, CONTRACT, 0);
        this.registerTransition(WAIT_SECOND_AUCTION, FAIL, 1);
    }
}
