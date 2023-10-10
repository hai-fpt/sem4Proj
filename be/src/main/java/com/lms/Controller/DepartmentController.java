package com.lms.controller;

import com.lms.dto.DepartmentDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Department;
import com.lms.service.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentServiceImpl departmentServiceImpl;

    private final ControllerUtils controllerUtils;

    @Autowired
    public DepartmentController(DepartmentServiceImpl departmentServiceImpl, ControllerUtils controllerUtils) {
        this.departmentServiceImpl = departmentServiceImpl;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping()
    public ResponseEntity<Page<Department>> getAllDepartments(@PageableDefault(page = 0, size = 10)Pageable pageable){
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<Department> departments = departmentServiceImpl.getAllDepartments(sorted);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDepartmentById(@PathVariable("id") Long id){
        if(Objects.isNull(id) || id < 0){
            String message = "Id invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Optional<Department> department = departmentServiceImpl.findDepartmentById(id);
        return department.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity createDepartment(@RequestBody DepartmentDTO department){
        if(Objects.isNull(department)){
            String message = "Department invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(department.getName().isEmpty()){
            String message = "Name invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (Objects.isNull(department.getManagerId()) || department.getManagerId() < 0){
            String message = "Manager Id invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Department newDepartment = departmentServiceImpl.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDepartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateDepartment(@PathVariable("id") Long id, @RequestBody DepartmentDTO department) throws NotFoundByIdException {
        if(Objects.isNull(id) || id < 0){
            String message = "Id invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (Objects.isNull(department)){
            String message = "Department invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(department.getName().isEmpty()){
            String message = "No Name";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (Objects.isNull(department.getManagerId()) || department.getManagerId() < 0){
            String message = "No Manager";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Department updateDepartment = departmentServiceImpl.updateDepartment(id, department);
        return ResponseEntity.ok(updateDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDepartment(@PathVariable("id") Long id){
        if(Objects.isNull(id) || id < 0){
            String message = "Id invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        departmentServiceImpl.deleteDepartment(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
