package com.edercatini.pontointeligente.api.repositories;

import com.edercatini.pontointeligente.api.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@NamedQueries({
    @NamedQuery(
        name = "AppointmentRepository.findByEmployeeId",
        query = "SELECT appointment FROM Appointment appointment WHERE appointment.employee.id = :employeeId"
    )
})
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByEmployeeId(@Param("employeeId") Long employeeId);

    Page<Appointment> findByEmployeeId(@Param("employeeId") Long employeeId, Pageable pageable);
}