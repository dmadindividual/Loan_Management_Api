package topg.User_Account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



public record UserRequestDto(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        String email,

        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid format (e.g., +1234567890)")
        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password should be at least 6 characters long")
        String password,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Date of birth is required")
        String dateOfBirth
) {
}
