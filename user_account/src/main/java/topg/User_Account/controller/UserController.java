package topg.User_Account.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.User_Account.dto.UserLoginDto;
import topg.User_Account.dto.UserLoginResponseDto;
import topg.User_Account.dto.UserRequestDto;
import topg.User_Account.dto.UserResponseDto;
import topg.User_Account.model.User;
import topg.User_Account.repository.UserRepository;
import topg.User_Account.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> createUser( @Valid  @RequestBody UserRequestDto userRequestDto){
        UserResponseDto message = userService.createUser(userRequestDto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") String userId){
        UserResponseDto message = userService.getUserById(userId);
        return ResponseEntity.ok(message);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> editUserById(   @PathVariable("id") String userId, UserRequestDto userRequestDto){
        UserResponseDto message = userService.editUserById(userId, userRequestDto);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id")String userId){
        String message = userService.deleteUserById(userId);
        return ResponseEntity.ok(message);

    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserLoginResponseDto> verify(@RequestBody UserLoginDto userLoginDto){
        UserLoginResponseDto message = userService.verify(userLoginDto);
        return ResponseEntity.ok(message);

    }


}
