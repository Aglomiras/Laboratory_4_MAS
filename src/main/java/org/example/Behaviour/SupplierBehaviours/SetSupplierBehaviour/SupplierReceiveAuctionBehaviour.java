package org.example.Behaviour.SupplierBehaviours.SetSupplierBehaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * SupplierReceiveAuctionBehaviour(Behaviour) -> ждет сообщений от производителей о том, что они не могут обеспечить требуемую мощность
 */
public class SupplierReceiveAuctionBehaviour extends Behaviour {
    private MessageTemplate messageTemplate;
    private int count = 0;
    private int countMsg;
    private boolean flagRecBeh = false;

    public SupplierReceiveAuctionBehaviour(int countMsg) {
        this.countMsg = countMsg;
    }

    @Override
    public void onStart() {
        messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.FAILURE);
    }

    @Override
    public void action() {
        ACLMessage receive = myAgent.receive(messageTemplate);
        if (receive != null) {
            count++;
            if (count == countMsg) {
                flagRecBeh = true;
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return flagRecBeh;
    }

    @Override
    public int onEnd() {
        if (flagRecBeh) {
            return 0;
        } else {
            return 1;
        }
    }
}
