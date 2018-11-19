package br.com.samarino.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.samarino.domain.Reservar;
import br.com.samarino.service.ReservarService;
import br.com.samarino.web.rest.errors.BadRequestAlertException;
import br.com.samarino.web.rest.util.HeaderUtil;
import br.com.samarino.service.dto.ReservarCriteria;
import br.com.samarino.service.ReservarQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Reservar.
 */
@RestController
@RequestMapping("/api")
public class ReservarResource {

    private final Logger log = LoggerFactory.getLogger(ReservarResource.class);

    private static final String ENTITY_NAME = "reservar";

    private ReservarService reservarService;

    private ReservarQueryService reservarQueryService;

    public ReservarResource(ReservarService reservarService, ReservarQueryService reservarQueryService) {
        this.reservarService = reservarService;
        this.reservarQueryService = reservarQueryService;
    }

    /**
     * POST  /reservars : Create a new reservar.
     *
     * @param reservar the reservar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reservar, or with status 400 (Bad Request) if the reservar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reservars")
    @Timed
    public ResponseEntity<Reservar> createReservar(@RequestBody Reservar reservar) throws URISyntaxException {
        log.debug("REST request to save Reservar : {}", reservar);
        if (reservar.getId() != null) {
            throw new BadRequestAlertException("A new reservar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reservar result = reservarService.save(reservar);
        return ResponseEntity.created(new URI("/api/reservars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reservars : Updates an existing reservar.
     *
     * @param reservar the reservar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reservar,
     * or with status 400 (Bad Request) if the reservar is not valid,
     * or with status 500 (Internal Server Error) if the reservar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reservars")
    @Timed
    public ResponseEntity<Reservar> updateReservar(@RequestBody Reservar reservar) throws URISyntaxException {
        log.debug("REST request to update Reservar : {}", reservar);
        if (reservar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Reservar result = reservarService.save(reservar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reservar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reservars : get all the reservars.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of reservars in body
     */
    @GetMapping("/reservars")
    @Timed
    public ResponseEntity<List<Reservar>> getAllReservars(ReservarCriteria criteria) {
        log.debug("REST request to get Reservars by criteria: {}", criteria);
        List<Reservar> entityList = reservarQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /reservars/count : count all the reservars.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/reservars/count")
    @Timed
    public ResponseEntity<Long> countReservars(ReservarCriteria criteria) {
        log.debug("REST request to count Reservars by criteria: {}", criteria);
        return ResponseEntity.ok().body(reservarQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /reservars/:id : get the "id" reservar.
     *
     * @param id the id of the reservar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reservar, or with status 404 (Not Found)
     */
    @GetMapping("/reservars/{id}")
    @Timed
    public ResponseEntity<Reservar> getReservar(@PathVariable Long id) {
        log.debug("REST request to get Reservar : {}", id);
        Optional<Reservar> reservar = reservarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservar);
    }

    /**
     * DELETE  /reservars/:id : delete the "id" reservar.
     *
     * @param id the id of the reservar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reservars/{id}")
    @Timed
    public ResponseEntity<Void> deleteReservar(@PathVariable Long id) {
        log.debug("REST request to delete Reservar : {}", id);
        reservarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
