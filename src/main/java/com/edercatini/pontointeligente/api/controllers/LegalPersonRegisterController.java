package com.edercatini.pontointeligente.api.controllers;

import com.edercatini.pontointeligente.api.dto.LegalPersonRegisterDto;
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
import java.security.NoSuchAlgorithmException;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/legal-person-register")
@CrossOrigin(origins = "*")
public class LegalPersonRegisterController {

    private static final Logger log = LoggerFactory.getLogger(LegalPersonRegisterController.class);

    private EmployeeService employeeService;
    private EnterpriseService enterpriseService;

    @Autowired
    public LegalPersonRegisterController(EmployeeService employeeService, EnterpriseService enterpriseService) {
        this.employeeService = employeeService;
        this.enterpriseService = enterpriseService;
    }

    @PostMapping
    public ResponseEntity<Response<LegalPersonRegisterDto>> post(@Valid @RequestBody LegalPersonRegisterDto object, BindingResult result) throws NoSuchAlgorithmException {
        log.info("Cadastrando PJ: {}", object);
        Response<LegalPersonRegisterDto> response = new Response<>();

        enterpriseService.findByDocument(object.getCnpj())
                .ifPresent(error -> result.addError(new ObjectError("empresa", "Empresa já existente")));

        employeeService.findByDocumentOrMail(object.getCpf(), object.getMail())
                .ifPresent(error -> result.addError(new ObjectError("funcionário", "Funcionário já existente")));

        Enterprise enterprise = this.convertDtoToEnterprise(object);
        Employee employee = this.convertDtoToEmployee(object, result);

        if (result.hasErrors()) {
            log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return badRequest().body(response);
        }

        enterpriseService.persist(enterprise);
        employee.setEnterprise(enterprise);
        this.employeeService.persist(employee);

        response.setData(this.convertDataToDto(employee));
        return ok(response);
    }

    private Enterprise convertDtoToEnterprise(LegalPersonRegisterDto object) {
        Enterprise enterprise = new Enterprise();
        enterprise.setDocument(object.getCnpj());
        enterprise.setSocialName(object.getSocialName());

        return enterprise;
    }

    private Employee convertDtoToEmployee(LegalPersonRegisterDto object, BindingResult result) throws NoSuchAlgorithmException {
        Employee employee = new Employee();
        employee.setName(object.getName());
        employee.setMail(object.getMail());
        employee.setDocument(object.getCpf());
        employee.setProfile(Profiles.ROLE_ADMIN);
        employee.setPassword(PasswordUtils.generateBCrypt(object.getPassword()));

        return employee;
    }

    private LegalPersonRegisterDto convertDataToDto(Employee employee) {
        LegalPersonRegisterDto object = new LegalPersonRegisterDto();
        object.setId(employee.getId());
        object.setName(employee.getName());
        object.setMail(employee.getMail());
        object.setCpf(employee.getDocument());
        object.setSocialName(employee.getEnterprise().getSocialName());
        object.setCnpj(employee.getEnterprise().getDocument());

        return object;
    }
}