package org.example.Behaviour.GeneratorBehaviours.SetGeneratorBehaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * GeneratorContractBehaviour(Behaviour) - поведение, реализующее заключение контракта
 */
@Slf4j
public class GeneratorContractBehaviour extends Behaviour {
    private double power;
    private MessageTemplate messageTemplate;

    public GeneratorContractBehaviour(double power) {
        this.power = power;
        messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
    }

    @Override
    public void action() {
        ACLMessage receive = myAgent.receive(messageTemplate);
        if (receive != null) {
            System.out.println(power + " " + receive.getContent().split(",")[1]);
            log.info("Получил сообщение от {} с предложением о заключении контракта на мощность", receive.getSender().getLocalName());
            diffPower(receive); //Проверка имеющейся мощности
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    private synchronized void diffPower(ACLMessage message) {
        double powerMsg = Double.parseDouble(message.getContent().split(",")[1]);
        if (power > powerMsg) {
            sendBet(Double.parseDouble(message.getContent().split(",")[0]), message.getSender(), message.getProtocol());
            log.info("Мощность есть!");
        } else {
            sendErr(Double.parseDouble(message.getContent().split(",")[0]), message.getSender(), message.getProtocol());
            log.info("Мощность уже продана...");
        }
        this.power = this.power - powerMsg;
    }
    /**Контракт успешен*/
    private void sendBet(double price, AID agent, String protocol) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.CONFIRM);
        firstMsg.setContent(price + "");
        firstMsg.setProtocol(protocol);
        firstMsg.addReceiver(agent);
        myAgent.send(firstMsg);
    }
    /**Контракт не успешен*/
    private void sendErr(double price, AID agent, String protocol) {
        ACLMessage firstMsg = new ACLMessage(ACLMessage.REQUEST);
        firstMsg.setContent(price + "");
        firstMsg.setProtocol(protocol);
        firstMsg.addReceiver(agent);
        myAgent.send(firstMsg);
    }
}
