package pl.lodz.p.it.mercedes.services;

import com.jayway.jsonpath.internal.function.numeric.Average;
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
class Top10Test {
    @Autowired
    private CarRepository repository;
    private CarService carService;


    @BeforeEach
    void setUp() {
        List<Car> cars = new ArrayList<>();

        AddCarsWithWorseRatings(cars);
        AddCarWithTopRatings(cars);

        repository.insert(cars);
        carService = new CarService(repository);
    }

    private void AddCarsWithWorseRatings(List<Car> cars) {
        IntStream.range(0, 8).forEach(i -> {
            String id = Integer.toString(i);
            var performanceAverage = ThreadLocalRandom.current().nextDouble(0, 2 + 1);
            var visualAspectAverage = ThreadLocalRandom.current().nextDouble(0, 2 + 1);
            var valueForMoneyAverage = ThreadLocalRandom.current().nextDouble(0, 2 + 1);
            var rating = (performanceAverage + visualAspectAverage + valueForMoneyAverage) / 3.0;
            Car car = Car.builder()
                    .id(id)
                    .modelId(id)
                    .name(String.format("car %d", i))
                    .className(String.format("klasa %d", i))
                    .bodyName(String.format("body %d", i))
                    .performanceAverage(performanceAverage)
                    .valueForMoneyAverage(visualAspectAverage)
                    .visualAspectAverage(valueForMoneyAverage)
                    .rating(rating)
                    .numberOfRatings(1)
                    .build();
            cars.add(car);
        });
    }

    private void AddCarWithTopRatings(List<Car> cars) {
        IntStream.range(1, 4).forEach(i -> {
            var rating = (double) 6 - i;
            Car car = Car.builder()
                    .id(String.format("topcar%d", i))
                    .modelId(String.format("topcar%d", i))
                    .name(String.format("topcar%d", i))
                    .performanceAverage(rating)
                    .valueForMoneyAverage(rating)
                    .visualAspectAverage(rating)
                    .rating(rating)
                    .numberOfRatings(1)
                    .build();
            cars.add(car);
        });
    }

    private void printCarList(List<Car> cars) {
        System.out.println("\n------------------------------");
        cars.forEach(car -> {
            System.out.println(car.toString());
        });
        System.out.println("------------------------------");
    }

    private void checkTopOfCarList(List<Car> cars) {
        System.out.println("Top cars id :");
        System.out.println(cars.get(0).getId());
        System.out.println(cars.get(1).getId());
        System.out.println(cars.get(2).getId());
        assertEquals(cars.get(0).getId(), "topcar1");
        assertEquals(cars.get(1).getId(), "topcar2");
        assertEquals(cars.get(2).getId(), "topcar3");
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }


    @Test
    @DisplayName("Check cars sorted by average overall rating")
    void getTop10_OverallRating() {
        List<Car> cars = carService.getTop10_OverallRating();
        printCarList(cars);
        checkTopOfCarList(cars);
    }


    @Test
    @DisplayName("Check cars sorted by average visual rating")
    void getTop10_VisualAspect() {
        List<Car> cars = carService.getTop10_VisualAspect();

        printCarList(cars);

        checkTopOfCarList(cars);
    }


    @Test
    @DisplayName("Check cars sorted by average value for money rating")
    void getTop10_ValueForMoney() {
        List<Car> cars = carService.getTop10_ValueForMoney();

        printCarList(cars);

        checkTopOfCarList(cars);
    }

    @Test
    @DisplayName("Check cars sorted by average performance rating")
    void getTop10_Performance() {
        List<Car> cars = carService.getTop10_Performance();

        printCarList(cars);

        checkTopOfCarList(cars);
    }
}