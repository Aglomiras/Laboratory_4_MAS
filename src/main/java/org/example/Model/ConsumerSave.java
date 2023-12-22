package org.example.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**Dto - для consumer*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerSave {
    private int count = 0;
    private int positiveCount = 0;
    private int negativeCount = 0;
    private int positiveCountPos = 0;
    private int negativeCountNeg = 0;
}
