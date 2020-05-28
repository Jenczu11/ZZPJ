package pl.lodz.p.it.mercedes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("reviews")
public class Review {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String carId;
    private String userId;
    private Integer valueForMoney;
    private Integer performance;
    private Integer visualAspect;
    private Double overallRating;
}
