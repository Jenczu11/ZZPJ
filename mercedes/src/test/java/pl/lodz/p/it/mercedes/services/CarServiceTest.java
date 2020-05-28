package pl.lodz.p.it.mercedes.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.mercedes.TestMongoConfiguration;
import pl.lodz.p.it.mercedes.TestSuiteExtension;
import pl.lodz.p.it.mercedes.exceptions.CarNotFoundException;
import pl.lodz.p.it.mercedes.model.*;
import pl.lodz.p.it.mercedes.model.engines.EngineFactory;
import pl.lodz.p.it.mercedes.repositories.CarRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestMongoConfiguration.class)
@ExtendWith(TestSuiteExtension.class)

class CarServiceTest {
    @Autowired
    private CarRepository repository;
    private CarService carService;

    @BeforeEach
    void setUp() {
        List<Car> cars = new ArrayList<>();
        ArrayList<String> enginesFuelTypes = new ArrayList<String>(
                Arrays.asList("Diesel", "Super", "SuperPlus","Electric")
        );
        IntStream.range(0,enginesFuelTypes.size()).forEach(i -> {
            String id = Integer.toString(i);
            String fuelType = enginesFuelTypes.get(i);
            Engine engine = EngineFactory.getEngine(fuelType, "Euro 6d ISC-FCM", "4x4",
                    "4.5", 190.0, 140.0, "4", 4.0, 1950.0);

            Transmission transmission = Transmission.builder().type("8G-DCT").name("auto").build();

            CarTechnicalInformation carTechnicalInformation = CarTechnicalInformation.builder()
                    .acceleration(6.9).topSpeed(240.0).doors(4.0).seats(5.0).mass(1620.0)
                    .engine(engine).transmission(transmission).build();

            Car car = Car.builder()
                    .id(id)
                    .modelId(id)
                    .name(String.format("car %d",i))
                    .className(String.format("klasa %d",i))
                    .bodyName(String.format("body %d",i))
                    .price(159000.0)
                    .carTechnicalInformation(carTechnicalInformation)
                    .build();
            cars.add(car);
        });


        repository.insert(cars);
        carService = new CarService(repository);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void addCar() {
    }


    @ParameterizedTest
    @ValueSource(strings = {"car 1","car 2","car 3"})
    void getCarWithName(String carName) throws CarNotFoundException {
        assertEquals(carService.getCarWithName(carName).size(),1);
    }

    @ParameterizedTest
    @DisplayName("Throw carNotFound on wrong car name")
    @ValueSource(strings = {"you won't find this car"})
    void givenNonExistCar_throwCarNotFoundException(String carName) throws CarNotFoundException {
        Exception exception = assertThrows(CarNotFoundException.class, () -> {
            carService.getCarWithName(carName);
        });

        String expectedMessage = "Car not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Get car by id")
    @ParameterizedTest(name = "Car ID = {0}")
    @ValueSource(strings = {"1","2","3"})
    void getCarById(String id) throws CarNotFoundException {
        List<Car> cars = new ArrayList<>();
        cars.add(carService.getCarById(id));
        assertEquals(cars.size(),1);
    }

    @Test
    void getAllCars() {
        assertEquals(carService.getAllCars().size(),4);
    }

    @Test
    void deleteCar() {
    }

    @Test
    void updateRating() {
    }
}