package pl.lodz.p.it.mercedes;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;
import pl.lodz.p.it.mercedes.services.ReviewService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMongoConfiguration.class)
@ExtendWith(TestSuiteExtension.class)

public class ReviewTests {
    @Autowired
    private ReviewRepository repository;
    private ReviewService reviewService;
    private List<Review> reviews;

    @BeforeEach
    void prepare() {
        reviews = new ArrayList<>();
//        repository = mock(ReviewRepository.class);

        reviews.add(Review.builder().id("1").carId("car1").performance(1).userId("Micheal").visualAspect(2).valueForMoney(1).build());
        reviews.add(Review.builder().carId("car2").performance(2).userId("Stephen").visualAspect(4).valueForMoney(3).build());
        reviews.add(Review.builder().carId("car3").performance(3).userId("Lucas").visualAspect(5).valueForMoney(5).build());

        repository.insert(reviews);
        reviewService = new ReviewService(repository);
    }

    @AfterEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Get all reviews.")
    public void getAllReviews() {
        List<Review> reviewsList = reviewService.getAllReviews();

        assertThat(reviewsList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Add one review.")
    public void addReview() {
        Review review = Review.builder().carId("car4").performance(2).userId("Alvaro").visualAspect(3).valueForMoney(5).build();

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
}
