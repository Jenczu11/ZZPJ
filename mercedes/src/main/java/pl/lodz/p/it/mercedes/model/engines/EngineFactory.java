package pl.lodz.p.it.mercedes.model.engines;

import lombok.Builder;
import pl.lodz.p.it.mercedes.model.Engine;

@Builder
public class EngineFactory {
    public static Engine getEngine(String fuelType, String emissionStandard, String driveType, String fuelConsumption, Double powerHp, Double powerKw, String cylinder, Double cylinderValves, Double capacity) {
         if(fuelType == null) {
            return null;
         }
         if(fuelType.equalsIgnoreCase("diesel")) {
             return Diesel.builder()
                        .emissionStandard(emissionStandard)
                        .powerHp(powerHp)
                        .powerKw(powerKw)
                        .cylinder(cylinder)
                        .cylinderValves(cylinderValves)
                        .capacity(capacity)
                        .driveType(driveType)
                .fuelConsumption(fuelConsumption)
                .build();
         }
        if(fuelType.equalsIgnoreCase("super")) {
            return Super.builder()
                    .fuelType(fuelType)
                    .emissionStandard(emissionStandard)
                    .powerHp(powerHp)
                    .powerKw(powerKw)
                    .cylinder(cylinder)
                    .cylinderValves(cylinderValves)
                    .capacity(capacity)
                    .driveType(driveType)
                    .fuelConsumption(fuelConsumption)
                    .build();
        }
        if(fuelType.equalsIgnoreCase("superplus")) {
            return SuperPlus.builder()
                    .fuelType(fuelType)
                    .emissionStandard(emissionStandard)
                    .powerHp(powerHp)
                    .powerKw(powerKw)
                    .cylinder(cylinder)
                    .cylinderValves(cylinderValves)
                    .capacity(capacity)
                    .driveType(driveType)
                    .fuelConsumption(fuelConsumption)
                    .build();
        }
        if(fuelType.equalsIgnoreCase("electric")) {
            return Electric.builder()
                    .fuelType(fuelType)
                    .emissionStandard(emissionStandard)
                    .fuelConsumption(fuelConsumption)
                    .build();
        }
        return null;
    }
}
