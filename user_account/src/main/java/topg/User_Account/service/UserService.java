package topg.User_Account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import topg.User_Account.dto.UserLoginDto;
import topg.User_Account.dto.UserLoginResponseDto;
import topg.User_Account.dto.UserRequestDto;
import topg.User_Account.dto.UserResponseDto;
import topg.User_Account.exceptions.UserNotFoundException;
import topg.User_Account.model.User;
import topg.User_Account.repository.UserRepository;
import topg.User_Account.security.JwtService;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService  {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private  final JwtService jwtService;


    @Transactional
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String userId = UUID.randomUUID().toString();
        User user = User.builder()
                .userId(userId)
                .firstName(userRequestDto.firstName())
                .lastName(userRequestDto.lastName())
                .email(userRequestDto.email())
                .phoneNumber(userRequestDto.phoneNumber())
                .password(passwordEncoder.encode(userRequestDto.password()))
                .address(userRequestDto.address())
                .dateOfBirth(userRequestDto.dateOfBirth())
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .active(true)
                .build();
        user = userRepository.save(user);

        return new UserResponseDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getCreatedAt(),
                user.getUpdatedAt()


        );
    }

    @Override
    public UserResponseDto getUserById(String userId) {
        // Find the user by ID using Optional
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        // Map the User entity to UserResponseDto
        return new UserResponseDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    @Override
    public UserResponseDto editUserById(String userId, UserRequestDto userRequestDto) {
        // Retrieve the user or throw an exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        // Update the user's details
        user.setFirstName(userRequestDto.firstName());
        user.setLastName(userRequestDto.lastName());
        user.setEmail(userRequestDto.email());
        user.setPhoneNumber(userRequestDto.phoneNumber());
        user.setAddress(userRequestDto.address());
        user.setDateOfBirth(userRequestDto.dateOfBirth());
        user.setPassword(userRequestDto.password());

        // Save the updated user back to the repository
        user = userRepository.save(user);

        // Return the updated user details in the response DTO
        return new UserResponseDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


    @Override
    public String deleteUserById(String userId) {
        // Check if the user exists, or throw an exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        // Delete the user
        userRepository.delete(user);

        // Return a success message
        return "User with ID " + userId + " has been successfully deleted.";
    }

    public UserLoginResponseDto verify(UserLoginDto user) {
        Authentication authentication =    authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.email(), user.password())
        );

        if(authentication.isAuthenticated()){
            return new UserLoginResponseDto(
                    "Success",
                    jwtService.generateToken(user.email())
            );
        }
        return null;
    }


}

