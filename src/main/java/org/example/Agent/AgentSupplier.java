package org.example.Agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.SupplierBehaviours.SupplierStartBehaviour;

@Slf4j
public class AgentSupplier extends Agent {
    @Override
    protected void setup() {
        log.info("{} was born", this);
        this.addBehaviour(new SupplierStartBehaviour());
    }
}
