package topg.User_Account.exceptions;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message); // Pass the message to the RuntimeException constructor
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause); // Allows chaining with another throwable
    }
}
