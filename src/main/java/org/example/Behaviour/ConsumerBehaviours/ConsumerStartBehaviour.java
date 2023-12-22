package org.example.Behaviour.ConsumerBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Support.VirtualTime;

import java.util.List;
@Slf4j
public class ConsumerStartBehaviour extends Behaviour {
    private double priceConsumer;
    private String nameSupplier;
    private VirtualTime virtualTime;
    private int countHour; //Переменная нужна, для того, чтобы в один час виртуального времени не отсылалось много сообщений
    private List<Double> powerHourList;

    public ConsumerStartBehaviour(double priceConsumer, String nameSupplier, List<Double> powerHourList) {
        this.priceConsumer = priceConsumer;
        this.nameSupplier = nameSupplier;
        this.powerHourList = powerHourList;
        this.virtualTime = VirtualTime.getInstance();
        this.countHour = virtualTime.getTimeHour() - 1; //Настраиваем начальные параметры
    }

    @Override
    public void action() {
        int timeActual = virtualTime.getTimeHour();

        /**Проверка обновления дня*/
        if (timeActual == 0 && countHour == 23) {
            countHour = -1;
        }
        /**Проверка на обновление часа*/
        if (timeActual - countHour > 0) {
            countHour = timeActual; //Обновление времени для следующего часа

            double power = powerHourList.get(timeActual); //Определяем какая мощность требуется в этот час
            double price = this.priceConsumer * power; //Определяем цену, за которую мы готовы эту мощность купить

            log.info("Текущее время {} ч. Мне необходимо {} мощности. Готов заплатить {} руб.", timeActual, power, price);

            creatAclMsg(power, price, this.nameSupplier);
            log.info("{} отправил запрос о покупке мощности", this.myAgent.getLocalName());
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    private void creatAclMsg(double power, double price, String name) {
        ACLMessage startMsg = new ACLMessage(ACLMessage.PROXY);
        startMsg.setContent(power + "," + price);
        startMsg.addReceiver(new AID(name, false));
        myAgent.send(startMsg);
    }
}
