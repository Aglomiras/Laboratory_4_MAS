package org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.GeneratorSave;
import org.example.Support.TopicHelper;

/**
 * GeneratorTopicAuctionBehaviour(Behaviour) - поведение реализующее торги в топик-чате
 */
@Slf4j
@Data
public class GeneratorTopicAuctionBehaviour extends Behaviour {
    private final double minPrice; //Минимальная цена продажи
    private final String topicName; //Имя топика
    private double powerCons; //Необходимая мощность
    private double priceCons; //Цена, за которую готовы купить
    private double currentPrice;
    private AID topic; //Топик
    private boolean finishAuction = false;
    private boolean wake = true;
    private GeneratorSave generatorSave; //Объект, содержащий необходимые данные

    public GeneratorTopicAuctionBehaviour(String topicName, double minPrice, double power, double price, GeneratorSave generatorSave) {
        this.minPrice = minPrice;
        this.topicName = topicName;
        this.powerCons = power;
        this.priceCons = price;
        this.generatorSave = generatorSave;
        this.generatorSave.setPrice(price);
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, topicName);
        sendBet(minPrice * 2);
    }

    /**
     * Торги агентов в топик-чате
     */
    @Override
    public void action() {
        ACLMessage receive = myAgent.receive(MessageTemplate.MatchTopic(topic));
        if (receive != null && !receive.getSender().equals(myAgent.getAID())) {
            double otherBet = Double.parseDouble(receive.getContent().split(",")[0]);
            if (otherBet < currentPrice) {
                if (otherBet < minPrice) {
                    finishAuction = true; //Если получит ставку, меньше своей минимальной, выйдет из аукциона
                    wake = false;
                    log.info("Получил ставку {}, которая меньше моей минимальной цены {}. Выхожу из аукциона.", otherBet, minPrice);
                } else {
                    double myNewBet = priceRandom(otherBet);
                    log.info("Получил ставку {}, которая меньше моей предыдущей ставки {}. Отправляю новую {}", otherBet, currentPrice, myNewBet);
                    sendBet(myNewBet);
                }
            } else {
                log.info("Получена неверная форма {} от {}, но сообщение игнорируется, потому что моя предыдущая ставка {} меньше", otherBet, receive.getSender().getLocalName(), currentPrice);
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return finishAuction;
    }

    /**Случайное снижение цены*/
    private double priceRandom(double otherBet) {
        double random = 0.8 + 0.1 * Math.random();
        double myNewBet = otherBet * random;
        if (myNewBet < this.minPrice) {
            myNewBet = this.minPrice;
        }
        return myNewBet;
    }

    /**Формирования сообщения для торгов в топик-чате*/
    private void sendBet(double price) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.INFORM);
        firstMsg.setContent(price + "," + this.priceCons);
        currentPrice = price;
        generatorSave.setCurrentPrice(currentPrice);
        firstMsg.addReceiver(topic);
        myAgent.send(firstMsg);
    }

    @Override
    public int onEnd() {
        if (wake) {
            return 0; //Не выходил из торгов
        } else {
            return 1; //Вышел из торгов
        }
    }
}
