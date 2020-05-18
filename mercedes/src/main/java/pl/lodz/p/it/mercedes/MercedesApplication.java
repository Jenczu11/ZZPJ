package pl.lodz.p.it.mercedes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.lodz.p.it.mercedes.model.Account;
import pl.lodz.p.it.mercedes.repositories.AccountRepository;

@SpringBootApplication
public class MercedesApplication  {
	public static void main(String[] args) {
		SpringApplication.run(MercedesApplication.class, args);
	}
}
