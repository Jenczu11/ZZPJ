package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarTechnicalInformation {
    private Double acceleration;
    private Double topSpeed;
    private Integer doors;
    private Integer seats;
    private Double mass;
    private Engine engine;
    private Transmission transmission;
}
