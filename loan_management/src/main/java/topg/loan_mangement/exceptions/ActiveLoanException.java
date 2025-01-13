package topg.loan_mangement.exceptions;

public class ActiveLoanException extends RuntimeException {
    public ActiveLoanException(String message) {
        super(message);
    }
}