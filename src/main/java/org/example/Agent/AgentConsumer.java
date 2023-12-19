package org.example.Agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.ConsumerBehaviours.ConsumerBehaviour;

@Slf4j
public class AgentConsumer extends Agent {
    @Override
    protected void setup() {
        log.info("{} was born", this);
        addBehaviour(new ConsumerBehaviour());
    }
}
