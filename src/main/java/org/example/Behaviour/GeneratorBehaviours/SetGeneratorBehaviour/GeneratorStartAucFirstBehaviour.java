package org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.GeneratorSave;

/**
 * GeneratorStartAucFirstBehaviour(Behaviour) -> запуск GeneratorWaitForReceiveBehaviour(ParallelBehaviour),
 * если мощности для участия в аукционе достаточно. В противном случае отправит сообщение агенту-продавцу, что не может обеспечить
 * покрытие заданной нагрузки и будет ждать повторного предложения, но с разделенным контрактом.
 */
@Slf4j
public class GeneratorStartAucFirstBehaviour extends Behaviour {
    private double power;
    private double price;
    private long times;
    private boolean flag = false;
    private int count = 0; //Счетчик успешных ответов
    private int globalCount = 0; //Счетчик принятых сообщений

    public GeneratorStartAucFirstBehaviour(double power, double price, long times) {
        this.power = power;
        this.price = price;
        this.times = times;
    }

    @Override
    public void action() {
        ACLMessage receive = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchProtocol("Auction grand"),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE)));
        if (receive != null) {
            globalCount++; //Для теста сразу с тремя потребителями
            String topic = receive.getContent().split(",")[0];
            double powerCons = Double.parseDouble(receive.getContent().split(",")[1]);
            double priceCons = Double.parseDouble(receive.getContent().split(",")[2]);

            if (powerCons <= this.power) {
                this.myAgent.addBehaviour(new GeneratorWaitForReceiveBehaviour(
                        topic,
                        this.price,
                        receive.getSender(),
                        this.power,
                        priceCons,
                        receive.getProtocol(),
                        times));
                count++;
            } else {
                createMsgFail(receive.getSender(), receive.getProtocol()); //Отправка сообщений в случае нехватки мощности
                log.info("Нету необходимой мощности. {}", this.myAgent.getLocalName());
            }
            if (globalCount == 1) { // ==1 - для 1 продавца // ==3 - для трех продавцов
                flag = true;
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return flag;
    }

    @Override
    public int onEnd() {
        if (count == 1) { // ==1 - для тестов с одним потребителем и продавцом // ==3 - для трех
            return 0; //Сразу запустит поведения с ожиданием заключения контракта
        } else {
            return 1; //Запустит поведения с повторным приглашением на аукцион
        }
    }

    public void createMsgFail(AID aidAgent, String protocol) {
        ACLMessage msgReject = new ACLMessage(ACLMessage.FAILURE);
        msgReject.setContent("Нет столько мощности!");
        msgReject.setProtocol(protocol);
        msgReject.addReceiver(aidAgent);
        myAgent.send(msgReject);
    }
}
