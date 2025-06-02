package com.example.twelvefactorapp.controller;

import com.example.twelvefactorapp.model.Vacancy;
import com.example.twelvefactorapp.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vacancies")
public class VacancyApiController {

    private final VacancyService vacancyService;

    public VacancyApiController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    /**
     * Retrieves a single vacancy by its ID.
     *
     * @param id The UUID of the vacancy.
     * @return ResponseEntity containing the Vacancy if found (HTTP 200), or HTTP 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vacancy> getVacancyById(@PathVariable UUID id) {
        Optional<Vacancy> vacancy = vacancyService.findVacancyById(id);
        return vacancy.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Lists all vacancies or searches/filters vacancies based on provided parameters.
     *
     * @param keyword  Optional search keyword.
     * @param location Optional location for geospatial search.
     * @param radius   Optional radius for geospatial search.
     * @param filters  Optional map of additional filter criteria.
     * @param pageable Pagination information (page, size, sort).
     * @return ResponseEntity containing a Page of Vacancies (HTTP 200).
     */
    @GetMapping
    public ResponseEntity<Page<Vacancy>> listOrSearchVacancies(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) Map<String, String> filters,
            Pageable pageable) {

        Page<Vacancy> vacanciesPage;

        // Check if any search or filter parameters are provided
        boolean hasKeyword = StringUtils.hasText(keyword);
        boolean hasLocation = StringUtils.hasText(location);
        boolean hasFilters = filters != null && !filters.isEmpty();

        if (hasKeyword || hasLocation || hasFilters) {
            vacanciesPage = vacancyService.searchVacancies(keyword, location, radius, filters, pageable);
        } else {
            vacanciesPage = vacancyService.findAllVacancies(pageable);
        }

        return ResponseEntity.ok(vacanciesPage);
    }
}
