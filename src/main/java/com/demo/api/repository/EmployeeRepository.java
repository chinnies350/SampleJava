package com.demo.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.api.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
