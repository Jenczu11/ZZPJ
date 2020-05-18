package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mercedes.model.Account;
import pl.lodz.p.it.mercedes.repositories.AccountRepository;

import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (accountRepository.findByUsername(username).isPresent()) {
            Account account = accountRepository.findByUsername(username).get();
            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
            return new User(account.getUsername(), account.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
