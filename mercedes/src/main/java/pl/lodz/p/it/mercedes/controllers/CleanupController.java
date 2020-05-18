package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.mercedes.repositories.AccountRepository;
import pl.lodz.p.it.mercedes.repositories.CarRepository;

@CrossOrigin
@RestController
@AllArgsConstructor
public class CleanupController {

    private final AccountRepository accountRepository;
    private final CarRepository carRepository;

    @DeleteMapping("/clean")
    public void cleanDatabase() {
        accountRepository.deleteAll();
        carRepository.deleteAll();
    }
}
