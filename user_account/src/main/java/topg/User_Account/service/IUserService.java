package topg.User_Account.service;

import topg.User_Account.dto.UserRequestDto;
import topg.User_Account.dto.UserResponseDto;

public interface IUserService {

    UserResponseDto  createUser(UserRequestDto userRequestDto);
    UserResponseDto  getUserById(String userid);
    UserResponseDto  editUserById(String userId, UserRequestDto userRequestDto);
    String deleteUserById(String userId);
}
