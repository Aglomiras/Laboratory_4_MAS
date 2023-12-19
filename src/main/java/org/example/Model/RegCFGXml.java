package org.example.Model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegCFGXml {
    @XmlElement
    private String agent;
    @XmlElement
    private double pricePower;
    @XmlElementWrapper(name = "powerHour")
    @XmlElement(name = "power")
    private List<Double> powerList;
}
