package org.example.Behaviour.ConsumerBehaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.ConsumerSave;

@Slf4j
public class ConsumerComebackBehaviour extends Behaviour {
    private MessageTemplate messageTemplate;
    private ConsumerSave consumerSave;

    public ConsumerComebackBehaviour(ConsumerSave consumerSave) {
        this.consumerSave = consumerSave;
    }

    @Override
    public void onStart() {
        messageTemplate = MessageTemplate.or(
                MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM),
                MessageTemplate.MatchPerformative(ACLMessage.CANCEL));
    }

    @Override
    public void action() {
        ACLMessage comebackMsg = myAgent.receive(messageTemplate);
        if (comebackMsg != null) {
            consumerSave.setCount(consumerSave.getCount() + 1);
            if (comebackMsg.getPerformative() == ACLMessage.DISCONFIRM) {
                String content = comebackMsg.getContent().split(",")[1];
                consumerSave.setPositiveCount(consumerSave.getPositiveCount() + 1);
                if (content.equals("Yes")) {
                    consumerSave.setPositiveCountPos(consumerSave.getPositiveCountPos() + 1);
                    log.info("Сделка совершена. Спасибо поставщик {}. Потребитель {} доволен!", comebackMsg.getSender().getLocalName(), this.myAgent.getLocalName());
                } else {
                    consumerSave.setNegativeCountNeg(consumerSave.getNegativeCountNeg() + 1);
                    log.info("Контракт в процессе отменился... {} остается без света", this.myAgent.getLocalName());
                }
            } else {
                consumerSave.setNegativeCount(consumerSave.getNegativeCount() + 1);
                log.info("Хороших предложений нет. Спасибо поставщик {}. Потребитель {} огорчен...", comebackMsg.getSender().getLocalName(), this.myAgent.getLocalName());
            }
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
