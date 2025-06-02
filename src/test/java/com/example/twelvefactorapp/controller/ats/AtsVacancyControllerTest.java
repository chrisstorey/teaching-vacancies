package com.example.twelvefactorapp.controller.ats;

import com.example.twelvefactorapp.dto.AtsVacancyDto;
import com.example.twelvefactorapp.service.AtsVacancyService;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AtsVacancyController.class)
class AtsVacancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtsVacancyService atsVacancyService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testVacancyId;
    private UUID testApiClientId; // This is the hardcoded one in controller
    private AtsVacancyDto testAtsVacancyDto;

    private final String VALID_API_KEY = "TEST_API_KEY"; // From AtsVacancyController placeholder logic
    private final String INVALID_API_KEY = "INVALID_KEY";


    @BeforeEach
    void setUp() {
        testVacancyId = UUID.randomUUID();
        testApiClientId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"); // From AtsVacancyController

        testAtsVacancyDto = new AtsVacancyDto();
        testAtsVacancyDto.setId(testVacancyId);
        testAtsVacancyDto.setJobTitle("Senior Test Engineer");
        AtsVacancyDto.OrganisationSummaryDto orgSummary = new AtsVacancyDto.OrganisationSummaryDto(UUID.randomUUID(), "Test School", "URN123");
        testAtsVacancyDto.setOrganisations(Collections.singletonList(orgSummary));
    }

    // --- Tests for GET / (index) endpoint ---

    @Test
    void listClientVacancies_withValidApiKey_returnsOkAndPageOfDtos() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<AtsVacancyDto> dtoList = Collections.singletonList(testAtsVacancyDto);
        Page<AtsVacancyDto> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        when(atsVacancyService.findVacanciesByApiClient(eq(testApiClientId), any(Pageable.class)))
                .thenReturn(dtoPage);

        mockMvc.perform(get("/ats-api/v1/vacancies")
                        .header("X-Api-Key", VALID_API_KEY)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(testVacancyId.toString())))
                .andExpect(jsonPath("$.content[0].jobTitle", is("Senior Test Engineer")));

        verify(atsVacancyService).findVacanciesByApiClient(eq(testApiClientId), any(Pageable.class));
    }

    @Test
    void listClientVacancies_withMissingApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/ats-api/v1/vacancies")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Missing X-Api-Key header")));
    }

    @Test
    void listClientVacancies_withInvalidApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/ats-api/v1/vacancies")
                        .header("X-Api-Key", INVALID_API_KEY)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Invalid X-Api-Key")));
    }

    // --- Tests for GET /{id} (show) endpoint ---

    @Test
    void getClientVacancyById_whenExistsAndAuthorized_returnsOkAndDto() throws Exception {
        when(atsVacancyService.findVacancyByIdAndApiClient(testVacancyId, testApiClientId))
                .thenReturn(Optional.of(testAtsVacancyDto));

        mockMvc.perform(get("/ats-api/v1/vacancies/{id}", testVacancyId)
                        .header("X-Api-Key", VALID_API_KEY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testVacancyId.toString())))
                .andExpect(jsonPath("$.jobTitle", is("Senior Test Engineer")));

        verify(atsVacancyService).findVacancyByIdAndApiClient(testVacancyId, testApiClientId);
    }

    @Test
    void getClientVacancyById_whenNotExists_returnsNotFound() throws Exception {
        when(atsVacancyService.findVacancyByIdAndApiClient(testVacancyId, testApiClientId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/ats-api/v1/vacancies/{id}", testVacancyId)
                        .header("X-Api-Key", VALID_API_KEY))
                .andExpect(status().isNotFound());

        verify(atsVacancyService).findVacancyByIdAndApiClient(testVacancyId, testApiClientId);
    }

    @Test
    void getClientVacancyById_withMissingApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/ats-api/v1/vacancies/{id}", testVacancyId))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Missing X-Api-Key header")));
    }

    @Test
    void getClientVacancyById_withInvalidApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/ats-api/v1/vacancies/{id}", testVacancyId)
                        .header("X-Api-Key", INVALID_API_KEY))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Invalid X-Api-Key")));
    }
}
