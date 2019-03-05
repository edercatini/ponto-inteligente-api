package com.edercatini.pontointeligente.api.controllers;

import com.edercatini.pontointeligente.api.dto.AppointmentDto;
import com.edercatini.pontointeligente.api.entities.Appointment;
import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.enums.Appointments;
import com.edercatini.pontointeligente.api.services.AppointmentService;
import com.edercatini.pontointeligente.api.services.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private EmployeeService employeeService;

    private static final String BASE_URL = "/api/appointments/";
    private static final Long EMPLOYEE_ID = 1L;
    private static final Long APPOINTMENT_ID = 1L;
    private static final String TYPE = Appointments.WORK_START_TIME.name();
    private static final Date DATE = new Date();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    @WithMockUser
    public void registerAppointment() throws Exception {
        Appointment appointment = getAppointmentData();
        given(employeeService.findById(anyLong())).willReturn(Optional.of(new Employee()));
        given(appointmentService.persist(any(Appointment.class))).willReturn(appointment);

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(this.getPostRequestJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(APPOINTMENT_ID))
                .andExpect(jsonPath("$.data.type").value(TYPE))
                .andExpect(jsonPath("$.data.date").value(this.dateFormat.format(DATE)))
                .andExpect(jsonPath("$.data.employeeId").value(EMPLOYEE_ID))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithMockUser
    public void appointmentWithInvalidEmployeeId() throws Exception {
        given(employeeService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(this.getPostRequestJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Funcionário não encontrado. ID inexistente."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void mustRemoveAppointment() throws Exception {
        given(appointmentService.findById(anyLong())).willReturn(new Appointment());

        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + APPOINTMENT_ID)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    private String getPostRequestJson() throws JsonProcessingException {
        AppointmentDto object = new AppointmentDto();
        object.setId(null);
        object.setDate(this.dateFormat.format(DATE));
        object.setType(TYPE);
        object.setEmployeeId(EMPLOYEE_ID);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private Appointment getAppointmentData() {
        Appointment appointment = new Appointment();
        appointment.setId(APPOINTMENT_ID);
        appointment.setDate(DATE);
        appointment.setType(Appointments.valueOf(TYPE));
        appointment.setEmployee(new Employee());
        appointment.getEmployee().setId(EMPLOYEE_ID);
        return appointment;
    }
}