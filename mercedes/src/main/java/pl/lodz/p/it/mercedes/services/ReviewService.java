package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mercedes.exceptions.ReviewForCarNotFoundException;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.ReviewRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public void addReview(Review review) {
        reviewRepository.insert(review);
    }

    public boolean checkReviewForDate(Review reviewToCheck) {
//        Get all reviews for car
        List<Review> carReviews = this.getReviewByCarId(reviewToCheck.getCarId());
        AtomicBoolean isCorrect = new AtomicBoolean(true);
//        If zero reviews for car return true, review can be added.
        if (carReviews.size() == 0) isCorrect.set(true);
        carReviews.forEach(review -> {
            if (review.getUserId().equals(reviewToCheck.getUserId())) {
                var oldReviewDate = review.getReviewCreation();
                var newReviewDate = reviewToCheck.getReviewCreation();
                if (newReviewDate.minusDays(1).isAfter(oldReviewDate)) {
//                    You can add review it is past 24h
                    isCorrect.set(true);
                } else {
//                    You can't add review.
                    isCorrect.set(false);
                    throw new RuntimeException("You can add review for this car once every 24h");
                }
            }
        });
        return isCorrect.get();
    }

    public boolean checkIfReviewForCarNotExists(Review reviewToCheck) {
//        Get all reviews for car
        List<Review> carReviews = this.getReviewByCarId(reviewToCheck.getCarId());
        AtomicBoolean addRecord = new AtomicBoolean(true);
//        If zero reviews for car return true, review can be added.
        if (carReviews.size() == 0) addRecord.set(true);
        carReviews.forEach(review -> {
            if (review.getUserId().equals(reviewToCheck.getUserId())) {
                var oldReviewCarId = review.getCarId();
                var newReviewCarId = reviewToCheck.getCarId();
                //                    If exists update record
                //                    If not exists simply add record
                addRecord.set(!oldReviewCarId.equals(newReviewCarId));
            }
        });
        return addRecord.get();
    }

    public Review getReviewById(String id) throws Exception {
        if (reviewRepository.findById(id).isPresent()) {
            return reviewRepository.findById(id).get();
        } else {
            throw new Exception("Review not found.");
        }
    }

    public List<Review> getReviewByCarId(String carId) throws ReviewForCarNotFoundException {
        if (reviewRepository.findByCarId(carId).isPresent()) {
            return reviewRepository.findByCarId(carId).get();
        } else {
            throw new ReviewForCarNotFoundException("No review for car found");
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

    public Review deleteReviewById(String id) throws Exception {
        if (reviewRepository.findById(id).isPresent()) {
            Review reviewToDelete = reviewRepository.findById(id).get();
            reviewRepository.delete(reviewToDelete);
            return reviewToDelete;
        } else {
            throw new Exception("Review to delete not found.");
        }
    }
}
