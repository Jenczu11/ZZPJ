package pl.lodz.p.it.mercedes.model.engines;

import lombok.Builder;
import lombok.Data;
import pl.lodz.p.it.mercedes.model.Engine;

@Data
@Builder
public class Electric extends Engine {
    @Builder.Default
    private FuelConsumptionUnit fuelConsumptionUnit = FuelConsumptionUnit.kWh_100km;
}
