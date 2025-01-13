package topg.loan_mangement.model;

import lombok.Getter;

@Getter
public enum Status {

    ACTIVE("Loan is active"),
    COMPLETED("Loan has been completed"),
    DEFAULT("Loan is in default");

    private final String description;

    // Constructor to initialize description
    Status(String description) {
        this.description = description;
    }

}
