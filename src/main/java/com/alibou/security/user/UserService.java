package com.alibou.security.user;

import com.alibou.security.auth.AuthenticationService;
import com.alibou.security.company.Company;
import com.alibou.security.company.CompanyRepository;
import com.alibou.security.config.JwtService;
import com.alibou.security.role.Role;
import com.alibou.security.role.RoleRepository;
import com.alibou.security.token.Token;
import com.alibou.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    HttpServletRequest request;



    public List<UserDetailsDto> showUsers(Integer companyId) {
        Company company=companyRepository.findById(companyId).orElseThrow();
        List<User> users=userRepository.findAllByCompany(company);
        List<UserDetailsDto> userDetailsDtos=new ArrayList<>();
        Role role;
        for (User user:users){
            role=user.getRole();
            userDetailsDtos.add(UserDetailsDto.builder()
                            .fullname(user.getFullname())
                            .roleName(role.getName())
                            .email(user.getEmail())
                            .stateType(user.getStateType())
                    .build());
        }
        return userDetailsDtos;
    }

    public String editUser(UserUpdatedDto userUpdatedDto) {
        User user=userRepository.findByEmail(userUpdatedDto.getEmail()).orElseThrow();
        Role role=roleRepository.findByNameAndCompanyId(userUpdatedDto.getRoleName(),user.getCompany().getId());
        user.setStateType(userUpdatedDto.getStateType());
        user.setRole(role);
        userRepository.save(user);
        return "User updated";

    }
    public ProfileResponseDto showUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            Token jwt = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
            if (!jwt.isExpired() && !jwt.isRevoked()) {
                User user = jwt.getUser();
                Role role = user.getRole();
                Company company=user.getCompany();
                return ProfileResponseDto.builder()
                        .email(user.getEmail())
                        .fullname(user.getFullname())
                        .permissions(role.getPermissions())
                        .companyName(company.getName())
                        .adress(company.getAddress())
                        .wilayas(company.getWilayas())
                        .build();
            } else {
                throw new RuntimeException("Token invalide");
            }
        } else {
            throw new RuntimeException("Authorization header missing or invalid");
        }
    }

    public String changePassword(HttpServletRequest request, PasswordChangedDto passwordChangedDto) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            Token jwt = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
            if (!jwt.isExpired() && !jwt.isRevoked()) {
                User user = jwt.getUser();
                if (!passwordEncoder.matches(passwordChangedDto.getOldPassword(), user.getPassword())) {
                    throw new IllegalStateException("Wrong password");
                }
                user.setPassword(passwordEncoder.encode(passwordChangedDto.getNewPassword()));
                userRepository.save(user);
                return "Password changed";
            } else {
                throw new RuntimeException("Token invalide");
            }
        } else {
            throw new RuntimeException("Authorization header missing or invalid");
        }
    }


    public String update(Integer id, ProfileRequestDto profileRequestDto) {

                User user = userRepository.findById(id).orElseThrow();
                Company company = user.getCompany();

                // Null check before setting company fields
                if (profileRequestDto.getCompanyName() != null) {
                    company.setName(profileRequestDto.getCompanyName());
                }
                if (profileRequestDto.getAdress() != null) {
                    company.setAddress(profileRequestDto.getAdress());
                }
                if (profileRequestDto.getWilayas() != null) {
                    company.setWilayas(profileRequestDto.getWilayas());
                }
                if (profileRequestDto.getHasDeliveryDates() != null) {
                    company.setHasDeliveryDate(profileRequestDto.getHasDeliveryDates());
                }
                if (profileRequestDto.getDescription() != null) {
                    company.setDescription(profileRequestDto.getDescription());
                }
                if (profileRequestDto.getCompanyEmail() != null) {
                    company.setEmail(profileRequestDto.getCompanyEmail());
                }
                if (profileRequestDto.getNumber() != null) {
                    company.setNumber(profileRequestDto.getNumber());
                }

                companyRepository.save(company);

                // Null check before setting user fields
                if (profileRequestDto.getFullname() != null) {
                    user.setFullname(profileRequestDto.getFullname());
                }


                userRepository.save(user);
                return "OK";



    }

    public UserResponseDto getUserDetails(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            Token jwt = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
            if (!jwt.isExpired() && !jwt.isRevoked()) {
                User user = jwt.getUser();
                Role role = user.getRole();
                Company company=user.getCompany();
                return UserResponseDto.builder()
                        .userId(user.getId())
                        .name(user.getFullname())
                        .email(user.getEmail())
                        .permissions(role.getPermissions())
                        .companyId(company.getId())
                        .companyName(company.getName())
                        .address(company.getAddress())
                        .hasDeliveryDate(company.getHasDeliveryDate())
                        .number(company.getNumber())
                        .description(company.getDescription())
                        .categories(company.getCategories())
                        .companyEmail(company.getEmail())
                        .wilayas(company.getWilayas())
                        .build();
            } else {
                throw new RuntimeException("Token invalide");
            }
        } else {
            throw new RuntimeException("Authorization header missing or invalid");
        }
    }

    public void logout(String token) {
            Token jwt = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
            if (!jwt.isExpired() && !jwt.isRevoked()) {
                jwt.setRevoked(true);
                jwt.setExpired(true);
                tokenRepository.save(jwt);
            } else {
                throw new RuntimeException("Token invalide");
            }

    }

}
