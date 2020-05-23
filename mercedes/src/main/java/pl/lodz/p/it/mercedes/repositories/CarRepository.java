package pl.lodz.p.it.mercedes.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.mercedes.model.Car;

import java.util.Optional;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    Optional<Car> findAllByName(String name);
}
