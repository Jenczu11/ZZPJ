package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

@Data
@Builder
public class Engine {
    private String fuelType;
    private String driveConcept;
    private Double powerHp;
    private Double powerKw;
    private String cylinder;
    private Integer cylinderValves;
    private Integer capacity;
    private String driveType;
//   unit l/100km
    private Double fuelConsumptionMin;
}
