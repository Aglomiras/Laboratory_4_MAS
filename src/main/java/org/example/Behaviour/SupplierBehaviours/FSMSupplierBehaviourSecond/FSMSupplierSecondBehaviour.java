package org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviourSecond;

import jade.core.behaviours.FSMBehaviour;
import org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour.*;
import org.example.Model.SupplierSave;

/**
 * FSMSupplierSecondBehaviour(FSMBehaviour) -> мини FSM-поведение
 */
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
            FIRST_START = "first_start", //SupplierStartAuctionBehaviour (Приглашение на участие в мини торгах)
            WAIT_FIRST_AUCTION = "wait_first_auction", //SupplierWaitForAuctionBehaviour (Проверка, а могут ли производители обеспечить мощность)
            WAIT_SECOND_AUCTION = "wait_second_auction", //SupplierWaitForReceiveBehaviour (Запуск торгов)
            CONTRACT = "contract", //SupplierContractBehaviour (Заключение контракта)
            FAIL = "fail", //SupplierFail (Провал)
            SUCCESS = "success"; //SupplierSuccess (Успех)

    @Override
    public void onStart() {
        this.registerFirstState(new SupplierStartAuctionBehaviour(power, price, topic), FIRST_START);
        this.registerState(new SupplierWaitForAuctionBehaviour(6, times / 5), WAIT_FIRST_AUCTION);
        this.registerState(new SupplierWaitForReceiveBehaviour(power, price, times / 4, this.supplierSave, topic), WAIT_SECOND_AUCTION);
        this.registerState(new SupplierContractBehaviour(), CONTRACT);

        this.registerLastState(new SupplierFail(), FAIL);
        this.registerLastState(new SupplierSuccess(), SUCCESS);

        this.registerDefaultTransition(FIRST_START, WAIT_FIRST_AUCTION);
        this.registerTransition(WAIT_FIRST_AUCTION, FAIL, 0);
        this.registerTransition(WAIT_FIRST_AUCTION, WAIT_SECOND_AUCTION, 1);

        this.registerTransition(WAIT_SECOND_AUCTION, CONTRACT, 0);
        this.registerTransition(WAIT_SECOND_AUCTION, FAIL, 1);
    }
}
