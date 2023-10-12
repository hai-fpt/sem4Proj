package com.lms.controller;

import com.lms.dto.Role;
import com.lms.dto.projection.RoleProjection;
import com.lms.service.RoleServiceImpl;
import com.lms.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/role")
public class RoleController {
    private final RoleServiceImpl roleServiceImpl;
    private final ControllerUtils controllerUtils;

    @Autowired
    public RoleController(RoleServiceImpl roleServiceImpl, ControllerUtils controllerUtils) {
        this.roleServiceImpl = roleServiceImpl;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping()
    public ResponseEntity<Page<RoleProjection>> getAllRoles(@PageableDefault(page = 0, size = 10)Pageable pageable){
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<RoleProjection> roles = roleServiceImpl.getAllRoles(sorted);
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
