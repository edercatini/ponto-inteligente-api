package com.edercatini.pontointeligente.api.services.impl;

import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.repositories.EnterpriseRepository;
import com.edercatini.pontointeligente.api.services.EnterpriseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseService.class);

    private EnterpriseRepository repository;

    @Autowired
    public EnterpriseServiceImpl(EnterpriseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Enterprise> findByDocument(String document) {
        logger.info("Busca de empresa por CNPJ {}", document);
        return Optional.ofNullable(repository.findByDocument(document));
    }

    @Override
    public Enterprise persist(Enterprise enterprise) {
        logger.info("Persistindo empresa na base de dados: {}", enterprise);
        return repository.save(enterprise);
    }
}