package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import pl.lodz.p.it.mercedes.dto.CarDto;
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
}
