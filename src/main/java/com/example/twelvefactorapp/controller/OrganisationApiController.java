package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.model.Organisation;
import com.example.twelvefactorapp.service.OrganisationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organisations")
public class OrganisationApiController {

    private final OrganisationService organisationService;

    public OrganisationApiController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    /**
     * Retrieves a single organisation by its ID.
     *
     * @param id The UUID of the organisation.
     * @return ResponseEntity containing the Organisation if found (HTTP 200), or HTTP 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Organisation> getOrganisationById(@PathVariable UUID id) {
        Optional<Organisation> organisation = organisationService.findOrganisationById(id);
        return organisation.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Lists all organisations or searches organisations based on provided parameters.
     *
     * @param name     Optional name of the organisation to search for.
     * @param location Optional location string for geospatial search.
     * @param pageable Pagination information (page, size, sort).
     * @return ResponseEntity containing a Page of Organisations (HTTP 200).
     */
    @GetMapping
    public ResponseEntity<Page<Organisation>> listOrSearchOrganisations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            Pageable pageable) {

        Page<Organisation> organisationsPage;

        boolean hasName = StringUtils.hasText(name);
        boolean hasLocation = StringUtils.hasText(location);

        if (hasName || hasLocation) {
            organisationsPage = organisationService.searchOrganisations(name, location, pageable);
        } else {
            organisationsPage = organisationService.findAllOrganisations(pageable);
        }

        return ResponseEntity.ok(organisationsPage);
    }
}
