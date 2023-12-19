package org.example.Behaviour.ConsumerBehaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerBehaviour extends Behaviour {
    private double powerHour = 20;
    private double priceOfEnergy = 15;
    private boolean flagBeh = false;
    private MessageTemplate messageTemplate;

    @Override
    public void onStart() {
        sendMsg(this.powerHour, this.priceOfEnergy);
        log.info("{} отправил запрос о покупке мощности", this.myAgent.getLocalName());
        messageTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM), MessageTemplate.MatchPerformative(ACLMessage.CANCEL));

    }

    @Override
    public void action() {
        ACLMessage msgRec = myAgent.receive(messageTemplate);
        if (msgRec != null) {
            if (msgRec.getPerformative() == ACLMessage.DISCONFIRM) {
                log.info("Сделка была совершена. Спасибо поставщик - {}. Потребитель {} доволен!", msgRec.getSender().getLocalName(), this.myAgent.getLocalName());
            } else {
                log.info("Не удалось найти хорошее предложение. Спасибо поставщик - {}. Потребитель {} огорчен...", msgRec.getSender().getLocalName(), this.myAgent.getLocalName());
            }
            this.flagBeh = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return flagBeh;
    }

    @Override
    public int onEnd() {
        return super.onEnd();
    }

    public String generatCopy(String agent){
        char[] crh = agent.toCharArray();
        String nameGenerator = "AgentSupplier" + crh[crh.length-1];
        return nameGenerator;
    }

    public void sendMsg(double powerHour, double priceOfEnergy){
        ACLMessage msg = new ACLMessage(ACLMessage.PROXY);
        msg.setContent(powerHour + "," + priceOfEnergy);
//        msg.setProtocol("Power");
        msg.addReceiver(new AID(generatCopy(this.myAgent.getLocalName()), false));
        myAgent.send(msg);
    }
}
