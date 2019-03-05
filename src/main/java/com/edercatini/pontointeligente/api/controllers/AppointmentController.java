package com.edercatini.pontointeligente.api.controllers;

import com.edercatini.pontointeligente.api.dto.AppointmentDto;
import com.edercatini.pontointeligente.api.entities.Appointment;
import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.enums.Appointments;
import com.edercatini.pontointeligente.api.response.Response;
import com.edercatini.pontointeligente.api.services.AppointmentService;
import com.edercatini.pontointeligente.api.services.EmployeeService;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private static final Logger log = LoggerFactory.getLogger(AppointmentController.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private EmployeeService employeeService;
    private AppointmentService appointmentService;

    @Value("${pagination.amountPerPage}")
    private int amountPerPage;

    @Autowired
    public AppointmentController(EmployeeService employeeService, AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/employee/{employeeId}")
    public ResponseEntity<Response<Page<AppointmentDto>>> listByEmployeeId(
            @PathVariable("employeeId") Long employeeId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {
        log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", employeeId, pag);
        Response<Page<AppointmentDto>> response = new Response<>();

        PageRequest pageRequest = new PageRequest(pag, this.amountPerPage, Sort.Direction.valueOf(dir), ord);
        Page<Appointment> appointments = appointmentService.findByEmployeeId(employeeId, pageRequest);
        Page<AppointmentDto> appointmentsDto = appointments.map(appointment -> this.convertToDto(appointment));

        response.setData(appointmentsDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<AppointmentDto>> listById(@PathVariable("id") Long id) {
        log.info("Buscando lançamento por ID: {}", id);
        Response<AppointmentDto> response = new Response<>();
        Optional<Appointment> appointment = Optional.ofNullable(appointmentService.findById(id));

        if (!appointment.isPresent()) {
            log.info("Lançamento não encontrado para o ID: {}", id);
            response.getErrors().add("Lançamento não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertToDto(appointment.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<AppointmentDto>> add(@Valid @RequestBody AppointmentDto object, BindingResult result) throws ParseException {
        log.info("Adicionando lançamento: {}", object.toString());
        Response<AppointmentDto> response = new Response<>();
        this.validateEmployee(object, result);
        Appointment appointment = this.convertToAppointment(object, result);

        if (result.hasErrors()) {
            log.error("Erro validando lançamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        appointment = appointmentService.persist(appointment);
        response.setData(this.convertToDto(appointment));
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<AppointmentDto>> update(@PathVariable("id") Long id, @Valid @RequestBody AppointmentDto object, BindingResult result) throws ParseException {
        log.info("Atualizando lançamento: {}", object.toString());
        Response<AppointmentDto> response = new Response<>();
        this.validateEmployee(object, result);
        object.setId(Optional.of(id));
        Appointment appointment = this.convertToAppointment(object, result);

        if (result.hasErrors()) {
            log.error("Erro validando lançamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        appointment = appointmentService.persist(appointment);
        response.setData(this.convertToDto(appointment));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response<String>> remove(@PathVariable("id") Long id) {
        log.info("Removendo lançamento: {}", id);
        Response<String> response = new Response<String>();
        Optional<Appointment> appointment = Optional.ofNullable(appointmentService.findById(id));

        if (!appointment.isPresent()) {
            log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
            response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        appointmentService.delete(id);
        return ResponseEntity.ok(new Response<>());
    }

    private void validateEmployee(AppointmentDto object, BindingResult result) {
        if (object.getEmployeeId() == null) {
            result.addError(new ObjectError("funcionario", "Funcionário não informado."));
            return;
        }

        log.info("Validando funcionário id {}: ", object.getEmployeeId());
        Optional<Employee> funcionario = employeeService.findById(object.getEmployeeId());

        if (!funcionario.isPresent()) {
            result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
        }
    }

    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto object = new AppointmentDto();
        object.setId(Optional.of(appointment.getId()));
        object.setDate(this.dateFormat.format(appointment.getDate()));
        object.setType(appointment.getType().toString());
        object.setDescription(appointment.getDescription());
        object.setLocation(appointment.getLocation());
        object.setEmployeeId(appointment.getEmployee().getId());

        return object;
    }

    private Appointment convertToAppointment(AppointmentDto object, BindingResult result) throws ParseException {
        Appointment appointment = new Appointment();

        if (object.getId().isPresent()) {
            Optional<Appointment> lanc = Optional.ofNullable(appointmentService.findById(object.getId().get()));

            if (lanc.isPresent()) {
                appointment = lanc.get();
            } else {
                result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
            }
        } else {
            appointment.setEmployee(new Employee());
            appointment.getEmployee().setId(object.getEmployeeId());
        }

        appointment.setDescription(object.getDescription());
        appointment.setLocation(object.getLocation());
        appointment.setDate(this.dateFormat.parse(object.getDate()));

        if (EnumUtils.isValidEnum(Appointments.class, object.getType())) {
            appointment.setType(Appointments.valueOf(object.getType()));
        } else {
            result.addError(new ObjectError("tipo", "Tipo inválido."));
        }

        return appointment;
    }
}