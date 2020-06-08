package pl.lodz.p.it.mercedes.model.mappers;

import pl.lodz.p.it.mercedes.dto.AccountDto;
import pl.lodz.p.it.mercedes.model.Account;

public class AccountMapper {
    @SuppressWarnings("unused")
    public static AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                .username(account.getUsername())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .build();
    }

    public static Account mapFromDto(AccountDto accountDto) {
        return Account.builder()
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .firstName(accountDto.getFirstName())
                .lastName(accountDto.getLastName())
                .build();
    }
}
