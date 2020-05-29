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
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestMongoConfiguration.class)
@ExtendWith(TestSuiteExtension.class)

class LogicTest {
    @Autowired
    private CarRepository carRepository;
    private CarService carService;
    @Autowired
    private ReviewRepository reviewRepository;
    private ReviewService reviewService;
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


        carRepository.insert(cars);
        carService = new CarService(carRepository);

        reviewService = new ReviewService(reviewRepository);
        Review review1 = Review.builder().carId("1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-30T10:50:00.000")).build();
        Review review2 = Review.builder().carId("1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-30T10:50:01.000")).build();

    }

    @AfterEach
    void tearDown() {
        carRepository.deleteAll();
        reviewRepository.deleteAll();
    }


    @Test
    @DisplayName("One user can send only one review for car")
    void checkIfReviewForCarExists() throws CarNotFoundException {
        Review review1 = Review.builder().carId("1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-29T10:50:00.000")).build();
        Review review2 = Review.builder().carId("1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-30T10:50:01.000")).build();

        reviewService.addReview(review1);

//        carService.updateRating("1",review1);
        Car car = carService.getCarById("1");

        assertEquals(reviewService.getReviewByCarId("1").size(),1);

        assertEquals(car.getReviewList().size(),1);

        reviewService.addReview(review2);
//        carService.updateRating("1",review2);
    }

}