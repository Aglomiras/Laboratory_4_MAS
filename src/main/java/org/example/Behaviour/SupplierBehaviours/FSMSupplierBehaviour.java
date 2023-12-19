package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.FSMBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FSMSupplierBehaviour extends FSMBehaviour {
    /**
     * FSM-поведение для продавца
     */
    private double power;

    public FSMSupplierBehaviour(double power) {
        this.power = power;
    }

    private final String
            START_AUCTION = "start_auction",
            WAIT_START_AUCTION_1 = "wait_start_auction_1",
            WAIT_START_AUCTION_2 = "wait_start_auction_2",
            DIVISION = "division",
//            CHOICE = "choice",
            WAIT_END_AUCTION = "wait_end_auction",
            RECEIVE = "receive",
            FAIL = "fail",
            SUCCESS = "success";


    @Override
    public void onStart() {
//        log.info("Начал {}", this.myAgent.getLocalName());
        this.registerFirstState(new SupplierStartAuctionBehaviour(), START_AUCTION);
        SupplierWaitForAuctionBehaviour suppWait1 = new SupplierWaitForAuctionBehaviour(3);
        this.registerState(suppWait1, WAIT_START_AUCTION_1);
        this.registerState(new SupplierDivisionOfContractBehaviour(power), DIVISION);

        SupplierWaitForAuctionBehaviour suppWait2 = new SupplierWaitForAuctionBehaviour(6);
        this.registerState(suppWait2, WAIT_START_AUCTION_2);

        this.registerState(new SupplierWaitForAcceptBehaviour(), WAIT_END_AUCTION);

        this.registerState(new SupplierContractBehaviour(), RECEIVE);
        this.registerLastState(new SupplierFail(), FAIL);
        this.registerLastState(new SupplierSuccess(), SUCCESS);



        this.registerDefaultTransition(START_AUCTION, WAIT_START_AUCTION_1);
        this.registerTransition(WAIT_START_AUCTION_1, DIVISION, 0);
        this.registerTransition(WAIT_START_AUCTION_1, WAIT_END_AUCTION, 1);
        this.registerTransition(DIVISION, WAIT_START_AUCTION_2, 0);
        this.registerTransition(DIVISION, FAIL, 1);

        this.registerTransition(WAIT_START_AUCTION_2, FAIL, 0);
        this.registerTransition(WAIT_START_AUCTION_2, WAIT_END_AUCTION, 1);

        this.registerTransition(WAIT_END_AUCTION, RECEIVE, 0);
        this.registerTransition(WAIT_END_AUCTION, FAIL, 1);

        this.registerTransition(RECEIVE, SUCCESS, 0);
        this.registerTransition(RECEIVE, FAIL, 1);
    }
}
