package org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * GeneratorStartAucSecondBehaviour(Behaviour) - аналогичен GeneratorStartAucFirstBehaviour(Behaviour)
 */
@Slf4j
public class GeneratorStartAucSecondBehaviour extends Behaviour {
    private double power;
    private double price;
    private long times;
    private boolean flag = false;
    private int count = 0; //Счетчик успешных ответов
    private int globalCount = 0; //Счетчик всех сообщений
    private MessageTemplate messageTemplate;
    private String agentName = "";

    public GeneratorStartAucSecondBehaviour(double power, double price, long times) {
        this.power = power;
        this.price = price;
        this.times = times;
    }

    @Override
    public void onStart() {
//        messageTemplate = MessageTemplate.or(
//                MessageTemplate.MatchProtocol("Auction min1"),
//                MessageTemplate.MatchProtocol("Auction min2"));
        messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
    }

    @Override
    public void action() {
        ACLMessage receive = myAgent.receive(messageTemplate);
        if (receive != null) {

            String topic = receive.getContent().split(",")[0];
            double powerCons = Double.parseDouble(receive.getContent().split(",")[1]);
            double priceCons = Double.parseDouble(receive.getContent().split(",")[2]);
            /**
             * Защита от переизбытка сообщений, если первая часть контракта от какого-либо агента пришла, то
             * ждет второй части контракта и заканчивает поведение
             * */
//            if (globalCount == 0) {
//                agentName = receive.getSender().getLocalName();
//            }
//            if (globalCount > 0 && receive.getSender().getLocalName().equals(agentName)) {
//                flag = true;
//            }
            globalCount++; // Для теста с одним потребителем и продавцом

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
            if (globalCount == 2){
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
        if (count != 0) {
            return 0;
        } else {
            return 1;
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
