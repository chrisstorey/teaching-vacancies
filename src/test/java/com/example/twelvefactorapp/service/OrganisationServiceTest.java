package com.example.twelvefactorapp.service;

import com.example.twelvefactorapp.model.Organisation;
import com.example.twelvefactorapp.repository.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @InjectMocks
    private OrganisationService organisationService;

    private Organisation testOrganisation;
    private UUID testOrganisationId;

    @BeforeEach
    void setUp() {
        testOrganisationId = UUID.randomUUID();
        testOrganisation = new Organisation();
        testOrganisation.setId(testOrganisationId);
        testOrganisation.setName("Test Organisation");
    }

    @Test
    void testFindOrganisationById_whenExists_returnsOrganisation() {
        when(organisationRepository.findById(testOrganisationId)).thenReturn(Optional.of(testOrganisation));

        Optional<Organisation> foundOrganisation = organisationService.findOrganisationById(testOrganisationId);

        assertTrue(foundOrganisation.isPresent());
        assertEquals(testOrganisationId, foundOrganisation.get().getId());
        verify(organisationRepository).findById(testOrganisationId);
    }

    @Test
    void testFindOrganisationById_whenNotExists_returnsEmptyOptional() {
        when(organisationRepository.findById(testOrganisationId)).thenReturn(Optional.empty());

        Optional<Organisation> foundOrganisation = organisationService.findOrganisationById(testOrganisationId);

        assertFalse(foundOrganisation.isPresent());
        verify(organisationRepository).findById(testOrganisationId);
    }

    @Test
    void testFindAllOrganisations_returnsPageOfOrganisations() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Organisation> organisationList = Collections.singletonList(testOrganisation);
        Page<Organisation> organisationPage = new PageImpl<>(organisationList, pageable, organisationList.size());

        when(organisationRepository.findAll(pageable)).thenReturn(organisationPage);

        Page<Organisation> resultPage = organisationService.findAllOrganisations(pageable);

        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(testOrganisation.getName(), resultPage.getContent().get(0).getName());
        verify(organisationRepository).findAll(pageable);
    }

    @Test
    void testSearchOrganisations_whenCalled_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        String name = "School";
        String location = "City";

        Page<Organisation> resultPage = organisationService.searchOrganisations(name, location, pageable);

        assertNotNull(resultPage);
        assertTrue(resultPage.isEmpty());
        assertEquals(pageable, resultPage.getPageable());
        // No direct verification of organisationRepository calls here as it's a placeholder.
    }
}
