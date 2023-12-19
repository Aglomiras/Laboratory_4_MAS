package org.example.Agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.SupplierBehaviours.FSMSupplierBehaviour;
import org.example.Behaviour.SupplierBehaviours.SupplierContractBehaviour;
import org.example.Behaviour.SupplierBehaviours.SupplierStartAuctionBehaviour;
import org.example.Behaviour.SupplierBehaviours.SupplierWaitForAcceptBehaviour;
import org.example.Support.ParserXml;

@Slf4j
public class AgentSupplier extends Agent {
    @Override
    protected void setup() {
        log.info("{} was born", this);
//        addBehaviour(new SupplierStartAuctionBehaviour());
//        addBehaviour(new SupplierBehaviour());
//        addBehaviour(new SupplierWaitForAcceptBehaviour());
//        addBehaviour(new SupplierContractBehaviour());

//        ParserXml parserXml = new ParserXml("src/main/java/org/example/Resources/" + this.getLocalName() + ".xml");
//        double power = parserXml.powerHour().get(0);
//
//        this.addBehaviour(new FSMSupplierBehaviour(power));
        this.addBehaviour(new FSMSupplierBehaviour(20));
    }
}
