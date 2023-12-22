package org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour;

import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

/**
 * Выводит сообщение о неудаче
 */
@Slf4j
public class GeneratorFail extends OneShotBehaviour {
    @Override
    public void action() {
        log.info("{} не удалось заключить контракт...", this.myAgent.getLocalName());
    }
}
