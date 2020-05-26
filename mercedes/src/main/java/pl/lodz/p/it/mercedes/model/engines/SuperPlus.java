package pl.lodz.p.it.mercedes.model.engines;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.lodz.p.it.mercedes.model.Engine;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SuperPlus extends Engine {
    private Double powerHp;
    private Double powerKw;
    private String cylinder;
    private Double cylinderValves;
    private Double capacity;

    @Builder.Default
    private FuelConsumptionUnit fuelConsumptionUnit = FuelConsumptionUnit.l_100km;
    @Builder.Default
    private String fuelType = "SUPERPLUS";

}
