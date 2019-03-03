package com.edercatini.pontointeligente.api.repositories;

import com.edercatini.pontointeligente.api.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByDocument(String document);

    Employee findByMail(String Mail);

    Employee findByDocumentOrMail(String document, String mail);
}
