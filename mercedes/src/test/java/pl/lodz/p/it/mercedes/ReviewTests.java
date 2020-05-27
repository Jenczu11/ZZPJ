package pl.lodz.p.it.mercedes;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;
import pl.lodz.p.it.mercedes.services.ReviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMongoConfiguration.class)
@ExtendWith(TestSuiteExtension.class)

public class ReviewTests {
    private ReviewRepository repository;
    private ReviewService reviewService;
    private List<Review> reviews;

    @BeforeEach
    void prepare() {
        reviews = new ArrayList<>();
        repository = mock(ReviewRepository.class);

        reviews.add(Review.builder().carId("car1").performance(1).userId("Micheal").visualAspect(2).valueForMoney(1).build());
        reviews.add(Review.builder().carId("car2").performance(2).userId("Stephen").visualAspect(4).valueForMoney(3).build());
        reviews.add(Review.builder().carId("car3").performance(3).userId("Lucas").visualAspect(5).valueForMoney(5).build());

        reviewService = new ReviewService(repository);
    }

    @AfterEach
    void clean() {
        reviews = new ArrayList<>();
    }

    @Test
    public void getAllReviewsTest() {
        when(repository.findAll()).thenReturn(reviews);
        List<Review> reviewsList = reviewService.getAllReviews();

        assertThat(reviewsList.size()).isEqualTo(3);
    }

    @Test
    public void addReviewTest() {
        when(repository.findAll()).thenReturn(reviews);
        Review review = Review.builder().carId("car2").performance(2).userId("Stephen").visualAspect(4).valueForMoney(3).build();
        reviewService.addReview(review);

        List<Review> reviewsList = reviewService.getAllReviews();

        assertThat(reviewsList.size()).isEqualTo(4);
    }

    @Test
    public void getReviewByCarId() throws Exception{

        List<Review> reviewsList = new ArrayList<>();
        String carId = "car1";

        Optional<List<Review>> returnCacheValue = Optional.of(reviews);
        when(repository.findByCarId(carId)).thenReturn(returnCacheValue);

        reviewsList = reviewService.getReviewByCarId(carId);

        assertThat(reviewsList.size()).isEqualTo(1);
    }
}
