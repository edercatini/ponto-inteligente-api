package com.edercatini.pontointeligente.api.services;

import com.edercatini.pontointeligente.api.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AppointmentService {

    Page<Appointment> findByEmployeeId(Long id, PageRequest pageRequest);

    Appointment findById(Long id);

    Appointment persist(Appointment appointment);

    void delete(Long id);
}