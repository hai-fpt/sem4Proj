package com.lms.controller;

import com.lms.dto.Department;
import com.lms.dto.projection.DepartmentProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.service.DepartmentService;
import com.lms.utils.ControllerUtils;
import com.lms.utils.ProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

import static com.lms.utils.Constants.*;

@RestController
@RequestMapping("/api/admin/department")
public class DepartmentController {
    @Autowired
    private final DepartmentService departmentService;

    @Autowired
    private final ControllerUtils controllerUtils;

    public DepartmentController(DepartmentService departmentService, ControllerUtils controllerUtils) {
        this.departmentService = departmentService;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping()
    public ResponseEntity<Page<DepartmentProjection>> getAllDepartments(@PageableDefault(page = 0, size = 10)Pageable pageable){
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<DepartmentProjection> departments = departmentService.getAllDepartments(sorted);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentProjection> getDepartmentById(@PathVariable("id") Long id) throws NotFoundByIdException {
        if(Objects.isNull(id) || id < 0){
            throw new NullPointerException(INVALID_ID);
        }
        Optional<com.lms.models.Department> departmentOptional = departmentService.findDepartmentById(id);
        com.lms.models.Department department = departmentOptional.get();
        DepartmentProjection departmentProjection = ProjectionMapper.mapToDepartmentProjection(department);
        return ResponseEntity.status(HttpStatus.OK).body(departmentProjection);
    }

    @PostMapping()
    public ResponseEntity<DepartmentProjection> createDepartment(@RequestBody Department department) throws NotFoundByIdException, DuplicateException {
        if(Objects.isNull(department)){
            throw new NullPointerException(INVALID_PAYLOAD);
        }
        if(department.getName() == null ||department.getName().isEmpty()){
            throw new NullPointerException(INVALID_NAME);
        }
        if (Objects.isNull(department.getManagerId()) || department.getManagerId() < 0){
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        com.lms.models.Department newDepartment = departmentService.createDepartment(department);
        DepartmentProjection departmentProjection = ProjectionMapper.mapToDepartmentProjection(newDepartment);
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentProjection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentProjection> updateDepartment(@PathVariable("id") Long id, @RequestBody Department department) throws NotFoundByIdException, DuplicateException {
        if(Objects.isNull(id) || id < 0){
            throw new NullPointerException(INVALID_ID);
        }
        if (Objects.isNull(department)){
            throw new NullPointerException(INVALID_PAYLOAD);
        }
        if(department.getName() == null || department.getName().isEmpty()){
            throw new NullPointerException(INVALID_NAME);
        }
        if (Objects.isNull(department.getManagerId()) || department.getManagerId() < 0){
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        com.lms.models.Department updateDepartment = departmentService.updateDepartment(id, department);
        DepartmentProjection departmentProjection = ProjectionMapper.mapToDepartmentProjection(updateDepartment);
        return ResponseEntity.status(HttpStatus.OK).body(departmentProjection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDepartment(@PathVariable("id") Long id){
        if(Objects.isNull(id) || id < 0){
            throw new NullPointerException(INVALID_ID);
        }
        departmentService.deleteDepartment(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
