package com.edercatini.pontointeligente.api.repositories;

import com.edercatini.pontointeligente.api.entities.Appointment;
import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.enums.Appointments;
import com.edercatini.pontointeligente.api.enums.Profiles;
import com.edercatini.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    private Long EmployeeId;

    @Before
    public void setUp() throws Exception {
        Enterprise enterprise = this.enterpriseRepository.save(this.getEnterpriseData());

        Employee employee = this.employeeRepository.save(this.getEmployeeData(enterprise));
        this.EmployeeId = employee.getId();

        this.appointmentRepository.save(this.getAppointmentData(employee));
        this.appointmentRepository.save(this.getAppointmentData(employee));
    }

    @After
    public void tearDown() throws Exception {
        this.enterpriseRepository.deleteAll();
    }

    @Test
    public void findAppointmentsByEmployeeId() {
        List<Appointment> Appointments = this.appointmentRepository.findByEmployeeId(EmployeeId);

        assertEquals(2, Appointments.size());
    }

    @Test
    public void findAppointmentsByEmployeeIdWithPaginatedResult() {
        PageRequest page = new PageRequest(0, 10);
        Page<Appointment> appointments = this.appointmentRepository.findByEmployeeId(EmployeeId, page);

        assertEquals(2, appointments.getTotalElements());
    }

    private Appointment getAppointmentData(Employee Employee) {
        Appointment appointment = new Appointment();
        appointment.setDate(new Date());
        appointment.setType(Appointments.LUNCH_START_TIME);
        appointment.setEmployee(Employee);

        return appointment;
    }

    private Employee getEmployeeData(Enterprise enterprise) throws NoSuchAlgorithmException {
        Employee employee = new Employee();
        employee.setName("Fulano de Tal");
        employee.setProfile(Profiles.ROLE_USER);
        employee.setPassword(PasswordUtils.generateBCrypt("123456"));
        employee.setDocument("24291173474");
        employee.setMail("email@email.com");
        employee.setEnterprise(enterprise);
        employee.setAmountOfLunchTime(60000L);

        return employee;
    }

    private Enterprise getEnterpriseData() {
        Enterprise enterprise = new Enterprise();
        enterprise.setSocialName("Empresa de exemplo");
        enterprise.setDocument("51463645000100");

        return enterprise;
    }
}