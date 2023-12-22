package org.example.Support;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import org.example.Agent.AgentConsumer;
import org.example.Agent.AgentGenerator;
import org.example.Agent.AgentSupplier;

import java.util.List;
import java.util.Map;

public class JadeStarter {
    /**
     * Создание агентов через main
     */
    public static void main(String[] args) {
        Map<String, Class<?>> agents = Map.of(
                "AgentSupplier1", AgentSupplier.class,
                "AgentSupplier2", AgentSupplier.class,
                "AgentSupplier3", AgentSupplier.class,
                "AgentConsumer1", AgentConsumer.class,
                "AgentConsumer2", AgentConsumer.class,
                "AgentConsumer3", AgentConsumer.class,
                "AgentGenerator1", AgentGenerator.class,
                "AgentGenerator2", AgentGenerator.class,
                "AgentGenerator3", AgentGenerator.class
        );

        Properties props = new ExtendedProperties();
        props.setProperty("gui", "true");
        props.setProperty("agents", addAgents(agents));
        props.setProperty("services", addServices(List.of("jade.core.messaging.TopicManagementService")));
        ProfileImpl p = new ProfileImpl(props);

        Runtime.instance().setCloseVM(true);
        Runtime.instance().createMainContainer(p);
    }

    private static String addAgents(Map<String, Class<?>> createAgents) {
        String outString = "";
        for (Map.Entry<String, Class<?>> entry : createAgents.entrySet()) {
            outString += entry.getKey() + ":" + entry.getValue().getName() + ";";
        }
        System.out.println(outString);
        return outString;
    }

    private static String addServices(List<String> services) {
        String outString = "";
        for (String service : services) {
            outString += service + ";";
        }
        return outString;
    }
}
