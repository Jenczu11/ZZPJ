package pl.lodz.p.it.mercedes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping
    public String index() {
        return "Mercedes i benz";
    }
}
