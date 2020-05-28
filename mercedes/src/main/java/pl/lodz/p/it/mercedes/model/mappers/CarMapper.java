package pl.lodz.p.it.mercedes.model.mappers;

import org.json.simple.JSONObject;
import pl.lodz.p.it.mercedes.dto.AccountDto;
import pl.lodz.p.it.mercedes.dto.CarDto;
import pl.lodz.p.it.mercedes.model.Account;
import pl.lodz.p.it.mercedes.model.Car;

public class CarMapper {
    public static CarDto mapToDto(Car car) {
        return CarDto.builder().build();
    }

    public static Car mapFromDto(CarDto carDto) {
        return Car.builder().build();
    }
}
