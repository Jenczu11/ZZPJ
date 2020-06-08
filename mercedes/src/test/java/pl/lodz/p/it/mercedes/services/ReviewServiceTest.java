package pl.lodz.p.it.mercedes.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.mercedes.TestMongoConfiguration;
import pl.lodz.p.it.mercedes.TestSuiteExtension;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestMongoConfiguration.class)
@ExtendWith(TestSuiteExtension.class)

public class ReviewServiceTest {
    @Autowired
    private ReviewRepository repository;
    private ReviewService reviewService;

    ArrayList<Review> createReviews(Integer numberOfReviews) {
        ArrayList<Review> reviews = new ArrayList<>();
        IntStream.range(0, numberOfReviews).forEach(ratingindex -> {
            Review.ReviewBuilder builder = Review.builder();
            builder.id(String.valueOf(ratingindex));
            builder.carId(String.format("car%d",ratingindex));
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
    void prepare() {
        List<Review> reviews = createReviews(3);

//        reviews.add(Review.builder().id("1").carId("car1").performance(1).userId("Micheal").visualAspect(2).valueForMoney(1).reviewCreation(LocalDateTime.parse("2020-04-20T10:50:00.000")).build());
//        reviews.add(Review.builder().carId("car2").performance(2).userId("Stephen").visualAspect(4).valueForMoney(3).reviewCreation(LocalDateTime.parse("2020-04-25T10:50:00.000")).build());
//        reviews.add(Review.builder().carId("car3").performance(3).userId("Lucas").visualAspect(5).valueForMoney(5).reviewCreation(LocalDateTime.parse("2020-04-30T10:50:00.000")).build());

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
        assertEquals(review.getId(), id);
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
        reviewService.getReviewByCarId("car4");
        assertThat(reviewService.getAllReviews().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Get all reviews with car id.")
    public void getReviewByCarId() {

        List<Review> reviewsListWithCarId = new ArrayList<>();
        String carId = "car2";
        reviewsListWithCarId.add(Review.builder().carId(carId).performance(2).userId("Alvaro").visualAspect(3).valueForMoney(4).build());
        reviewsListWithCarId.add(Review.builder().carId(carId).performance(3).userId("Anna").visualAspect(1).valueForMoney(5).build());
        repository.insert(reviewsListWithCarId);

        List<Review> returnedReviewsList;
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

        List<Review> returnedReviewsList;
        returnedReviewsList = reviewService.getReviewByUserId(userId);

        assertThat(returnedReviewsList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Delete review with given id.")
    public void deleteReviewById() throws Exception {

        String reviewId = "1";
        List<Review> returnedReviewsList;
        reviewService.deleteReviewById(reviewId);
        returnedReviewsList = reviewService.getAllReviews();

        assertThat(returnedReviewsList.size()).isEqualTo(2);
    }


}