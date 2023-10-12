package com.lms.service;

import com.lms.dto.Role;
import com.lms.dto.projection.RoleProjection;
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
    public Page<RoleProjection> getAllRoles(Pageable pageable) {
        return roleRepository.findAllProjectedBy(pageable);
    }

    @Override
    public Optional<com.lms.models.Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public com.lms.models.Role createRole(Role role) {
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.Role roleEntity = modelMapper.map(role, com.lms.models.Role.class);
        return roleRepository.save(roleEntity);
    }

    @Override
    public com.lms.models.Role updateRole(Role role) {
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.Role roleEntity = modelMapper.map(role, com.lms.models.Role.class);
        return roleRepository.save(roleEntity);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
