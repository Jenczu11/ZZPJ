package pl.lodz.p.it.mercedes.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.mercedes.TestMongoConfiguration;
import pl.lodz.p.it.mercedes.TestSuiteExtension;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;
import pl.lodz.p.it.mercedes.services.ReviewService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMongoConfiguration.class)
@ExtendWith(TestSuiteExtension.class)

public class ReviewServiceTest {
    @Autowired
    private ReviewRepository repository;
    private ReviewService reviewService;

    @BeforeEach
    void prepare() {
        List<Review> reviews = new ArrayList<>();

        reviews.add(Review.builder().id("1").carId("car1").performance(1).userId("Micheal").visualAspect(2).valueForMoney(1).reviewCreation(LocalDateTime.parse("2020-04-20T10:50:00.000")).build());
        reviews.add(Review.builder().carId("car2").performance(2).userId("Stephen").visualAspect(4).valueForMoney(3).reviewCreation(LocalDateTime.parse("2020-04-25T10:50:00.000")).build());
        reviews.add(Review.builder().carId("car3").performance(3).userId("Lucas").visualAspect(5).valueForMoney(5).reviewCreation(LocalDateTime.parse("2020-04-30T10:50:00.000")).build());

        repository.insert(reviews);
        reviewService = new ReviewService(repository);
    }

    @AfterEach
    void clean() {
        repository.deleteAll();
    }
    @Test
    @DisplayName("Review builder should return UUID when build default")
    void reviewBuilderWithUUID() {
        Review review = Review.builder().build();
        System.out.println(review.getId());
        assertNotNull(review.getId());
    }

    @Test
    @DisplayName("Review builder should return Id when build with that id")
    void reviewBuilderWithId() {
        String id = "1";
        Review review = Review.builder().id("1").build();
        System.out.println(review.getId());
        assertEquals(review.getId(),id);
    }

    @Test
    @DisplayName("Get all reviews.")
    public void getAllReviews() {
        List<Review> reviewsList = reviewService.getAllReviews();

        assertThat(reviewsList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Add one review for diffrent car.")
    public void addReview() {
        Review review = Review.builder().carId("car4").performance(2).userId("Alvaro").visualAspect(3).valueForMoney(5).reviewCreation(LocalDateTime.parse("2020-04-30T10:50:00.000")).build();

        reviewService.addReview(review);

        assertThat(reviewService.getAllReviews().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Get all reviews with car id.")
    public void getReviewByCarId() throws Exception {

        List<Review> reviewsListWithCarId = new ArrayList<>();
        String carId = "car2";
        reviewsListWithCarId.add(Review.builder().carId(carId).performance(2).userId("Alvaro").visualAspect(3).valueForMoney(4).build());
        reviewsListWithCarId.add(Review.builder().carId(carId).performance(3).userId("Anna").visualAspect(1).valueForMoney(5).build());
        repository.insert(reviewsListWithCarId);

        List<Review> returnedReviewsList = new ArrayList<>();
        returnedReviewsList = reviewService.getReviewByCarId(carId);

        assertThat(returnedReviewsList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Get all reviews with user id.")
    public void getReviewByUserId() throws Exception {

        List<Review> reviewsListWithUserId = new ArrayList<>();
        String userId = "Lucas";
        reviewsListWithUserId.add(Review.builder().carId("car5").performance(1).userId(userId).visualAspect(5).valueForMoney(2).build());
        repository.insert(reviewsListWithUserId);

        List<Review> returnedReviewsList = new ArrayList<>();
        returnedReviewsList = reviewService.getReviewByUserId(userId);

        assertThat(returnedReviewsList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Delete review with given id.")
    public void deleteReviewById() throws Exception {

        String reviewId = "1";
        List<Review> returnedReviewsList = new ArrayList<>();
        reviewService.deleteReviewById(reviewId);
        returnedReviewsList = reviewService.getAllReviews();

        assertThat(returnedReviewsList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("One review for car every 24 hours")
    public void checkReviewForDate() {

        Review review1 = Review.builder().carId("carid1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-29T10:50:00.000")).build();
        Review review2 = Review.builder().carId("carid1").userId("userid1").reviewCreation(LocalDateTime.parse("2020-05-29T10:50:01.000")).build();

        reviewService.addReview(review1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.checkReviewForDate(review2);
        });
        String expectedMessage = "You can add review for this car once every 24h";
        String actualMessage = exception.getMessage();

//        var date1 = review1.getReviewCreation();
//        var date2 = review2.getReviewCreation();
//        System.out.println(date2.minusDays(1));
//        System.out.println(date1);
//        System.out.println(date2.minusDays(1).isAfter(date1));
        System.out.println("Exception message: " + actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }



}