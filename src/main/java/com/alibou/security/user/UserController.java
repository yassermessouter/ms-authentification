package com.alibou.security.user;

import com.alibou.security.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000",allowedHeaders = "*") // Allow requests from http://localhost:3000

public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @PostMapping("/invite-user")
    public User invite(
            @RequestBody UserDto userDto
    ){
        return authenticationService.invite(userDto);
    }

    @GetMapping("/users/{company-id}")
    public List<UserDetailsDto> showUser(
            @PathVariable("company-id") String companyId
    ){
        return userService.showUsers(Integer.parseInt(companyId));
    }

    @PatchMapping("/users")
    public String editUser(
            @RequestBody UserUpdatedDto userUpdatedDto
    ){
        return userService.editUser(userUpdatedDto);
    }

    @GetMapping("/profile")
    public ProfileResponseDto getUser(
             HttpServletRequest request
    ){
        return userService.showUser(request);
    }


    @PutMapping("/change-password")
    public String changePassword(
            HttpServletRequest request,
            @RequestBody  PasswordChangedDto passwordChangedDto
    ){
        return userService.changePassword(request,passwordChangedDto);
    }

    @PutMapping("/profile/{id}")
    public String update(
            @PathVariable("id") String id,
            @RequestBody ProfileRequestDto profileRequestDto

    ){
        return userService.update(Integer.parseInt(id),profileRequestDto);
    }

    @GetMapping("/user-details")
    public UserResponseDto get(
            HttpServletRequest request
    ){
        return userService.getUserDetails(request);
    }
    @PostMapping("/logout/{token}")
    public void logout(@PathVariable("token") String token){
        userService.logout(token);
    }



}
