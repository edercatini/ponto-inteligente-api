package com.edercatini.pontointeligente.api.services;

import com.edercatini.pontointeligente.api.entities.Enterprise;

import java.util.Optional;

public interface EnterpriseService {

    /**
     * Retorna uma empresa dado um CNPJ
     * @param document
     * @return
     */
    Optional<Enterprise> findByDocument(String document);

    /**
     * Persiste uma empresa na Base de Dados
     * @param enterprise
     * @return
     */
    Enterprise persist(Enterprise enterprise);
}