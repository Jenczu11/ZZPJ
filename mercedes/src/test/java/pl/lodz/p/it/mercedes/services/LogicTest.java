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

/*
Tutaj powinny znaleźć się testy do logiki biznesowej czyli:
    * Jeden użytkownik może wysłać tylko jedną ocenę do jednego samochodu.
    * Użytkownik może zmienić ocenę dla danego samochodu najwcześniej po 24h od poprzedniej oceny.
 */
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
                Arrays.asList("Diesel", "Super", "SuperPlus", "Electric")
        );
        IntStream.range(0, enginesFuelTypes.size()).forEach(i -> {
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
                    .name(String.format("car %d", i))
                    .className(String.format("klasa %d", i))
                    .bodyName(String.format("body %d", i))
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
    @DisplayName("(MethodException) User can change the rating for a given car after 24h from the previous rating")
    public void GivenReviewWithWrongDate_ThrowException() {

        Review review1 = Review.builder().carId("carid1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-29T10:50:00.000")).build();
        Review review2 = Review.builder().carId("carid1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-29T10:50:01.000")).build();

        reviewService.addReview(review1);
        assertEquals(reviewService.getReviewByCarId("carid1").size(), 1);
//        Tested method.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.checkReviewForDate(review2);
        });
        String expectedMessage = "You can add review for this car once every 24h";
        String actualMessage = exception.getMessage();

        System.out.println("Exception message: " + actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @DisplayName("(Logic) User can change the rating for a given car after 24h from the previous rating" +
            " and One user can send only one rating to this one car")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    public void AddReviewAfter24h(String carId) throws Exception {

        String userId = "userid1";
        String firstReviewId = "1";
        String secondReviewId = "2";

        Review.ReviewBuilder builder = Review.builder();
        builder.id(firstReviewId);
        builder.carId(carId);
        builder.userId(userId);
        builder.performance(1);
        builder.valueForMoney(2);
        builder.visualAspect(3);
        builder.reviewCreation(LocalDateTime.parse("2020-05-29T10:50:00.000"));
        Review review1 = builder.build();

        builder.id(secondReviewId);
        builder.carId(carId);
        builder.userId(userId);
        builder.performance(5);
        builder.valueForMoney(5);
        builder.visualAspect(5);
        builder.reviewCreation(LocalDateTime.parse("2020-05-30T10:50:01.000"));
        Review review2 = builder.build();

        reviewService.addReview(review1);
        carService.addReviewToCar(review1.getCarId(), review1);
        carService.calculateAverageRatings(review1.getCarId());


        var ReviewList = carService.getCarById(carId).getReviewList();
        assertEquals(ReviewList.size(), 1);
        assertEquals(ReviewList.get(0).getOverallRating(), 2, 0.1);
        assertEquals(ReviewList.get(0).getPerformance(), 1);
        assertEquals(ReviewList.get(0).getValueForMoney(), 2);
        assertEquals(ReviewList.get(0).getVisualAspect(), 3);

        assertEquals(reviewService.getReviewByCarId(carId).size(), 1);

        if(reviewService.checkReviewForDate(review2)){
            if(!reviewService.checkIfReviewForCarNotExists(review2)) {
                reviewService.addReview(review2);
                carService.updateReviewInCar(review2.getCarId(), review2);
                carService.calculateAverageRatings(review2.getCarId());
            }
        }
        ReviewList = carService.getCarById(carId).getReviewList();

        assertEquals(ReviewList.size(), 1);
        assertEquals(ReviewList.get(0).getOverallRating(), 5, 0.1);
        assertEquals(ReviewList.get(0).getPerformance(), 5);
        assertEquals(ReviewList.get(0).getValueForMoney(), 5);
        assertEquals(ReviewList.get(0).getVisualAspect(), 5);

        assertEquals(reviewService.getReviewByCarId(carId).size(), 2);
        assertEquals(reviewService.getReviewByUserId(userId).size(), 2);

    }


//    @DisplayName("One user can send only one rating to this one car")
//    @ParameterizedTest
//    @ValueSource(strings = {"1"})
//    void checkIfReviewForCarExists(String carId) throws Exception {
//
//        String userId = "userid1";
//        String firstReviewId = "1";
//        String secondReviewId = "2";
////      Build first review
//        Review.ReviewBuilder builder = Review.builder();
//        builder.id(firstReviewId);
//        builder.carId(carId);
//        builder.userId(userId);
//        builder.performance(1);
//        builder.valueForMoney(2);
//        builder.visualAspect(3);
//        builder.reviewCreation(LocalDateTime.parse("2020-05-29T10:50:00.000"));
//        Review review1 = builder.build();
////      Build second review
//        builder.id(secondReviewId);
//        builder.carId(carId);
//        builder.userId(userId);
//        builder.performance(5);
//        builder.valueForMoney(5);
//        builder.visualAspect(5);
//        builder.reviewCreation(LocalDateTime.parse("2020-05-30T10:50:01.000"));
//        Review review2 = builder.build();
//
//// Add first review (like in Review controller)
//        reviewService.addReview(review1);
//        carService.addReviewToCar(review1.getCarId(), review1);
//        carService.calculateAverageRatings(review1.getCarId());
////        Check if car has only first review
//        assertEquals(carService.getCarById(carId).getReviewList().size(), 1);
////        Check if db holds only first review
//        assertEquals(reviewService.getReviewByCarId(carId).size(), 1);
//
//// Add second review (like in Review controller)
//        if(reviewService.checkReviewForDate(review2)){
//            if(!reviewService.checkIfReviewForCarNotExists(review2)) {
//                reviewService.addReview(review2);
//                carService.updateReviewInCar(review2.getCarId(), review2);
//                carService.calculateAverageRatings(review2.getCarId());
//            }
//        }
////        Check if car has only second review data
//        assertEquals(carService.getCarById(carId).getReviewList().size(), 1);
//
////    Check if db holds both reviews for this carID
//        assertEquals(reviewService.getReviewByCarId(carId).size(), 2);
////    Check if db holds both reviews for this userID
//        assertEquals(reviewService.getReviewByUserId(userId).size(), 2);
//    }

}