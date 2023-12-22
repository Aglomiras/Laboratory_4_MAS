package org.example.Behaviour.GeneratorBehaviours.FSMGenerator;

import jade.core.behaviours.FSMBehaviour;
import org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour.*;

/**
 * FSMGeneratorBehaviour(FSMBehaviour) -> запускает дерево сценариев
 */
public class FSMGeneratorBehaviour extends FSMBehaviour {
    private double power;
    private double price;
    private long times;

    public FSMGeneratorBehaviour(double power, double price, long times) {
        this.power = power;
        this.price = price;
        this.times = times;
    }

    private final String
            FIRST_START = "first_start", //GeneratorStartAucFirstBehaviour (Проверка: осилим ли полный контракт)
            SECOND_START = "second_start", //GeneratorStartAucSecondBehaviour (Проверка: осилим ли части контракта)
            CONTRACT = "contract", //GeneratorContractBehaviour (Заключение контракта)
            FAIL = "fail", //GeneratorFail (Провал)
            SUCCESS = "success"; //GeneratotSuccess (Успех)

    @Override
    public void onStart() {
        this.registerFirstState(new GeneratorStartAucFirstBehaviour(power, price, times / 8), FIRST_START);
        this.registerState(new GeneratorStartAucSecondBehaviour(power, price, times / 8), SECOND_START);
        this.registerState(new GeneratorContractBehaviour(power), CONTRACT);
        this.registerLastState(new GeneratorFail(), FAIL);
        this.registerLastState(new GeneratotSuccess(), SUCCESS);

        this.registerTransition(FIRST_START, CONTRACT, 0);
        this.registerTransition(FIRST_START, SECOND_START, 1);

        this.registerTransition(SECOND_START, CONTRACT, 0);
        this.registerTransition(SECOND_START, FAIL, 1);
    }
}
