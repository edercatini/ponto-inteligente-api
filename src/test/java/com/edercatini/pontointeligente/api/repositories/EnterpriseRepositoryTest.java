package com.edercatini.pontointeligente.api.repositories;

import com.edercatini.pontointeligente.api.entities.Enterprise;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EnterpriseRepositoryTest {

    @Autowired
    private EnterpriseRepository repository;

    private static final String DOCUMENT = "1651651651652";

    @Before
    public void before() {
        Enterprise enterprise = new Enterprise();
        enterprise.setDocument(DOCUMENT);
        enterprise.setSocialName("Teste");
        repository.saveAll(asList(enterprise));
    }

    @After
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void mustReturnAnEnterprise() {
        Enterprise enterprise = repository.findByDocument(DOCUMENT);
        assertEquals(DOCUMENT, enterprise.getDocument());
    }
}