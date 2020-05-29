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

import javax.lang.model.type.ArrayType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

    ArrayList<Review> createReviews(String carId, Integer numberOfReviews) {
        ArrayList<Review> reviews = new ArrayList<>();
        IntStream.range(0, numberOfReviews).forEach(ratingindex -> {
            Review.ReviewBuilder builder = Review.builder();
            builder.id(String.valueOf(ratingindex));
            builder.carId(carId);
            builder.userId("Test user");
            builder.performance(ThreadLocalRandom.current().nextInt(1, 5 + 1));
            builder.visualAspect(ThreadLocalRandom.current().nextInt(1, 5 + 1));
            builder.valueForMoney(ThreadLocalRandom.current().nextInt(1, 5 + 1));
            builder.reviewCreation(LocalDateTime.parse("2020-04-10T10:50:00.000").plusDays(ratingindex));
            Review review = builder.build();
            reviews.add(review);
        });
        return reviews;
    }

    @BeforeEach
    void setUp() {
        List<Car> cars = new ArrayList<>();
        ArrayList<String> enginesFuelTypes = new ArrayList<String>(
                Arrays.asList("Diesel", "Super", "SuperPlus", "Electric")
        );
        IntStream.range(0, enginesFuelTypes.size()).forEach(i -> {
            Car car = createTestCar(enginesFuelTypes, i);
            cars.add(car);
        });


        repository.insert(cars);
        carService = new CarService(repository);
    }

    private Car createTestCar(ArrayList<String> enginesFuelTypes, int i) {
        String carid = Integer.toString(i);
        String fuelType = enginesFuelTypes.get(i);

        CarTechnicalInformation carTechnicalInformation = createTestCTI(fuelType);

        ArrayList<Review> reviews = createReviews(carid, 3);

        return Car.builder()
                .id(carid)
                .modelId(carid)
                .name(String.format("car %d", i))
                .className(String.format("klasa %d", i))
                .bodyName(String.format("body %d", i))
                .price(159000.0)
                .carTechnicalInformation(carTechnicalInformation)
                .reviewList(reviews)
                .build();
    }

    private CarTechnicalInformation createTestCTI(String fuelType) {
        Engine engine = EngineFactory.getEngine(fuelType, "Euro 6d ISC-FCM", "4x4",
                "4.5", 190.0, 140.0, "4", 4.0, 1950.0);

        Transmission transmission = Transmission.builder().type("8G-DCT").name("auto").build();

        return CarTechnicalInformation.builder()
                .acceleration(6.9).topSpeed(240.0).doors(4.0).seats(5.0).mass(1620.0)
                .engine(engine).transmission(transmission).build();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Add car to repository.")
    void addCar() {
        Car car = Car.builder().build();
        carService.addCar(car);
        assertEquals(carService.getAllCars().size(), 5);
    }

    @DisplayName("Get car with name")
    @ParameterizedTest(name = "Car name = {0}")
    @ValueSource(strings = {"car 1", "car 2", "car 3"})
    void getCarWithName(String carName) throws CarNotFoundException {
        assertEquals(carService.getCarWithName(carName).size(), 1);
    }


    @DisplayName("Throw carNotFound on wrong car name")
    @ParameterizedTest(name = "carName = {0}")
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
    @ValueSource(strings = {"1", "2", "3"})
    void getCarById(String id) throws CarNotFoundException {
        List<Car> cars = new ArrayList<>();
        cars.add(carService.getCarById(id));
        assertEquals(cars.size(), 1);
    }

    @Test
    @DisplayName("Get all cars")
    void getAllCars() {
        assertEquals(carService.getAllCars().size(), 4);
    }


    @DisplayName("Delete car")
    @ParameterizedTest(name = "Car id to delete {0}")
    @ValueSource(strings = {"1"})
    void deleteCar(String id) throws CarNotFoundException {
        carService.deleteCar(id);
        assertEquals(carService.getAllCars().size(), 3);
    }

    @Test
    @DisplayName("Update rating")
    void updateRating() {
    }

    @Test
    @DisplayName("Calculate average ratings with all zeros")
    void calculateAverageRatingsWithZeros() throws CarNotFoundException {


        ArrayList<Review> reviewlist = new ArrayList<>();
        reviewlist.add(Review.builder().id("0").carId("testcar").userId("Test user").
                performance(0).visualAspect(0).valueForMoney(0).reviewCreation(LocalDateTime.parse("2020-04-10T10:50:00.000")).build());

        Car car = Car.builder().id("testcar").reviewList(reviewlist).build();

        carService.addCar(car);
        carService.calculateAverageRatings(car);

        car = carService.getCarById("testcar");
        assertEquals(car.getRating(), 0.0, 0.0000001);
    }

    @Test
    @DisplayName("Calculate average ratings")
    void calculateAverageRatings() throws CarNotFoundException {

        Car car = carService.getCarById("1");

        assertEquals(car.getRating(), 0, 0.0000001);

        car = carService.calculateAverageRatings(car);


    }
}