package com.alibou.security.role;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles")
    public Role addRole(
            @RequestBody RoleRequestDto roleRequestDto
    ){
        return roleService.addRole(roleRequestDto);
    }

    @GetMapping("/roles/{company-id}")
    public List<RoleResponceDto> findAllRoles(
            @PathVariable("company-id") String companyId
    ){
        return roleService.findAll(Integer.parseInt(companyId));

    }

    @PutMapping("/roles")
    public Role updateRole(
            @RequestBody RoleUpdated roleUpdated){
        return roleService.updateRole(roleUpdated);
    }

     @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
        String errorMessage = e.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }



}
