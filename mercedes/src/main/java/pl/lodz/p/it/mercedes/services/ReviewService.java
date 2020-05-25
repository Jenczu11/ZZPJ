package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public void addReview(Review review) {
        reviewRepository.insert(review);
    }
    public Review getReviewById(String id) throws Exception {
        if (reviewRepository.findById(id).isPresent()) {
            return reviewRepository.findById(id).get();
        } else {
            throw new Exception("Review not found.");
        }
    }
    public List<Review> getReviewByCarId(String carId) throws Exception {
        if (reviewRepository.findByCarId(carId).isPresent()) {
            return reviewRepository.findByCarId(carId).get();
        } else {
            throw new Exception("No review for car found");
        }
    }
    public List<Review> getReviewByUserId(String userId) throws Exception {
        if (reviewRepository.findAllByUserId(userId).isPresent()) {
            return reviewRepository.findAllByUserId(userId).get();
        } else {
            throw new Exception("No Review for user found");
        }
    }
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}
