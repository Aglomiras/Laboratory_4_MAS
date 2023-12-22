package org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour;

import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

/**
 * Выводит сообщение об успехе
 */
@Slf4j
public class GeneratotSuccess extends OneShotBehaviour {
    @Override
    public void action() {
        log.info("Все успешно! {} заключил контракт", this.myAgent.getLocalName());
    }
}
