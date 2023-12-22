package org.example.Behaviour.GeneratorBehaviours;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.GeneratorBehaviours.FSMGenerator.FSMGeneratorBehaviour;

/**
 * GeneratorParallelBehaviour(ParallelBehaviour):
 * -> WakerBehaviour(задаем время работы 0.85 часа)
 * -> FSMGeneratorBehaviour(FSMBehaviour)
 */
@Slf4j
public class GeneratorParallelBehaviour extends ParallelBehaviour {
    private Behaviour wakeupBeh;
    private FSMGeneratorBehaviour receiveBeh;
    private double power;
    private double price;
    private long times;


    public GeneratorParallelBehaviour(double power, double price, long times) {
        super(WHEN_ANY);
        this.power = power; //Мощность генератора в этот час
        this.price = price; //Цена этой мощности
        this.times = times; //Время, за которое надо успеть продать мощность
    }

    @Override
    public void onStart() {
        receiveBeh = new FSMGeneratorBehaviour(power, price, times);
        wakeupBeh = new WakerBehaviour(myAgent, times) {
            boolean wake = false;

            @Override
            protected void onWake() {
                wake = true;
                log.info("TIME IS UP FSM GENERATOR " + this.myAgent.getLocalName());
            }

            @Override
            public int onEnd() {
                return wake ? 0 : 1;
            }
        };

        this.addSubBehaviour(receiveBeh);
        this.addSubBehaviour(wakeupBeh);
    }
}
