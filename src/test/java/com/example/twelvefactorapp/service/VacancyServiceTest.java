package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.repository.OrganisationRepository;
import com.example.twelvefactorapp.repository.VacancyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacancyServiceTest {

    @Mock
    private VacancyRepository vacancyRepository;

    @Mock
    private OrganisationRepository organisationRepository; // Mocked even if not used in all methods

    @InjectMocks
    private VacancyService vacancyService;

    private Vacancy testVacancy;
    private UUID testVacancyId;

    // Consider adding a TestAppender for log verification if strictly needed,
    // but for now, we'll focus on return values.

    @BeforeEach
    void setUp() {
        testVacancyId = UUID.randomUUID();
        testVacancy = new Vacancy();
        testVacancy.setId(testVacancyId);
        testVacancy.setJobTitle("Test Job");
    }

    @Test
    void testFindVacancyById_whenExists_returnsVacancy() {
        when(vacancyRepository.findById(testVacancyId)).thenReturn(Optional.of(testVacancy));

        Optional<Vacancy> foundVacancy = vacancyService.findVacancyById(testVacancyId);

        assertTrue(foundVacancy.isPresent());
        assertEquals(testVacancyId, foundVacancy.get().getId());
        verify(vacancyRepository).findById(testVacancyId);
    }

    @Test
    void testFindVacancyById_whenNotExists_returnsEmptyOptional() {
        when(vacancyRepository.findById(testVacancyId)).thenReturn(Optional.empty());

        Optional<Vacancy> foundVacancy = vacancyService.findVacancyById(testVacancyId);

        assertFalse(foundVacancy.isPresent());
        verify(vacancyRepository).findById(testVacancyId);
    }

    @Test
    void testFindAllVacancies_returnsPageOfVacancies() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Vacancy> vacancyList = Collections.singletonList(testVacancy);
        Page<Vacancy> vacancyPage = new PageImpl<>(vacancyList, pageable, vacancyList.size());

        when(vacancyRepository.findAll(pageable)).thenReturn(vacancyPage);

        Page<Vacancy> resultPage = vacancyService.findAllVacancies(pageable);

        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(testVacancy.getJobTitle(), resultPage.getContent().get(0).getJobTitle());
        verify(vacancyRepository).findAll(pageable);
    }

    @Test
    void testSearchVacancies_whenCalled_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = "developer";
        String location = "london";
        Integer radius = 10;
        Map<String, String> filters = Collections.singletonMap("contractType", "PERMANENT");

        // To verify logging, you would typically use a library like Logback's ListAppender
        // or a custom TestAppender. For simplicity, we're focusing on the return type here.
        // We can also check if the logger was called if we mock it.
        // However, the actual logger is static in VacancyService, so this is a bit more involved.
        // For now, we'll just check the behavior.

        Page<Vacancy> resultPage = vacancyService.searchVacancies(keyword, location, radius, filters, pageable);

        assertNotNull(resultPage);
        assertTrue(resultPage.isEmpty());
        assertEquals(pageable, resultPage.getPageable()); // Check if the same pageable is returned for the empty page

        // No direct verification of vacancyRepository calls here as it's a placeholder,
        // but we could verify no unexpected interactions if needed.
        // verifyNoInteractions(vacancyRepository); // This would fail if findAll/findById were called
    }
}
