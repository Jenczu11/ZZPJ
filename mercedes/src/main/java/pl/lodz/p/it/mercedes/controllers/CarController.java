package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import pl.lodz.p.it.mercedes.dto.CarDto;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.services.CarService;

@RestController
@AllArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping("/car")
    public String addCar(@ModelAttribute CarDto carDto) {
        Car car = Car.builder()
                .modelName(carDto.getModelName())
                .engine(carDto.getEngine())
                .build();
        carService.addCar(car);
        return car.toString();
    }
    @GetMapping("/car")
    public String getCar(@RequestParam(name="modelName", required=true, defaultValue="World") String modelName) {
        return carService.getCar(modelName).toString();
    }
}
