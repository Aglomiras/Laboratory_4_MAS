package org.example.Agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Behaviour.GeneratorBehaviours.GeneratorStartBehaviour;
import org.example.Support.DfHelper;
import org.example.Support.ParserXml;

import java.util.List;

@Slf4j
public class AgentGenerator extends Agent {
    /**Агент генератор (производитель энергии):
     * - берет массив вырабатываемой мощности из xml-файла
     * - берет коэффициент цены на мощность из xml-файла
     *
     * Поведения:
     * - запуск ожидания сообщения на начало аукциона по истечению времени (Behaviour)
     * - -
     * */
    @Override
    protected void setup() {
        log.info("{} was born", this);
        DfHelper.register(this, "generator");

        ParserXml parserXml = new ParserXml("src/main/java/org/example/Resources/" + this.getLocalName() + ".xml");
        String name = parserXml.agentNameConsGen(); // Получаем имя агента-потребителя
        double priceMin = parserXml.priceAgent(); // Получаем коэффициент цены, за которую потребитель готов купить энергию
        List<Double> powerHours = parserXml.powerHour(); // Получаем график нагрузки потребителя

        this.addBehaviour(new GeneratorStartBehaviour(priceMin, powerHours));
    }
}
