package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mercedes.exceptions.AccountNotFoundException;
import pl.lodz.p.it.mercedes.exceptions.CarNotFoundException;
import pl.lodz.p.it.mercedes.model.Account;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.repositories.CarRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    public void addCar(Car car) {
        carRepository.insert(car);
    }

    public Car getCar(String modelName) throws CarNotFoundException {
        if (carRepository.findAllByModelName(modelName).isPresent()) {
            return carRepository.findAllByModelName(modelName).get();
        } else {
            throw new CarNotFoundException("Car not found.");
        }
    }

    public Car getCarById(String id) throws CarNotFoundException {
        if (carRepository.findById(id).isPresent()) {
            return carRepository.findById(id).get();
        } else {
            throw new CarNotFoundException("Car not found.");
        }
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}
