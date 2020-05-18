package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mercedes.exceptions.AccountAlreadyExistsException;
import pl.lodz.p.it.mercedes.exceptions.AccountNotFoundException;
import pl.lodz.p.it.mercedes.model.Account;
import pl.lodz.p.it.mercedes.repositories.AccountRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void addAccount(Account account) throws AccountAlreadyExistsException {
        if (accountRepository.findByUsername(account.getUsername()).isEmpty()) {
            accountRepository.insert(account);
        } else {
            throw new AccountAlreadyExistsException("Account already exists.");
        }
    }

    public Account getAccount(String username) throws AccountNotFoundException {
        if (accountRepository.findByUsername(username).isPresent()) {
            return accountRepository.findByUsername(username).get();
        } else {
            throw new AccountNotFoundException("Account not found.");
        }
    }

    public List<Account> getAllAccount() throws AccountNotFoundException {
         return accountRepository.findAll();
    }
}
