package org.example.Support;

import org.example.Model.RegCFGXml;
import org.example.Model.XmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParserXml {
    private String nameAgent;
    Optional<RegCFGXml> parse;

    public ParserXml(String nameAgent) {
        this.nameAgent = nameAgent;
        parse = XmlUtils.parse(this.nameAgent, RegCFGXml.class);
    }

    public String agentNameCons(){
        StringBuilder findAgent = new StringBuilder();
        parse.ifPresent(e-> findAgent.append(e.getAgent()));
        String findAg = findAgent.toString();
        return findAg;
    }

    public double priceAgent(){
        double price = -1;
        price = parse.get().getPricePower();
        return price;
    }

    public List<Double> powerHour() {
        List<Double> findPowers= new ArrayList<>();
        parse.ifPresent(e -> findPowers.addAll(e.getPowerList()));
        return findPowers;
    }
}
