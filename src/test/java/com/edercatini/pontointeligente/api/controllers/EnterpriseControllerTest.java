package com.edercatini.pontointeligente.api.controllers;

import com.edercatini.pontointeligente.api.entities.Enterprise;
import com.edercatini.pontointeligente.api.services.EnterpriseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EnterpriseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EnterpriseService service;

    private static final String CNPJ_URL = "/api/enterprise/cnpj/";
    private static final Long ID = Long.valueOf(1);
    private static final String CNPJ = "51463645000100";
    private static final String SOCIAL_NAME = "Empresa XYZ";

    @Test
    @WithMockUser
    public void invalidDocumentForEnterprise() throws Exception {
        BDDMockito.given(service.findByDocument(Mockito.anyString())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get(CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors").value("Empresa não encontrada para o CNPJ " + CNPJ));
    }

    @Test
    @WithMockUser
    public void mustFindAnEnterprise() throws Exception {
        BDDMockito.given(service.findByDocument(Mockito.anyString()))
                .willReturn(Optional.of(this.getEnterpriseData()));

        mvc.perform(MockMvcRequestBuilders.get(CNPJ_URL + CNPJ)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(ID))
            .andExpect(jsonPath("$.data.socialName", equalTo(SOCIAL_NAME)))
            .andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
            .andExpect(jsonPath("$.errors").isEmpty());
    }

    private Enterprise getEnterpriseData() {
        Enterprise enterprise = new Enterprise();
        enterprise.setId(ID);
        enterprise.setSocialName(SOCIAL_NAME);
        enterprise.setDocument(CNPJ);
        return enterprise;
    }
}