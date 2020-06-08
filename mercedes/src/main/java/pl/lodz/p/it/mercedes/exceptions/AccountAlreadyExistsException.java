package pl.lodz.p.it.mercedes.exceptions;

@SuppressWarnings("ALL")
public class AccountAlreadyExistsException extends Exception {

    public AccountAlreadyExistsException() {
        super();
    }

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
