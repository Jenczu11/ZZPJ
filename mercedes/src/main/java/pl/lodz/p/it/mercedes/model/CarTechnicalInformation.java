package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarTechnicalInformation {
    private Double acceleration;
    private Long topSpeed;
    private Long doors;
    private Long seats;
    private Long mass;
    private Engine engine;
    private Transmission transmission;
}
