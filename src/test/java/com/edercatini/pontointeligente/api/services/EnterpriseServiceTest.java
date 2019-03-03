package com.edercatini.pontointeligente.api.services;

import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.repositories.EnterpriseRepository;
import net.bytebuddy.asm.Advice;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EnterpriseServiceTest {

    @MockBean
    private EnterpriseRepository repository;

    @Autowired
    private EnterpriseService service;

    private static final String DOCUMENT = "15616161165616";

    @Before
    public void setUp() {
        given(repository.findByDocument(anyString()))
            .willReturn(new Enterprise());

        given(repository.save(any(Enterprise.class)))
            .willReturn(new Enterprise());
    }

    @Test
    public void mustFindAnEnterpriseByDocument() {
        Optional<Enterprise> enterprise = service.findByDocument(DOCUMENT);
        assertTrue(enterprise.isPresent());
    }

    @Test
    public void mustPersistAnEnterprise() {
        Enterprise enterprise = service.persist(new Enterprise());
        assertNotNull(enterprise);
    }
}