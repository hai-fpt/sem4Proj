package com.lms.service;

import com.lms.dto.DepartmentDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DepartmentService {

    Page<Department> getAllDepartments(Pageable pageable);

    Optional<Department> findDepartmentById(Long id) throws NotFoundByIdException;

    Department createDepartment(DepartmentDTO department);

    Department updateDepartment(Long id, DepartmentDTO department) throws NotFoundByIdException;

    void deleteDepartment(Long id);
}
