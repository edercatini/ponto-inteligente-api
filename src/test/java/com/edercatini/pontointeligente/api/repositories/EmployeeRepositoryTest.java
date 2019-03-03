package com.edercatini.pontointeligente.api.repositories;

import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.enums.Profiles;
import com.edercatini.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    private static final String DOCUMENT = "16516165165";
    private static final String MAIL = "teste@teste.com";

    @Before
    public void setUp() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.getEnterpriseData());
        employeeRepository.save(this.getEmployeeData(enterprise));
    }

    private Employee getEmployeeData(Enterprise enterprise) {
        Employee employee = new Employee();
        employee.setName("Name");
        employee.setProfile(Profiles.ROLE_USER);
        employee.setPassword(PasswordUtils.generateBCrypt("123456"));
        employee.setDocument(DOCUMENT);
        employee.setMail(MAIL);
        employee.setAmountOfLunchTime(60000L);
        employee.setEnterprise(enterprise);

        return employee;
    }

    private Enterprise getEnterpriseData() {
        Enterprise enterprise = new Enterprise();
        enterprise.setSocialName("Teste");
        enterprise.setDocument("57799849898");

        return enterprise;
    }

    @After
    public void tearDown() {
        enterpriseRepository.deleteAll();
    }

    @Test
    public void mustFindByDocument() {
        Employee employee = employeeRepository.findByDocument(DOCUMENT);
        assertEquals(DOCUMENT, employee.getDocument());
    }

    @Test
    public void mustFindByMail() {
        Employee employee = employeeRepository.findByMail(MAIL);
        assertEquals(MAIL, employee.getMail());
    }

    @Test
    public void mustFindByMailOrDocument() {
        Employee employee = employeeRepository.findByDocumentOrMail(DOCUMENT, MAIL);
        assertEquals(MAIL, employee.getMail());
        assertEquals(DOCUMENT, employee.getDocument());
    }
}