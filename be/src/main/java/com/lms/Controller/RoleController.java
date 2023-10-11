package com.lms.controller;

import com.lms.dto.Role;
import com.lms.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public RoleController(RoleServiceImpl roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping()
    public ResponseEntity<Page<com.lms.models.Role>> getAllRoles(@PageableDefault(page = 0, size = 10)Pageable pageable){
        Page<com.lms.models.Role> roles = roleServiceImpl.getAllRoles(pageable);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.lms.models.Role> getRoleById(@PathVariable("id") Long id){
        Optional<com.lms.models.Role> role = roleServiceImpl.findRoleById(id);
        return role.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public  ResponseEntity<com.lms.models.Role> createRole(@RequestBody Role role){
        com.lms.models.Role newRole = roleServiceImpl.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.lms.models.Role> updateRole(@PathVariable("id") Long id, @RequestBody Role role){
        role.setId(id);
        com.lms.models.Role updateRole = roleServiceImpl.updateRole(role);
        return ResponseEntity.ok(updateRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id){
        roleServiceImpl.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
