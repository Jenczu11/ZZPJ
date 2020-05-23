package pl.lodz.p.it.mercedes.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transmission {
    public String name;
    public String type;
}
