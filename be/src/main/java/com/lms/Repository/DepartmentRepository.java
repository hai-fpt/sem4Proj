package com.lms.repository;

import com.lms.dto.projection.DepartmentProjection;
import com.lms.models.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findDepartmentByName(String name);

    Department findDepartmentByIdNotAndName(Long id, String name);

    Page<DepartmentProjection> findAllProjectedBy(Pageable pageable);
}
