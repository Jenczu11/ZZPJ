package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

@Data
@Builder
public class Engine {
    private String fuelType;
    private String emissionStandard;
    private Long powerHp;
    private Long powerKw;
    private String cylinder;
    private Long cylinderValves;
    private Long capacity;
    private String driveType;
//   unit l/100km
    private Double fuelConsumptionMin;
}
