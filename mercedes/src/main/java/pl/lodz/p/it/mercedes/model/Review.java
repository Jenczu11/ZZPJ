package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document("reviews")
public class Review {
    private String id = UUID.randomUUID().toString();
    private String carId;
    private String userId;
    private Integer valueForMoney;
    private Integer performance;
    private Integer visualAspect;
    private Double overallRating;
}
