package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.SupplierSave;

/**
 * SupplierWaitForReceiveBehaviour(ParallelBehaviour):
 * -> SupplierReceiveChoiceBehaviour(ParallelBehaviour)
 * -> WakerBehaviour(ждет, когда производители закончат свои торги)
 */
@Slf4j
public class SupplierWaitForReceiveBehaviour extends ParallelBehaviour {
    private Behaviour wakeupBeh;
    private SupplierReceiveChoiceBehaviour receiveBeh;
    private double power;
    private double price;
    private long times;
    private SupplierSave supplierSave;
    private String topic;

    public SupplierWaitForReceiveBehaviour(double power, double price, long times, SupplierSave supplierSave, String topic) {
        super(WHEN_ANY);
        this.power = power; //Закупаемая мощность
        this.times = times; //Количество времени, отведенного для проведения торгов
        this.price = price; //Цена закупаемой мощности
        this.supplierSave = supplierSave; //Dto - поставщика
        this.supplierSave.setPower(power);
        this.supplierSave.setPrice(price);
        this.topic = topic;
    }

    @Override
    public void onStart() {
        receiveBeh = new SupplierReceiveChoiceBehaviour(supplierSave, topic);
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
                createMsgContract(); //Отправка сообщения победителю
                return 0;
            } else {
                log.info("Нет выгодной цены... Отчитался {}", this.myAgent.getLocalName());
                createMsgNo(); //Отправка сообщений потребителю
                return 1;
            }
        } else {
            log.info("Победителя торгов нет...");
            return 1;
        }
    }

    /**
     * Формируем сообщение победителю для заключения контракта
     */
    private void createMsgContract() {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.CFP); //CFP - тип сообщения с контрактом
        firstMsg.setContent(supplierSave.getMinPrice() + "," + supplierSave.getPower());
        firstMsg.setProtocol("Auction " + topic);
        firstMsg.addReceiver(supplierSave.getAgent());
        myAgent.send(firstMsg);
    }
    private void createMsgNo(){
        ACLMessage firstMsg = new ACLMessage(ACLMessage.CANCEL); //CANCEL - тип сообщения, оповещающий, что нет хороших предложений
        firstMsg.setContent("Нет хороших предложений");
        firstMsg.setProtocol("Auction " + topic);
        firstMsg.addReceiver(new AID(nameConsumer(), false));
        myAgent.send(firstMsg);
    }

    private String nameConsumer() {
        char[] crh = myAgent.getLocalName().toCharArray();
        String nameSupp = "AgentConsumer" + crh[crh.length - 1];
        return nameSupp;
    }
}
