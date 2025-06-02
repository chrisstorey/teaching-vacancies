package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.service.VacancyService;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VacancyApiController.class)
class VacancyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacancyService vacancyService;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON strings if needed for request body or comparing

    private Vacancy testVacancy;
    private UUID testVacancyId;

    @BeforeEach
    void setUp() {
        testVacancyId = UUID.randomUUID();
        testVacancy = new Vacancy();
        testVacancy.setId(testVacancyId);
        testVacancy.setJobTitle("Test Job");
        testVacancy.setSlug("test-job"); // Slugs are often non-null
        // Ensure other potentially non-null fields are set if your actual Vacancy has constraints
        // For example, if using @NonNull from Lombok or other validation
        testVacancy.setCreatedAt(java.time.OffsetDateTime.now());
        testVacancy.setUpdatedAt(java.time.OffsetDateTime.now());
    }

    @Test
    void testGetVacancyById_whenExists_returnsOkAndVacancy() throws Exception {
        when(vacancyService.findVacancyById(testVacancyId)).thenReturn(Optional.of(testVacancy));

        mockMvc.perform(get("/api/v1/vacancies/{id}", testVacancyId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testVacancyId.toString()))
                .andExpect(jsonPath("$.jobTitle").value("Test Job"));

        verify(vacancyService).findVacancyById(testVacancyId);
    }

    @Test
    void testGetVacancyById_whenNotExists_returnsNotFound() throws Exception {
        when(vacancyService.findVacancyById(testVacancyId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/vacancies/{id}", testVacancyId))
                .andExpect(status().isNotFound());

        verify(vacancyService).findVacancyById(testVacancyId);
    }

    @Test
    void testListOrSearchVacancies_whenNoParams_callsFindAllAndReturnsOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<Vacancy> vacancyList = Collections.singletonList(testVacancy);
        Page<Vacancy> vacancyPage = new PageImpl<>(vacancyList, pageable, vacancyList.size());

        when(vacancyService.findAllVacancies(any(Pageable.class))).thenReturn(vacancyPage);

        mockMvc.perform(get("/api/v1/vacancies")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(testVacancyId.toString()))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(vacancyService).findAllVacancies(pageable);
    }

    @Test
    void testListOrSearchVacancies_whenKeywordParam_callsSearchVacanciesAndReturnsOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        String keyword = "developer";
        List<Vacancy> searchResultList = Collections.singletonList(testVacancy); // Assume search returns this
        Page<Vacancy> searchResultPage = new PageImpl<>(searchResultList, pageable, searchResultList.size());
        Map<String, String> expectedFilters = new HashMap<>(); // Expect empty if no filter params passed

        when(vacancyService.searchVacancies(eq(keyword), eq(null), eq(null), eq(expectedFilters), any(Pageable.class)))
                .thenReturn(searchResultPage);

        mockMvc.perform(get("/api/v1/vacancies")
                        .param("keyword", keyword)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(testVacancyId.toString()))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(vacancyService).searchVacancies(eq(keyword), eq(null), eq(null), eq(expectedFilters), any(Pageable.class));
    }

    @Test
    void testListOrSearchVacancies_whenAllParams_callsSearchVacanciesAndReturnsOk() throws Exception {
        Pageable pageable = PageRequest.of(1, 20);
        String keyword = "engineer";
        String location = "remote";
        Integer radius = 50;
        Map<String, String> filters = new HashMap<>();
        filters.put("contractType", "FULL_TIME");
        filters.put("anotherFilter", "someValue");


        List<Vacancy> searchResultList = Arrays.asList(testVacancy, new Vacancy()); // Example with two results
        Page<Vacancy> searchResultPage = new PageImpl<>(searchResultList, pageable, searchResultList.size());

        when(vacancyService.searchVacancies(eq(keyword), eq(location), eq(radius), eq(filters), any(Pageable.class)))
                .thenReturn(searchResultPage);

        mockMvc.perform(get("/api/v1/vacancies")
                        .param("keyword", keyword)
                        .param("location", location)
                        .param("radius", String.valueOf(radius))
                        .param("filters.contractType", "FULL_TIME") // How Map filters are typically passed
                        .param("filters.anotherFilter", "someValue")
                        .param("page", "1")
                        .param("size", "20")
                        .param("sort", "jobTitle,asc")) // Example of sort param
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(vacancyService).searchVacancies(eq(keyword), eq(location), eq(radius), eq(filters), any(Pageable.class));
    }
}
