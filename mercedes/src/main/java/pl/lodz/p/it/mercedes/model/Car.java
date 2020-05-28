package pl.lodz.p.it.mercedes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "Cars")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public @Data class Car {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String modelId;
    private String name;
    private String className;
    private String bodyName;
    private Double price;
    private CarTechnicalInformation carTechnicalInformation;
    private Map<String,String> imagesUrls;
    @Builder.Default
    private ArrayList<Review> reviewList = new ArrayList<>();
    @Builder.Default
    private Double rating = 0.0;
    @Builder.Default
    private Integer numberOfRatings = 0;

}