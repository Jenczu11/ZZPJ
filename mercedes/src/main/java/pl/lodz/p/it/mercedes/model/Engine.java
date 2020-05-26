package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import pl.lodz.p.it.mercedes.model.engines.FuelConsumptionUnit;

@Data
@Builder
public class Engine {
    private String fuelType;
    private String emissionStandard;
    private String driveType;
    private String fuelConsumption;
    private FuelConsumptionUnit fuelConsumptionUnit;
}
