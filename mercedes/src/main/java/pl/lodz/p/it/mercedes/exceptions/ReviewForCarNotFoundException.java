package pl.lodz.p.it.mercedes.exceptions;

public class ReviewForCarNotFoundException extends RuntimeException {

    public ReviewForCarNotFoundException() {
        super();
    }

    public ReviewForCarNotFoundException(String message) {
        super(message);
    }
}
