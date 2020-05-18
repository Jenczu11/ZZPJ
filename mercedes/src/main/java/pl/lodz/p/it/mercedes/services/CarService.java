package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.repositories.CarRepository;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    public void addCar(Car car) {
        carRepository.insert(car);
    }

    public Car getCar(String modelName) {
        return carRepository.findByModelName(modelName).get();
    }
}
