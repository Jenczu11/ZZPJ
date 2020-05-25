package pl.lodz.p.it.mercedes.dto;

import lombok.Data;

public @Data
class ReviewDto {
    private String carId;
    private String userId;
    private Integer valueForMoney;
    private Integer performance;
    private Integer visualAspect;
    private Double overallRating;
}
