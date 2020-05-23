package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.services.MercedesAPIService;

@RestController
@Slf4j
@AllArgsConstructor
public class MercedesAPIController {


    private final MercedesAPIService mercedesAPIService;
    @GetMapping("/mercedes/{id}")
    public Car getModel(@PathVariable int id) {
       return mercedesAPIService.getCar(id);
    }
    @PostMapping("/mercedes/{id}/save")
    public Car saveCarinDB(@PathVariable int id) {
        return mercedesAPIService.saveCar(id);
    }
}
