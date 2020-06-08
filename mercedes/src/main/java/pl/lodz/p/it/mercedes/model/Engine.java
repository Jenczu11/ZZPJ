package pl.lodz.p.it.mercedes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Engine {
    private String emissionStandard;
    private String driveType;
    private String fuelConsumption;
}

