package pl.lodz.p.it.mercedes.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.lodz.p.it.mercedes.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<List<Review>> findByCarId(String carId);
    Optional<List<Review>> findAllByUserId(String userId);
}
