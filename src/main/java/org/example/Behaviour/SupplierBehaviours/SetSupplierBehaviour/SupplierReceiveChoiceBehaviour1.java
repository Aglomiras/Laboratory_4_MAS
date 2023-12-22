package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.SupplierSave;

@Slf4j
@Data
public class SupplierReceiveChoiceBehaviour1 extends Behaviour {
    private Double minPriceCons = null;
    private String agentMinMin = "";
    private AID agentMin = null;
    private ACLMessage bestOffer = null;
    private MessageTemplate messageTemplate;
    private double minCons;
    private String protocol;
    private SupplierSave supplierSave;
    private String topic = "Auction " ;
    public SupplierReceiveChoiceBehaviour1(SupplierSave supplierSave, String topic){
        this.supplierSave = supplierSave;
        this.topic += topic;
    }

    @Override
    public void onStart() {
//        this.messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        this.messageTemplate = MessageTemplate.and(MessageTemplate.MatchProtocol(topic), MessageTemplate.MatchPerformative(ACLMessage.AGREE));
    }

    @Override
    public void action() {
        ACLMessage propose = myAgent.receive(messageTemplate);
        if (propose != null) {
            double price = -1;

            try {
                price = Double.parseDouble(propose.getContent().split(",")[0]); //Содержит предлагаемую цену от агента-участника
                this.minCons = Double.parseDouble(propose.getContent().split(",")[1]);

                log.info("Получил предложение с ценой {} от агента {}. Получил {}", price, propose.getSender().getLocalName(), this.myAgent.getLocalName());
            } catch (NumberFormatException e) {
                log.warn("NumberFormatException " + propose.getSender().getLocalName());
            }

            if (price > 0 && (supplierSave.getMinPrice() == 0 || price < supplierSave.getMinPrice())) {
                this.supplierSave.setMinPrice(price);
                this.supplierSave.setAgent(propose.getSender());
                this.supplierSave.setAgentNamNam(propose.getSender().getLocalName());
                this.supplierSave.setProtocol(propose.getProtocol());
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
