package Laboratory_4;

import Kit.MasStarterKit;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import lombok.SneakyThrows;
import org.example.Behaviour.ConsumerBehaviours.ConsumerComebackBehaviour;
import org.example.Behaviour.ConsumerBehaviours.ConsumerStartBehaviour;
import org.example.Behaviour.GeneratorBehaviours.GeneratorStartBehaviour;
import org.example.Behaviour.SupplierBehaviours.SupplierStartBehaviour;
import org.example.Model.ConsumerSave;
import org.example.Support.DfHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    private MasStarterKit kit = new MasStarterKit();

    /**
     * Сценарий 1: Торги с единственным производителем. Задать такое количество покупаемой мощности,
     * чтобы только 1 поставщик смог удовлетворить запросу. Ожидаемый результат: агент-производитель
     * продает по завышенной цене мощность, однако контракт отклоняется поставщиком из-за большой цены.
     */
    @Test
    @SneakyThrows
    public void Test1() {
        kit.startJade(List.of("jade.core.messaging.TopicManagementService"));
        List<Double> gen1 = new ArrayList<>();
        gen1.add(21.0);
        List<Double> gen2 = new ArrayList<>();
        gen2.add(19.0);
        List<Double> gen3 = new ArrayList<>();
        gen3.add(17.0);
        List<Double> cons = new ArrayList<>();
        cons.add(20.0);

        ConsumerSave consumerSave = new ConsumerSave();
        Behaviour SupplierStartBehaviour = new SupplierStartBehaviour();
        Behaviour[] AgentConsumer1 = createConsumerBeh(1, "AgentSupplier1", cons, consumerSave);
        Behaviour AgentGenerator1 = createSuppBehavior(gen1, 1);
        Behaviour AgentGenerator2 = createSuppBehavior(gen2, 1);
        Behaviour AgentGenerator3 = createSuppBehavior(gen3, 1);

        kit.createAgent("AgentSupplier1", SupplierStartBehaviour);
        kit.createAgent("AgentConsumer1", AgentConsumer1);
        kit.createAgent("AgentGenerator1", registerInDf("generator"), AgentGenerator1);
        kit.createAgent("AgentGenerator2", registerInDf("generator"), AgentGenerator2);
        kit.createAgent("AgentGenerator3", registerInDf("generator"), AgentGenerator3);

        Thread.sleep(20000);
        System.out.println(consumerSave.getPositiveCountPos());
        assertEquals(0, consumerSave.getPositiveCountPos());
    }

    /**
     * Сценарий 2: Успешный аукцион с двумя участниками. Задать такое количество покупаемой мощности,
     * чтобы два поставщика смогли удовлетворить запросу и начали процесс снижения цены.
     * Ожидаемый результат: агенты соревнуются друг с другом для право продать ЭЭ, и один из агентов-производителей
     * продает по удовлетворительной цене запрошенную мощность.
     */
    @Test
    @SneakyThrows
    public void Test2() {
        kit.startJade(List.of("jade.core.messaging.TopicManagementService"));
        List<Double> gen1 = new ArrayList<>();
        gen1.add(20.1);
        List<Double> gen2 = new ArrayList<>();
        gen2.add(20.1);
        List<Double> gen3 = new ArrayList<>();
        gen3.add(17.0);
        List<Double> cons = new ArrayList<>();
        cons.add(20.0);

        ConsumerSave consumerSave = new ConsumerSave();
        Behaviour SupplierStartBehaviour = new SupplierStartBehaviour();
        Behaviour[] AgentConsumer1 = createConsumerBeh(4, "AgentSupplier1", cons, consumerSave);
        Behaviour AgentGenerator1 = createSuppBehavior(gen1, 1);
        Behaviour AgentGenerator2 = createSuppBehavior(gen2, 1.1);
        Behaviour AgentGenerator3 = createSuppBehavior(gen3, 1);

        kit.createAgent("AgentSupplier1", SupplierStartBehaviour);
        kit.createAgent("AgentConsumer1", AgentConsumer1);
        kit.createAgent("AgentGenerator1", registerInDf("generator"), AgentGenerator1);
        kit.createAgent("AgentGenerator2", registerInDf("generator"), AgentGenerator2);
        kit.createAgent("AgentGenerator3", registerInDf("generator"), AgentGenerator3);

        Thread.sleep(20000);
        System.out.println(consumerSave.getPositiveCountPos());
        assertEquals(1, consumerSave.getPositiveCountPos());
    }

    /**
     * Сценарий 3: Дефицит мощности в системе. Задать такое количество покупаемой мощности,
     * что ни один производитель не может полностью удовлетворить запрос. Ожидаемый результат: агент дистрибьютер
     * должен разбить контракт на несколько частей и закупить требуемое количество у различных поставщиков.
     */
    @Test
    @SneakyThrows
    public void Test3() {
        kit.startJade(List.of("jade.core.messaging.TopicManagementService"));
        List<Double> gen1 = new ArrayList<>();
        gen1.add(15.1);
        List<Double> gen2 = new ArrayList<>();
        gen2.add(15.1);
        List<Double> gen3 = new ArrayList<>();
        gen3.add(7.2);
        List<Double> cons = new ArrayList<>();
        cons.add(20.0);

        ConsumerSave consumerSave = new ConsumerSave();
        Behaviour SupplierStartBehaviour = new SupplierStartBehaviour();
        Behaviour[] AgentConsumer1 = createConsumerBeh(40, "AgentSupplier1", cons, consumerSave);
        Behaviour AgentGenerator1 = createSuppBehavior(gen1, 1);
        Behaviour AgentGenerator2 = createSuppBehavior(gen2, 1.1);
        Behaviour AgentGenerator3 = createSuppBehavior(gen3, 1);

        kit.createAgent("AgentSupplier1", SupplierStartBehaviour);
        kit.createAgent("AgentConsumer1", AgentConsumer1);
        kit.createAgent("AgentGenerator1", registerInDf("generator"), AgentGenerator1);
        kit.createAgent("AgentGenerator2", registerInDf("generator"), AgentGenerator2);
        kit.createAgent("AgentGenerator3", registerInDf("generator"), AgentGenerator3);

        Thread.sleep(25000);
        System.out.println(consumerSave.getPositiveCountPos());
        assertEquals(2, consumerSave.getPositiveCountPos());
    }

    private Behaviour registerInDf(String serviceToReg) {
        return new OneShotBehaviour() {
            @Override
            public void action() {
                DfHelper.register(this.getAgent(), serviceToReg);
            }
        };
    }

    private Behaviour[] createConsumerBeh(double priceMin, String nameSup, List<Double> powers, ConsumerSave consumerSave) {
        Behaviour sendOffer = new ConsumerStartBehaviour(priceMin, nameSup, powers);
        Behaviour getOrderResult = new ConsumerComebackBehaviour(consumerSave);
        return new Behaviour[]{sendOffer, getOrderResult};
    }

    private Behaviour createSuppBehavior(List<Double> powers, double mod) {
        return new OneShotBehaviour() {
            Behaviour subBeh;

            @Override
            public void action() {
                subBeh = new GeneratorStartBehaviour(mod, powers);
                getAgent().addBehaviour(subBeh);
            }

            @Override
            public int onEnd() {
                return subBeh.onEnd();
            }
        };
    }
}
