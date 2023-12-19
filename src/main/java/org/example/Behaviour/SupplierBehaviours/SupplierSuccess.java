package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupplierSuccess extends OneShotBehaviour {
    @Override
    public void action() {
        log.info("Все чики-пуки! {}", this.myAgent.getLocalName());
    }
}
