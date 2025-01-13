package topg.loan_mangement.exceptions;

public class LenderNotFoundException extends RuntimeException{
    public LenderNotFoundException(String message) {
        super(message);
    }
}
