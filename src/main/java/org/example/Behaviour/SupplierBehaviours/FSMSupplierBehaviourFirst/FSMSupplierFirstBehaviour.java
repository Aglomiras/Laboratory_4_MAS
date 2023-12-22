package org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviourFirst;

import jade.core.behaviours.FSMBehaviour;
import org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour.*;
import org.example.Model.SupplierSave;

/**FSMSupplierFirstBehaviour(FSMBehaviour) - главное FSM-поведение*/
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
            FIRST_START = "first_start", //SupplierStartAuctionBehaviour (Приглашение на аукцион)
            WAIT_FIRST_AUCTION = "wait_first_auction", //SupplierWaitForAuctionBehaviour (Проверка наличия необходимой энергии)
            WAIT_SECOND_AUCTION = "wait_second_auction", //SupplierWaitForReceiveBehaviour (Запускает аукцион среди производителей энергии)
            CONTRACT = "contract", //
            DIVISION = "division", //
            FAIL = "fail", //SupplierFail (Провал)
            SUCCESS = "success"; //SupplierSuccess (Успех)

    @Override
    public void onStart() {
        this.registerFirstState(new SupplierStartAuctionBehaviour(power, price, modifier), FIRST_START);
        this.registerState(new SupplierWaitForAuctionBehaviour(3, times / 8), WAIT_FIRST_AUCTION);
        this.registerState(new SupplierWaitForReceiveBehaviour(power, price, times / 6, supplierSave, modifier), WAIT_SECOND_AUCTION);

        this.registerState(new SupplierContractBehaviour(), CONTRACT);
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
