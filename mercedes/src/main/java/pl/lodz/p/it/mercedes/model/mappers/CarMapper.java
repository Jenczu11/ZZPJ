package pl.lodz.p.it.mercedes.model.mappers;

import pl.lodz.p.it.mercedes.dto.CarDto;
import pl.lodz.p.it.mercedes.model.Car;

@SuppressWarnings("unused")
public class CarMapper {
    public static CarDto mapToDto(Car car) {
        return CarDto.builder().build();
    }

    public static Car mapFromDto(CarDto carDto) {
        return Car.builder().build();
    }
}
