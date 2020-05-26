package pl.lodz.p.it.mercedes.model.engines;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.lodz.p.it.mercedes.model.Engine;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Electric extends Engine {
    @Builder.Default
    private FuelConsumptionUnit fuelConsumptionUnit = FuelConsumptionUnit.kWh_100km;
    @Builder.Default
    private String fuelType = "ELECTRIC";

}
