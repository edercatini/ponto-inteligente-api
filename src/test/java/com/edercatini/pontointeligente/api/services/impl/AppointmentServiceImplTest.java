package com.edercatini.pontointeligente.api.services.impl;

import com.edercatini.pontointeligente.api.entities.Appointment;
import com.edercatini.pontointeligente.api.repositories.AppointmentRepository;
import com.edercatini.pontointeligente.api.services.AppointmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AppointmentServiceImplTest {

    @MockBean
    private AppointmentRepository repository;

    @Autowired
    private AppointmentService service;

    @Before
    public void setUp() {
        given(repository.findByEmployeeId(anyLong(), any(PageRequest.class)))
            .willReturn(new PageImpl<>(new ArrayList<>()));

        given(repository.findById(anyLong()))
            .willReturn(Optional.of(new Appointment()));

        given(repository.save(any(Appointment.class)))
            .willReturn(new Appointment());
    }

    @Test
    public void findAppointmentsByEmployeeId() {
        Page<Appointment> appointments = service.findByEmployeeId(1L, new PageRequest(0, 10));
        assertNotNull(appointments);
    }

    @Test
    public void findAppointmentsById() {
        Optional<Appointment> appointment = Optional.ofNullable(service.findById(1L));
        assertTrue(appointment.isPresent());
    }

    @Test
    public void mustPersistAppointment() {
        Appointment appointment = service.persist(new Appointment());
        assertNotNull(appointment);
    }
}