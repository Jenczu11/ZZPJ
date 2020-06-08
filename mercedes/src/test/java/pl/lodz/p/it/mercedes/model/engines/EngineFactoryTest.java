package pl.lodz.p.it.mercedes.model.engines;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.lodz.p.it.mercedes.model.Engine;

import static org.junit.jupiter.api.Assertions.*;

class EngineFactoryTest {

    @DisplayName("Is engine factory returning good class")
    @ParameterizedTest(name = "{0} fueltype should return engine {9}")
    @CsvSource({
            "'Diesel','Euro 6d ISC-FCM','4x4','4.5', 190.0, 140.0,'4', 4.0, 1950.0, 'class pl.lodz.p.it.mercedes.model.engines.Diesel'",
            "'Super','Euro 6d ISC-FCM','4x4','4.5', 190.0, 140.0,'4', 4.0, 1950.0, 'class pl.lodz.p.it.mercedes.model.engines.Super'",
            "'Superplus','Euro 6d ISC-FCM','4x4','4.5', 190.0, 140.0,'4', 4.0, 1950.0, 'class pl.lodz.p.it.mercedes.model.engines.SuperPlus'",
            "'Electric','Euro 6d ISC-FCM','4x4','4.5', 190.0, 140.0,'4', 4.0, 1950.0, 'class pl.lodz.p.it.mercedes.model.engines.Electric'",
    })
    void getEngine(String fuelType, String emissionStandard, String driveType, String fuelConsumption,
                   Double powerHp, Double powerKw, String cylinder, Double cylinderValves, Double capacity,
                   String className) {

        Engine engine = EngineFactory.getEngine(fuelType, emissionStandard, driveType, fuelConsumption,
                powerHp, powerKw, cylinder, cylinderValves, capacity);

        System.out.println(engine.getClass() + ":" + className);
        assertEquals(engine.getClass().toString(), className);
    }
}