package com.lms.service;

import com.lms.dto.Department;
import com.lms.dto.projection.DepartmentProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.User;
import com.lms.repository.DepartmentRepository;
import com.lms.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService{
    @Autowired
    private final DepartmentRepository departmentRepository;

    @Autowired
    private final UserRepository userRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<DepartmentProjection> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAllProjectedBy(pageable);
    }

    @Override
    public Optional<com.lms.models.Department> findDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public com.lms.models.Department createDepartment(Department department) throws NotFoundByIdException, DuplicateException {
        Optional<User> user = userRepository.findById(department.getManagerId());
        if (user.isEmpty()) {
            throw new NotFoundByIdException("Manager not found with id: " + department.getManagerId());
        }
        String departmentName = department.getName();
        com.lms.models.Department departmentByName = departmentRepository.findDepartmentByName(departmentName);
        if (!Objects.isNull(departmentByName)) {
            throw new DuplicateException("Department duplicate with name: " + departmentName);
        }
        User manager = user.get();
        com.lms.models.Department departmentEntity = new com.lms.models.Department();
        departmentEntity.setName(department.getName());
        departmentEntity.setManager(manager);
        return departmentRepository.save(departmentEntity);
    }

    @Override
    public com.lms.models.Department updateDepartment(Long id, Department department) throws NotFoundByIdException, DuplicateException {
        Optional<com.lms.models.Department> departmentOptional = departmentRepository.findById(id);
        if(departmentOptional.isEmpty()) {
            throw new NotFoundByIdException("Department not found with id: " + id);
        }
        Optional<User> user = userRepository.findById(department.getManagerId());
        if (user.isEmpty()) {
            throw new NotFoundByIdException("Manager not found with id: " + id);
        }
        String departmentName = department.getName();
        com.lms.models.Department departmentByName = departmentRepository.findDepartmentByIdNotAndName(id, departmentName);
        if(!Objects.isNull(departmentByName)) {
            throw new DuplicateException("Department duplicate with name: " + departmentName);
        }
        User manager = user.get();
        com.lms.models.Department departmentEntity = departmentOptional.get();
        departmentEntity.setName(department.getName());
        departmentEntity.setManager(manager);
        return departmentRepository.save(departmentEntity);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
