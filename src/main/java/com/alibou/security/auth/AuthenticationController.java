package com.alibou.security.auth;

import com.alibou.security.company.Company;
import com.alibou.security.company.CompanyDto;
import com.alibou.security.company.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final CompanyService companyService;

  @GetMapping("/user-details/{token}")
  public UserInfosDto getUserDetails(
          @PathVariable("token") String token
  ){
    return authenticationService.getUserDetails(token);
  }


  @PostMapping("/register/user-infos")
  public String userRegister(
      @RequestBody RegisterRequest request
  ) {
    return authenticationService.userRegister(request);
  }

  @PostMapping("/register/company-infos")
  public String companyRegister(
          @RequestBody CompanyDto companyDto
          ){
    return authenticationService.companyRegister(companyDto);
  }


  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    authenticationService.refreshToken(request, response);
  }


  @PostMapping("/send-email")
  public String sendEmail(
          @RequestBody EmailDto emailDto
  ){
     return authenticationService.sendEmail(emailDto);
  }

  @PostMapping("/reset-password")
  public String resetPassword(
          @RequestBody PasswordDto passwordDto
  ){
    return authenticationService.resetPassword(passwordDto);
  }

  @PostMapping("/is-password-forgetten/{token}")
  public Boolean isPasswordForgetten(
          @PathVariable("token") String token){
    return authenticationService.isPasswordForgetten(token);
  }


  @PutMapping("/authenticate-as-invited")
  public ResponseEntity<AuthenticationResponse> authenticateMembre(
          @RequestBody AuthenticationRequest request
  ){
      return ResponseEntity.ok(authenticationService.authenticateMembre(request));
  }












}
