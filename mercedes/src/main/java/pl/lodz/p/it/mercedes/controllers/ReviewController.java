package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mercedes.dto.ReviewDto;
import pl.lodz.p.it.mercedes.exceptions.CarNotFoundException;
import pl.lodz.p.it.mercedes.model.Car;
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
        Double overallRating = ((reviewDto.getValueForMoney()+reviewDto.getPerformance()+reviewDto.getVisualAspect()) / 3.0);
        Review review = Review.builder()
                .carId(reviewDto.getCarId())
                .userId(reviewDto.getUserId())
                .valueForMoney(reviewDto.getValueForMoney())
                .performance(reviewDto.getPerformance())
                .visualAspect(reviewDto.getVisualAspect())
                .overallRating(overallRating)
                .build();
        reviewService.addReview(review);
        carService.updateRating(reviewDto.getCarId(),overallRating);
        return review;
    }

    @GetMapping("/review/all")
    @ResponseBody
    public List<Review> getAllReviews () {
        return reviewService.getAllReviews();
    }
    @GetMapping("/review/{id}")
    @ResponseBody
    public Review getReviewById (@PathVariable String id) throws Exception {
        return reviewService.getReviewById(id);
    }
    @GetMapping("/review/car/{carId}")
    @ResponseBody
    public List<Review> getReviewByCarId (@PathVariable String carId) throws Exception {
        return reviewService.getReviewByCarId(carId);
    }
    @GetMapping("/review/user/{userId}")
    @ResponseBody
    public List<Review> getReviewByUserId (@PathVariable String userId) throws Exception {
        return reviewService.getReviewByUserId(userId);
    }
}
