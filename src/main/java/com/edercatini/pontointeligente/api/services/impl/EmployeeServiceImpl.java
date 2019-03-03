package com.edercatini.pontointeligente.api.services.impl;

import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.repositories.EmployeeRepository;
import com.edercatini.pontointeligente.api.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Employee> findByDocument(String document) {
        logger.info("Procurando por funcionário com CNPJ {}", document);
        return Optional.ofNullable(repository.findByDocument(document));
    }

    @Override
    public Optional<Employee> findByMail(String mail) {
        logger.info("Procurando por funcionário com e-mail {}", mail);
        return Optional.ofNullable(repository.findByMail(mail));
    }

    @Override
    public Optional<Employee> findByDocumentOrMail(String document, String mail) {
        logger.info("Procurando por funcionário com e-mail ou documento");
        return Optional.ofNullable(repository.findByDocumentOrMail(document, mail));
    }

    public Employee persist(Employee employee) {
        return repository.save(employee);
    }
}