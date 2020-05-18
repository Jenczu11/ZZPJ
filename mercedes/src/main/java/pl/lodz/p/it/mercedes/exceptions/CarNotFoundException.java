package pl.lodz.p.it.mercedes.exceptions;

public class CarNotFoundException extends Exception {

    public CarNotFoundException() {
        super();
    }

    public CarNotFoundException(String message) {
        super(message);
    }
}
