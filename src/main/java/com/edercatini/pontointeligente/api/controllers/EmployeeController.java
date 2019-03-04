package com.edercatini.pontointeligente.api.controllers;

import com.edercatini.pontointeligente.api.dto.EmployeeDto;
import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.response.Response;
import com.edercatini.pontointeligente.api.services.EmployeeService;
import com.edercatini.pontointeligente.api.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*")
public class EmployeeController {
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<EmployeeDto>> update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeDto object, BindingResult result) throws NoSuchAlgorithmException {
        log.info("Atualizando funcionário: {}", object);
        Response<EmployeeDto> response = new Response<>();

        Optional<Employee> employee = service.findById(id);

        if (!employee.isPresent()) {
            result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
        }

        this.updateEmployeeData(employee.get(), object, result);

        if (result.hasErrors()) {
            log.error("Erro validando funcionário: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        service.persist(employee.get());
        response.setData(this.convertToDto(employee.get()));

        return ResponseEntity.ok(response);
    }

    private void updateEmployeeData(Employee employee, EmployeeDto object, BindingResult result) throws NoSuchAlgorithmException {
        employee.setName(object.getName());

        if (!employee.getMail().equals(object.getMail())) {
            service.findByMail(object.getMail())
                .ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));

            employee.setMail(object.getMail());
        }

        employee.setAmountOfLunchTime(null);
        object.getAmountOfLunchTime()
            .ifPresent(amountOfLunchTime -> employee.setAmountOfLunchTime(Long.valueOf(amountOfLunchTime)));

        employee.setAmountOfWorkTime(null);
        object.getAmountOfWorkTime()
            .ifPresent(amountOfWorkTime -> employee.setAmountOfWorkTime(Long.valueOf(amountOfWorkTime)));

        if (object.getPassword().isPresent()) {
            employee.setPassword(PasswordUtils.generateBCrypt(object.getPassword().get()));
        }
    }

    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto object = new EmployeeDto();
        object.setId(employee.getId());
        object.setMail(employee.getMail());
        object.setName(employee.getName());
        employee.getAmountOfLunchTimeOpt()
            .ifPresent(amountOfLunchTime -> object.setAmountOfLunchTime(Optional.of(Float.toString(amountOfLunchTime))));

        employee.getAmountOfWorkTimeOpt()
            .ifPresent(amountOfWorktime -> object.setAmountOfWorkTime(Optional.of(Float.toString(amountOfWorktime))));

        return object;
    }
}
