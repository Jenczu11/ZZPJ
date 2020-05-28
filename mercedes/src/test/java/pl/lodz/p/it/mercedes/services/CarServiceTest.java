package pl.lodz.p.it.mercedes.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.mercedes.TestMongoConfiguration;
import pl.lodz.p.it.mercedes.TestSuiteExtension;
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
        ArrayList<String> engines = new ArrayList<String>(
                Arrays.asList("Diesel", "Super", "SuperPlus","Electric")
        );
        IntStream.range(0,engines.size()).forEach(i -> {
            String id = Integer.toString(i);
            Engine engine = EngineFactory.getEngine(engines.get(i), "Euro 6d ISC-FCM", "4x4",
                    "4.5", 190.0, 140.0, "4", 4.0, 1950.0);
            Transmission transmission = Transmission.builder().type("8G-DCT").name("auto").build();
            CarTechnicalInformation carTechnicalInformation = CarTechnicalInformation.builder()
                    .acceleration(6.9).topSpeed(240.0).doors(4.0).seats(5.0).mass(1620.0)
                    .engine(engine).transmission(transmission).build();
            Car car = Car.builder()
                    .modelId(id)
                    .name(String.format("car %d",i))
                    .className(String.format("klasa %d",i))
                    .bodyName(String.format("body %d",i))
                    .price(159000.0)
                    .carTechnicalInformation(carTechnicalInformation)
                    .build();
            cars.add(car);
        });


        System.out.println("yes");
        repository.insert(cars);
        carService = new CarService(repository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addCar() {
    }

    @Test
    void getCarWithName() {
    }

    @Test
    void getCarById() {
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