package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupplierFail extends OneShotBehaviour {
    @Override
    public void action() {
        log.info("Что то пошло не так... Простите {} ", myAgent.getLocalName());
    }
}
