package com.lms.service;

import com.lms.dto.RoleDTO;
import com.lms.models.Role;
import com.lms.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<Role> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
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
