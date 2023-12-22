package org.example.Model;

import jade.core.AID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**Dto - для supplier*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierSave {
    private double power;
    private double price;
    private String protocol;
    private double minPrice;
    private AID agent;
    private String agentNamNam;
}
