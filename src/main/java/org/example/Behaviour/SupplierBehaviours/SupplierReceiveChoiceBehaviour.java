package org.example.Behaviour.SupplierBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class SupplierReceiveChoiceBehaviour extends Behaviour {
    private List<Double> priceList = new ArrayList<>(); //?
    private List<AID> aidList = new ArrayList<>(); //?
    private Double minPriceCons = null;
    private String agentMinMin = "";
    private AID agentMin = null;
    private ACLMessage bestOffer = null;
    private MessageTemplate messageTemplate;

    @Override
    public void onStart() {
        this.messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
    }

    @Override
    public void action() {
        ACLMessage propose = myAgent.receive(messageTemplate);
        if (propose != null) {
            aidList.add(propose.getSender());
            double price = -1;

            try {
                price = Double.parseDouble(propose.getContent()); //Содержит предлагаемую цену от агента-участника
                priceList.add(price);
                log.info("Получил предложение с ценой {} от агента {}. Получил {}", price, propose.getSender().getLocalName(), this.myAgent.getLocalName());
            } catch (NumberFormatException e) {
                log.warn("NumberFormatException " + propose.getSender().getLocalName());
            }

            if (price > 0 && (this.minPriceCons == null || price < this.minPriceCons)) {
                this.minPriceCons = price; //Записывает цену, предлагаемую победителем
                this.agentMin = propose.getSender(); //Переопределяет сообщение от агента-участника, победившего в аукционе
                this.agentMinMin = propose.getSender().getLocalName(); //Переопределяет имя агента-участника, победившего в аукционе
                this.bestOffer = propose;
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

//    @Override
//    public int onEnd() {
//        if (bestOffer != null) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }
}
