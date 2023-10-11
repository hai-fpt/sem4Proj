package com.lms.service;

import com.lms.dto.Department;
import com.lms.dto.projection.DepartmentProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DepartmentService {

    Page<DepartmentProjection> getAllDepartments(Pageable pageable);

    Optional<com.lms.models.Department> findDepartmentById(Long id) throws NotFoundByIdException;

    com.lms.models.Department createDepartment(Department department) throws NotFoundByIdException, DuplicateException;

    com.lms.models.Department updateDepartment(Long id, Department department) throws NotFoundByIdException, DuplicateException;

    void deleteDepartment(Long id);
}
