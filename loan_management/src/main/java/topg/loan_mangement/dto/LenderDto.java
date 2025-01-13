package topg.loan_mangement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record LenderDto(



        @NotNull(message = "Name cannot be null")
        @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
        String name,

        @NotNull(message = "Balance cannot be null")
        BigDecimal balance

) {
}
