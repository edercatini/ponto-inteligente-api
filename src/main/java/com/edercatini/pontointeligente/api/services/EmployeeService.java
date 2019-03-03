package com.edercatini.pontointeligente.api.services;

import com.edercatini.pontointeligente.api.entities.Employee;

import java.util.Optional;

public interface EmployeeService {

    Optional<Employee> findById(Long id);

    Optional<Employee> findByDocument(String document);

    Optional<Employee> findByMail(String mail);

    Optional<Employee> findByDocumentOrMail(String document, String mail);

    Employee persist(Employee employee);
}