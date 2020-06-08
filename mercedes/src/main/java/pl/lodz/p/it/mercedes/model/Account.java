package pl.lodz.p.it.mercedes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "accounts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public @Data
class Account {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}