package com.lms.Service;

import com.lms.DTO.RoleDTO;
import com.lms.Models.Role;
import com.lms.Repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role createRole(RoleDTO role) {
        ModelMapper modelMapper = new ModelMapper();
        Role roleEntity = modelMapper.map(role, Role.class);
        return roleRepository.save(roleEntity);
    }

    @Override
    public Role updateRole(RoleDTO role) {
        ModelMapper modelMapper = new ModelMapper();
        Role roleEntity = modelMapper.map(role, Role.class);
        return roleRepository.save(roleEntity);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
