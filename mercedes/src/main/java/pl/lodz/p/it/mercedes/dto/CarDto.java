package pl.lodz.p.it.mercedes.dto;

import lombok.Builder;
import lombok.Data;
import pl.lodz.p.it.mercedes.model.CarTechnicalInformation;
import pl.lodz.p.it.mercedes.model.Review;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Builder
public @Data class CarDto {

    private String modelId;
    private String name;
    private String className;
    private String bodyName;
    private Double price;
    private CarTechnicalInformation carTechnicalInformation;
    private Map<String,String> imagesUrls;
    private ArrayList<Review> reviewList;
    private Double rating;
    private Integer numberOfRatings;

}
