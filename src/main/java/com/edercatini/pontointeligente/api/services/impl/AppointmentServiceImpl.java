package com.edercatini.pontointeligente.api.services.impl;

import com.edercatini.pontointeligente.api.entities.Appointment;
import com.edercatini.pontointeligente.api.repositories.AppointmentRepository;
import com.edercatini.pontointeligente.api.services.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private AppointmentRepository repository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Appointment> findByEmployeeId(Long id, PageRequest pageRequest) {
        logger.info("Buscando lançamentos para o funcionário ID {}", id);
        return repository.findByEmployeeId(id, pageRequest);
    }

    @Cacheable("byId")
    @Override
    public Appointment findById(Long id) {
        logger.info("Buscando um lançamento pelo ID {}", id);
        Optional<Appointment> appointment = repository.findById(id);
        return appointment.orElse(null);
    }

    @Override
    public Appointment persist(Appointment appointment) {
        logger.info("Persistindo o lançamento: {}", appointment);
        return repository.save(appointment);
    }

    @Override
    public void delete(Long id) {
        logger.info("Removendo o lançamento ID {}", id);
        repository.deleteById(id);
    }
}