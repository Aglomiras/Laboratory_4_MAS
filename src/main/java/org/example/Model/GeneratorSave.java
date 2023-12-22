package org.example.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**Dto - для generator*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorSave {
    private double currentPrice;
    private double price;
}
