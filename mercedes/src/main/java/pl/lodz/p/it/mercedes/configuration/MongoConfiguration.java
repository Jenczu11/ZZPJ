package pl.lodz.p.it.mercedes.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Objects;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    private final Environment env;

    @Autowired
    public MongoConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    protected String getDatabaseName() {
        return Objects.requireNonNull(env.getProperty("DATABASENAME"));
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(Objects.requireNonNull(env.getProperty("CONNECTIONSTRING")));
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
