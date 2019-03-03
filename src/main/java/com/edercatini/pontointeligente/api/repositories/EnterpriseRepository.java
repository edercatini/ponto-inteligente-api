package com.edercatini.pontointeligente.api.repositories;

import com.edercatini.pontointeligente.api.entities.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

    @Transactional(readOnly = true)
    Enterprise findByDocument(String document);
}
