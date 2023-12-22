package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

/**
 * Поведение оповещает об успехе
 */
@Slf4j
public class SupplierSuccess extends OneShotBehaviour {
    @Override
    public void action() {
        log.info("Все чики-пуки! {}", this.myAgent.getLocalName());
    }
}
