package com.edercatini.pontointeligente.api.controllers;

import com.edercatini.pontointeligente.api.dto.PhysicalPersonRegisterDto;
import com.edercatini.pontointeligente.api.entities.Employee;
import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.enums.Profiles;
import com.edercatini.pontointeligente.api.response.Response;
import com.edercatini.pontointeligente.api.services.EmployeeService;
import com.edercatini.pontointeligente.api.services.EnterpriseService;
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
@RequestMapping("/api/physical-person-register")
@CrossOrigin(origins = "*")
public class PhysicalPersonRegisterController {

    private static final Logger log = LoggerFactory.getLogger(PhysicalPersonRegisterController.class);

    private EnterpriseService enterpriseService;
    private EmployeeService employeeService;

    @Autowired
    public PhysicalPersonRegisterController(EnterpriseService enterpriseService, EmployeeService employeeService) {
        this.enterpriseService = enterpriseService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Response<PhysicalPersonRegisterDto>> post(@Valid @RequestBody PhysicalPersonRegisterDto object, BindingResult result) throws NoSuchAlgorithmException {
        log.info("Cadastrando PF: {}", object);
        Response<PhysicalPersonRegisterDto> response = new Response<>();

        this.validate(object, result);
        Employee employee = this.convertToEmployee(object, result);

        if (result.hasErrors()) {
            log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Enterprise> empresa = enterpriseService.findByDocument(object.getCnpj());
        empresa.ifPresent(enterprise -> employee.setEnterprise(enterprise));
        employeeService.persist(employee);

        response.setData(this.convertToDto(employee));
        return ResponseEntity.ok(response);
    }

    private void validate(PhysicalPersonRegisterDto object, BindingResult result) {
        Optional<Enterprise> enterprise = enterpriseService.findByDocument(object.getCnpj());

        if (!enterprise.isPresent()) {
            result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
        }

        employeeService.findByDocument(object.getCpf())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

        employeeService.findByMail(object.getMail())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
    }

    private Employee convertToEmployee(PhysicalPersonRegisterDto object, BindingResult result) throws NoSuchAlgorithmException {
        Employee employee = new Employee();
        employee.setName(object.getName());
        employee.setMail(object.getMail());
        employee.setDocument(object.getCpf());
        employee.setProfile(Profiles.ROLE_USER);
        employee.setPassword(PasswordUtils.generateBCrypt(object.getPassword()));

        object.getAmountOfLunchHours()
                .ifPresent(amountOfLunchTime -> employee.setAmountOfLunchTime(Long.valueOf(amountOfLunchTime)));

        object.getAmountOfWorkingHours()
                .ifPresent(amountOfWorkTime -> employee.setAmountOfWorkTime(Long.valueOf(amountOfWorkTime)));

        object.getHourValue().ifPresent(hourValue -> employee.setHourValue(new BigDecimal(hourValue)));

        return employee;
    }

    private PhysicalPersonRegisterDto convertToDto(Employee employee) {
        PhysicalPersonRegisterDto object = new PhysicalPersonRegisterDto();
        object.setId(employee.getId());
        object.setName(employee.getName());
        object.setMail(employee.getMail());
        object.setCpf(employee.getDocument());
        object.setCnpj(employee.getEnterprise().getDocument());

        employee.getAmountOfLunchTimeOpt()
            .ifPresent(amountOfLunchTime -> object.setAmountOfLunchHours(Optional.of(Float.toString(amountOfLunchTime))));

        employee.getAmountOfWorkTimeOpt()
            .ifPresent(amountOfWorkingHours -> object.setAmountOfWorkingHours(Optional.of(Float.toString(amountOfWorkingHours))));

        employee.getHourValueOpt()
            .ifPresent(hourValue -> object.setHourValue(hourValue));

        return object;
    }
}