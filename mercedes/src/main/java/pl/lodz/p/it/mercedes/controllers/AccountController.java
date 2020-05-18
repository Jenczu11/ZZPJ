package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.p.it.mercedes.dto.AccountDto;
import pl.lodz.p.it.mercedes.exceptions.AccountAlreadyExistsException;
import pl.lodz.p.it.mercedes.exceptions.AccountNotFoundException;
import pl.lodz.p.it.mercedes.model.Account;
import pl.lodz.p.it.mercedes.services.AccountService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Account register(AccountDto accountDto) throws AccountAlreadyExistsException {
        Account account = Account.builder()
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .firstName(accountDto.getFirstName())
                .lastName(accountDto.getLastName())
                .build();
        accountService.addAccount(account);
        log.debug(account.toString());
        return account;
    }
    @GetMapping("/allacounts")
    public List<Account> getAllAccounts() throws AccountNotFoundException {
        return accountService.getAllAccount();
    }
}
