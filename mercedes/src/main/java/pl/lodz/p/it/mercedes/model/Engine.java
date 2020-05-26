package pl.lodz.p.it.mercedes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import pl.lodz.p.it.mercedes.model.engines.FuelConsumptionUnit;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Engine {
    private String emissionStandard;
    private String driveType;
    private String fuelConsumption;
}

