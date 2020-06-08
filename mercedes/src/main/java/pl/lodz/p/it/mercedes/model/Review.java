package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@Document("reviews")
public class Review {

    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String carId;
    private String userId;
    @Builder.Default
    private Integer valueForMoney = 0;
    @Builder.Default
    private Integer performance = 0;
    @Builder.Default
    private Integer visualAspect = 0;
    private Double overallRating;
    private LocalDateTime reviewCreation;
    @SuppressWarnings("unused")
    public Review(String id, String carId, String userId, Integer valueForMoney, Integer performance, Integer visualAspect, Double overallRating, LocalDateTime reviewCreation) {
        this.id = id;
        this.carId = carId;
        this.userId = userId;
        this.valueForMoney = valueForMoney;
        this.performance = performance;
        this.visualAspect = visualAspect;
        this.reviewCreation = reviewCreation;
        this.overallRating = (valueForMoney + performance + visualAspect) / 3.0;
    }

}
