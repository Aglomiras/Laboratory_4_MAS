package org.example.Behaviour.GeneratorBehaviours;

import jade.core.behaviours.Behaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Support.VirtualTime;

import java.util.List;

/**
 * GeneratorStartBehaviour(Behaviour) -> запускает GeneratorParallelBehaviour(ParallelBehaviour),
 * для приема сообщений на участие в аукционе.
 */
@Slf4j
public class GeneratorStartBehaviour extends Behaviour {
    private double price;
    private VirtualTime virtualTime;
    private int countHour; //Переменная нужна, для того, чтобы в один час виртуального времени не отсылалось много сообщений
    private List<Double> powerHourList;

    public GeneratorStartBehaviour(double price, List<Double> powerHourList) {
        this.price = price;
        this.powerHourList = powerHourList;
        this.virtualTime = VirtualTime.getInstance();
        this.countHour = virtualTime.getTimeHour() - 1;
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
            double price = this.price * power; //Определяем цену, за которую мы готовы эту мощность купить

            log.info("Текущее время {} ч. Я вырабатываю {} мощности. Готов продать за {} руб.", timeActual, power, price);
            myAgent.addBehaviour(new GeneratorParallelBehaviour(power, price, virtualTime.getTimeMils(0.9)));
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
