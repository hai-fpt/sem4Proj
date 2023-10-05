package com.lms.Controller;

import com.lms.DTO.RoleDTO;
import com.lms.Models.Role;
import com.lms.Service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RoleController {
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public RoleController(RoleServiceImpl roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping("/role")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleServiceImpl.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("role/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id){
        Optional<Role> role = roleServiceImpl.findRoleById(id);
        return role.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/role")
    public  ResponseEntity<Role> createRole(@RequestBody RoleDTO role){
        Role newRole = roleServiceImpl.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    @PutMapping("role/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Long id, @RequestBody RoleDTO role){
        role.setId(id);
        Role updateRole = roleServiceImpl.updateRole(role);
        return ResponseEntity.ok(updateRole);
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id){
        roleServiceImpl.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
