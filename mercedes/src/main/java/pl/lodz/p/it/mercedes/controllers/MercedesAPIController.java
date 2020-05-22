package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.mercedes.services.MercedesAPIService;

@RestController
@Slf4j
@AllArgsConstructor
public class MercedesAPIController {


    private final MercedesAPIService mercedesAPIService;
    @GetMapping("/mercedes")
    public String getModel() {
       return mercedesAPIService.getModelsForMarket();
    }
}
