package pl.lodz.p.it.mercedes.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mercedes.dto.AccountDto;
import pl.lodz.p.it.mercedes.exceptions.AccountAlreadyExistsException;
import pl.lodz.p.it.mercedes.model.Account;
import pl.lodz.p.it.mercedes.model.mappers.AccountMapper;
import pl.lodz.p.it.mercedes.services.AccountService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void register(@RequestBody AccountDto accountDto) throws AccountAlreadyExistsException {
        accountService.addAccount(AccountMapper.mapFromDto(accountDto));
    }

    @GetMapping("/allaccounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccount();
    }
}
