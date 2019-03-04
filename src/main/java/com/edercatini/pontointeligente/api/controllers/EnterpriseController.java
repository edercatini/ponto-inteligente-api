package com.edercatini.pontointeligente.api.controllers;

import com.edercatini.pontointeligente.api.dto.EnterpriseDto;
import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.response.Response;
import com.edercatini.pontointeligente.api.services.EnterpriseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/enterprise")
@CrossOrigin(origins = "*")
public class EnterpriseController {
    private static final Logger log = LoggerFactory.getLogger(EnterpriseController.class);

    private EnterpriseService service;

    @Autowired
    public EnterpriseController(EnterpriseService service) {
        this.service = service;
    }

    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<Response<EnterpriseDto>> findByDocument(@PathVariable("cnpj") String cnpj) {
        log.info("Buscando empresa por CNPJ: {}", cnpj);
        Response<EnterpriseDto> response = new Response<>();
        Optional<Enterprise> enterprise = service.findByDocument(cnpj);

        if (!enterprise.isPresent()) {
            log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
            response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertToDto(enterprise.get()));
        return ResponseEntity.ok(response);
    }

    private EnterpriseDto convertToDto(Enterprise enterprise) {
        EnterpriseDto object = new EnterpriseDto();
        object.setId(enterprise.getId());
        object.setCnpj(enterprise.getDocument());
        object.setSocialName(enterprise.getSocialName());

        return object;
    }
}