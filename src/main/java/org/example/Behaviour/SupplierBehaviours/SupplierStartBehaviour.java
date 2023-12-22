package org.example.Behaviour.SupplierBehaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.Support.VirtualTime;

/**
 * SupplierStartBehaviour(Behaviour) -> запускает SupplierParallelFirstBehaviour(ParallelBehaviour), при изменении часа
 */
public class SupplierStartBehaviour extends Behaviour {
    private VirtualTime virtualTime = VirtualTime.getInstance(); //Виртуальное время
    private MessageTemplate messageTemplate; //Тип сообщений

    @Override
    public void onStart() {
        messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROXY);
    }

    @Override
    public void action() {
        ACLMessage mesStart = myAgent.receive(messageTemplate);
        if (mesStart != null) {
            double power = Double.parseDouble(mesStart.getContent().split(",")[0]);
            double price = Double.parseDouble(mesStart.getContent().split(",")[1]);
            String consumer = mesStart.getSender().getLocalName();
            /**Запуск поиска мощности для потребителя*/
            myAgent.addBehaviour(new SupplierParallelFirstBehaviour(power, price, consumer, virtualTime.getTimeMils(0.9)));
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
