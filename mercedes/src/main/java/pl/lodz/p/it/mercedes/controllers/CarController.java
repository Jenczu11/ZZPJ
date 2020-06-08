package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import pl.lodz.p.it.mercedes.exceptions.CarNotFoundException;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.services.CarService;

import java.util.List;

@RestController
@AllArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("/car")
    public List<Car> getCar(@RequestParam String name) throws CarNotFoundException {
        return carService.getCarWithName(name);
    }

    @GetMapping("/car/{id}")
    @ResponseBody
    public Car getCarById(@PathVariable String id) throws CarNotFoundException {
        return carService.getCarById(id);
    }

    @DeleteMapping("/car/{id}")
    @ResponseBody
    public Car deleteCar(@PathVariable String id) throws CarNotFoundException {
        return carService.deleteCar(id);
    }

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/top10")
    public List<Car> getTop10_OverallRating() {
        return carService.getTop10_OverallRating();
    }

    @GetMapping("/cars/top10/visual")
    public List<Car> getTop10_VisualAspect() {
        return carService.getTop10_VisualAspect();
    }

    @GetMapping("/cars/top10/value")
    public List<Car> getTop10_ValueForMoney() {
        return carService.getTop10_ValueForMoney();
    }

    @GetMapping("/cars/top10/performance")
    public List<Car> getTop10_Performance() {
        return carService.getTop10_Performance();
    }

}
