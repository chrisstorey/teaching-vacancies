package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.model.Organisation;
import com.example.twelvefactorapp.service.OrganisationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganisationApiController.class)
class OrganisationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganisationService organisationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Organisation testOrganisation;
    private UUID testOrganisationId;

    @BeforeEach
    void setUp() {
        testOrganisationId = UUID.randomUUID();
        testOrganisation = new Organisation();
        testOrganisation.setId(testOrganisationId);
        testOrganisation.setName("Test School");
        // Set other necessary fields if they are non-null or part of JSON response
        testOrganisation.setCreatedAt(OffsetDateTime.now());
        testOrganisation.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void testGetOrganisationById_whenExists_returnsOkAndOrganisation() throws Exception {
        when(organisationService.findOrganisationById(testOrganisationId)).thenReturn(Optional.of(testOrganisation));

        mockMvc.perform(get("/api/v1/organisations/{id}", testOrganisationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testOrganisationId.toString()))
                .andExpect(jsonPath("$.name").value("Test School"));

        verify(organisationService).findOrganisationById(testOrganisationId);
    }

    @Test
    void testGetOrganisationById_whenNotExists_returnsNotFound() throws Exception {
        when(organisationService.findOrganisationById(testOrganisationId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/organisations/{id}", testOrganisationId))
                .andExpect(status().isNotFound());

        verify(organisationService).findOrganisationById(testOrganisationId);
    }

    @Test
    void testListOrSearchOrganisations_whenNoParams_callsFindAllAndReturnsOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        List<Organisation> organisationList = Collections.singletonList(testOrganisation);
        Page<Organisation> organisationPage = new PageImpl<>(organisationList, pageable, organisationList.size());

        when(organisationService.findAllOrganisations(any(Pageable.class))).thenReturn(organisationPage);

        mockMvc.perform(get("/api/v1/organisations")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(testOrganisationId.toString()))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(organisationService).findAllOrganisations(pageable);
    }

    @Test
    void testListOrSearchOrganisations_whenNameParam_callsSearchOrganisationsAndReturnsOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 15);
        String nameParam = "TestSchool";
        List<Organisation> searchResultList = Collections.singletonList(testOrganisation);
        Page<Organisation> searchResultPage = new PageImpl<>(searchResultList, pageable, searchResultList.size());

        when(organisationService.searchOrganisations(eq(nameParam), eq(null), any(Pageable.class)))
                .thenReturn(searchResultPage);

        mockMvc.perform(get("/api/v1/organisations")
                        .param("name", nameParam)
                        .param("page", "0")
                        .param("size", "15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(testOrganisationId.toString()))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(organisationService).searchOrganisations(eq(nameParam), eq(null), any(Pageable.class));
    }

    @Test
    void testListOrSearchOrganisations_whenLocationParam_callsSearchOrganisationsAndReturnsOk() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        String locationParam = "London";
        List<Organisation> searchResultList = Collections.singletonList(testOrganisation);
        Page<Organisation> searchResultPage = new PageImpl<>(searchResultList, pageable, searchResultList.size());

        when(organisationService.searchOrganisations(eq(null), eq(locationParam), any(Pageable.class)))
                .thenReturn(searchResultPage);

        mockMvc.perform(get("/api/v1/organisations")
                        .param("location", locationParam)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(testOrganisationId.toString()))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(organisationService).searchOrganisations(eq(null), eq(locationParam), any(Pageable.class));
    }

     @Test
    void testListOrSearchOrganisations_whenAllParams_callsSearchOrganisationsAndReturnsOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        String nameParam = "Another School";
        String locationParam = "Manchester";
        List<Organisation> searchResultList = Collections.singletonList(testOrganisation);
        Page<Organisation> searchResultPage = new PageImpl<>(searchResultList, pageable, searchResultList.size());

        when(organisationService.searchOrganisations(eq(nameParam), eq(locationParam), any(Pageable.class)))
                .thenReturn(searchResultPage);

        mockMvc.perform(get("/api/v1/organisations")
                        .param("name", nameParam)
                        .param("location", locationParam)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(testOrganisationId.toString()))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(organisationService).searchOrganisations(eq(nameParam), eq(locationParam), any(Pageable.class));
    }
}
