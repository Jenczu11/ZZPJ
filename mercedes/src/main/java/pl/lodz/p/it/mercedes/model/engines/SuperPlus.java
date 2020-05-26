package pl.lodz.p.it.mercedes.model.engines;

import lombok.Builder;
import lombok.Data;
import pl.lodz.p.it.mercedes.model.Engine;

@Data
@Builder
public class Super extends Engine {
    private Double powerHp;
    private Double powerKw;
    private String cylinder;
    private Double cylinderValves;
    private Double capacity;
}