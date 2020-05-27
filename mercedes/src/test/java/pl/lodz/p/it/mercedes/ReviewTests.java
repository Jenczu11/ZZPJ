package pl.lodz.p.it.mercedes;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;
import pl.lodz.p.it.mercedes.services.ReviewService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMongoConfiguration.class)
@ExtendWith(TestSuiteExtension.class)

public class CarControllerTests {
    private ReviewRepository repository;
    private ReviewService reviewService;
    private List<Review> reviews;

    @BeforeEach
    void prepare() {
        reviews = new ArrayList<>();
        repository = mock(ReviewRepository.class);

        reviews.add(Review.builder().carId("kupa").performance(1).overallRating(1.0).userId("kasdksajdsad").visualAspect(2).valueForMoney(2).build());
        reviews.add(Review.builder().carId("kupa2").performance(1).overallRating(1.5).userId("kasdksajdsad").visualAspect(2).valueForMoney(2).build());

        reviewService = new ReviewService(repository);
    }

    @AfterEach
    void clean() {
        reviews = new ArrayList<>();
    }

    @Test
    public void getAllReviewsTest() {

//        reviews = new ArrayList<>();
//        repository = mock(ReviewRepository.class);
//
//        reviews.add(Review.builder().carId("kupa").performance(1).overallRating(1.0).userId("kasdksajdsad").visualAspect(2).valueForMoney(2).build());
//        reviews.add(Review.builder().carId("kupa2").performance(1).overallRating(1.5).userId("kasdksajdsad").visualAspect(2).valueForMoney(2).build());
//
//        reviewService = new ReviewService(repository);

        when(repository.findAll()).thenReturn(reviews);

        List<Review> reviewsList = reviewService.getAllReviews();

        assertThat(reviewsList.size()).isEqualTo(2);
    }
}
