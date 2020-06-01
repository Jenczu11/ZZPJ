package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.services.MercedesAPIService;

@RestController
@Slf4j
@AllArgsConstructor
public class MercedesAPIController {

    private final MercedesAPIService mercedesAPIService;

    @GetMapping("/mercedes/{id}")
    public Car getModel(@PathVariable int id, @RequestParam(defaultValue = "null") String apikey) {
        return mercedesAPIService.getCar(id, apikey);
    }

    @PostMapping("/mercedes/{id}/save")
    public Car saveCarinDB(@PathVariable int id, @RequestParam(defaultValue = "null") String apikey) {
        return mercedesAPIService.saveCar(id, apikey);
    }

    @PostMapping("mercedes/populateDB")
    public String populateDB(@RequestParam int start, @RequestParam int end, @RequestParam(defaultValue = "null") String apikey) {
        return mercedesAPIService.populateDB(start, end, apikey);
    }
}
