package org.example.Agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.ConsumerBehaviours.ConsumerComebackBehaviour;
import org.example.Behaviour.ConsumerBehaviours.ConsumerStartBehaviour;
import org.example.Model.ConsumerSave;
import org.example.Support.ParserXml;

import java.util.List;

@Slf4j
public class AgentConsumer extends Agent {
    /**Агент потребитель:
     * - берет массив нагрузки из xml-файла
     * - берет коэффициент цены на мощность из xml-файла
     *
     * Поведения:
     * - отправка сообщения по истечению часа (Behaviour)
     * - прослушивание сообщений постоянно (Behaviour)
     * */
    @Override
    protected void setup() {
        log.info("{} was born", this);

        ParserXml parserXml = new ParserXml("src/main/java/org/example/Resources/" + this.getLocalName() + ".xml");
        String name = parserXml.agentNameConsGen(); // Получаем имя агента-потребителя
        double priceMin = parserXml.priceAgent(); // Получаем коэффициент цены, за которую потребитель готов купить энергию
        List<Double> powerHours = parserXml.powerHour(); // Получаем график нагрузки потребителя

        ConsumerSave consumerSave = new ConsumerSave();

        addBehaviour(new ConsumerStartBehaviour(priceMin, nameSupplier(), powerHours));
        addBehaviour(new ConsumerComebackBehaviour(consumerSave));
    }

    public String nameSupplier() {
        char[] crh = this.getLocalName().toCharArray();
        String nameSupp = "AgentSupplier" + crh[crh.length - 1];
        return nameSupp;
    }
}
