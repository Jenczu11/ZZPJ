package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mercedes.dto.ReviewDto;
import pl.lodz.p.it.mercedes.exceptions.CarNotFoundException;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.services.CarService;
import pl.lodz.p.it.mercedes.services.ReviewService;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final CarService carService;

    @PostMapping("/review")
    public Review addReview(@RequestBody ReviewDto reviewDto) throws CarNotFoundException {
        Review review = Review.builder()
                .carId(reviewDto.getCarId())
                .userId(reviewDto.getUserId())
                .valueForMoney(reviewDto.getValueForMoney())
                .performance(reviewDto.getPerformance())
                .visualAspect(reviewDto.getVisualAspect())
                .reviewCreation(reviewDto.getReviewCreation())
                .build();
        if (reviewService.checkReviewForDate(review)) {
            if (reviewService.checkIfReviewForCarNotExists(review)) {
                reviewService.addReview(review);
                carService.addReviewToCar(review.getCarId(), review);
                carService.calculateAverageRatings(review.getCarId());
            } else {
                reviewService.addReview(review);
                carService.updateReviewInCar(review.getCarId(), review);
                carService.calculateAverageRatings(review.getCarId());
            }
        }
        return review;
    }

    @GetMapping("/review/all")
    @ResponseBody
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/review/{id}")
    @ResponseBody
    public Review getReviewById(@PathVariable String id) throws Exception {
        return reviewService.getReviewById(id);
    }

    @DeleteMapping("/review/{id}")
    @ResponseBody
    public Review deleteReviewById(@PathVariable String id) throws Exception {
        return reviewService.deleteReviewById(id);
    }

    @GetMapping("/review/car/{carId}")
    @ResponseBody
    public List<Review> getReviewByCarId(@PathVariable String carId) throws Exception {
        return reviewService.getReviewByCarId(carId);
    }

    @GetMapping("/review/user/{userId}")
    @ResponseBody
    public List<Review> getReviewByUserId(@PathVariable String userId) throws Exception {
        return reviewService.getReviewByUserId(userId);
    }
}
