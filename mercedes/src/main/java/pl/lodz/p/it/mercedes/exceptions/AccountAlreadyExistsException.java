package pl.lodz.p.it.mercedes.exceptions;

public class AccountAlreadyExistsException extends Exception {

    public AccountAlreadyExistsException() {
        super();
    }

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
