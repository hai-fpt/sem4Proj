package com.lms.service;

import com.lms.dto.DepartmentDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Department;
import com.lms.models.User;
import com.lms.repository.DepartmentRepository;
import com.lms.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public Optional<Department> findDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department createDepartment(DepartmentDTO department) {
        Optional<User> user = userRepository.findById(department.getManagerId());
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found");
        }
        User manager = user.get();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<DepartmentDTO, Department>() {
            @Override
            protected void configure() {
                map().setManager(manager);
            }
        });
        Department departmentEntity = modelMapper.map(department, Department.class);
        return departmentRepository.save(departmentEntity);
    }

    @Override
    public Department updateDepartment(Long id, DepartmentDTO department) throws NotFoundByIdException {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if(departmentOptional.isEmpty()){
            throw new NotFoundByIdException("Department find by id not found");
        }
        Optional<User> user = userRepository.findById(department.getManagerId());
        if (user.isEmpty()){
            throw new NotFoundByIdException("Manager find by id not found");
        }
        User manager = user.get();
        Department departmentEntity = departmentOptional.get();
        departmentEntity.setName(department.getName());
        departmentEntity.setManager(manager);
        return departmentRepository.save(departmentEntity);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
