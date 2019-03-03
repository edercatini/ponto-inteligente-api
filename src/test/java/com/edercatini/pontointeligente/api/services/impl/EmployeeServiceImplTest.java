package com.edercatini.pontointeligente.api.services.impl;

import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.repositories.EmployeeRepository;
import com.edercatini.pontointeligente.api.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeServiceImplTest {

    @MockBean
    private EmployeeRepository repository;

    @Autowired
    private EmployeeService service;

    private static final String DOCUMENT = "6516565165165165";
    private static final String MAIL = "test@test.com";

    @Before
    public void setUp() {
        given(repository.findById(anyLong()))
            .willReturn(Optional.of(new Employee()));

        given(repository.findByDocument(anyString()))
            .willReturn(new Employee());

        given(repository.findByMail(anyString()))
            .willReturn(new Employee());

        given(repository.findByDocumentOrMail(anyString(), anyString()))
            .willReturn(new Employee());

        given(repository.save(any(Employee.class)))
            .willReturn(new Employee());
    }

    @Test
    public void mustFindById() {
        Optional<Employee> employee = service.findById(50050L);
        assertTrue(employee.isPresent());
    }

    @Test
    public void mustFindByDocument() {
        Optional<Employee> employee = service.findByDocument(DOCUMENT);
        assertTrue(employee.isPresent());
    }

    @Test
    public void mustFindByMail() {
        Optional<Employee> employee = service.findByMail(MAIL);
        assertTrue(employee.isPresent());
    }

    @Test
    public void mustFindByDocumentOrMail() {
        Optional<Employee> employee = service.findByDocumentOrMail(DOCUMENT, MAIL);
        assertTrue(employee.isPresent());
    }

    @Test
    public void testMustPersistAnEmployee() {
        Employee employee = service.persist(new Employee());
        assertNotNull(employee);
    }
}