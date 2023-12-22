package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

/**
 * SupplierWaitForAuctionBehaviour(ParallelBehaviour):
 * -> SupplierReceiveAuctionBehaviour(Behaviour)
 * -> WakerBehaviour(ждет ответов от производителей о том, что у них нет необходимого количества энергии)
 */
@Slf4j
public class SupplierWaitForAuctionBehaviour extends ParallelBehaviour {
    private Behaviour wakeupBeh;
    private SupplierReceiveAuctionBehaviour receiveBeh;
    private int countMsg;
    private long times;

    public SupplierWaitForAuctionBehaviour(int countMsg, long times) {
        super(WHEN_ANY);
        this.countMsg = countMsg; //Счетчик количества сообщений для принятия решения о делении контракта
        this.times = times;
    }

    @Override
    public void onStart() {
        receiveBeh = new SupplierReceiveAuctionBehaviour(countMsg);
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
            if (receiveBeh.onEnd() == 0) {
                log.info("Никто не может дать столько энергии. Необходимо разделить контракт. Отчитался {}", this.myAgent.getLocalName());
                return 0; //Плохой исход
            } else {
                log.info("Есть производители, которые могут обеспечить требуемую энергию.");
                return 1; //Хороший исход
            }
        } else {
            if (receiveBeh.onEnd() == 0) {
                log.info("Никто не может дать столько энергии. Необходимо разделить контракт. Отчитался {}", this.myAgent.getLocalName());
                return 0; //Плохой исход
            } else {
                log.info("Есть производители, которые могут обеспечить требуемую энергию.");
                return 1; //Хороший исход
            }
        }
    }
}
