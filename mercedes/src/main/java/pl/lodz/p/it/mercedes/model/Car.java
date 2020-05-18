package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Document(collection = "Cars")
@Builder
public @Data class Car {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String modelName;
    private String engine;

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", modelName='" + modelName + '\'' +
                ", engine='" + engine + '\'' +
                '}';
    }
}