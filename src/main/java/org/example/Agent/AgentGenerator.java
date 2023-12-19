package org.example.Agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.GeneratorBehaviours.GeneratorAuctionStartBehaviour;
import org.example.Behaviour.GeneratorBehaviours.GeneratorContractBehaviour;
import org.example.Support.DfHelper;

import java.util.Random;

@Slf4j
public class AgentGenerator extends Agent {
    @Override
    protected void setup() {
        log.info("{} was born", this);
        DfHelper.register(this, "generator");

        double minPrice = new Random().nextDouble() * 10 + 10;
        log.info("My min price {}", minPrice);
        this.addBehaviour(new GeneratorAuctionStartBehaviour(minPrice, 15));
        this.addBehaviour(new GeneratorContractBehaviour());
    }
}
