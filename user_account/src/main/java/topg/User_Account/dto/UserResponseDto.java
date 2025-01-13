package topg.User_Account.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

public record UserResponseDto(
        String userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address,
        String dateOfBirth,
        Date createdAt,
        Date updatedAt
) {

}
