package br.com.samarino.service;

import br.com.samarino.domain.Reservar;
import br.com.samarino.repository.ReservarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Reservar.
 */
@Service
@Transactional
public class ReservarService {

    private final Logger log = LoggerFactory.getLogger(ReservarService.class);

    private ReservarRepository reservarRepository;

    public ReservarService(ReservarRepository reservarRepository) {
        this.reservarRepository = reservarRepository;
    }

    /**
     * Save a reservar.
     *
     * @param reservar the entity to save
     * @return the persisted entity
     */
    public Reservar save(Reservar reservar) {
        log.debug("Request to save Reservar : {}", reservar);
        return reservarRepository.save(reservar);
    }

    /**
     * Get all the reservars.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Reservar> findAll() {
        log.debug("Request to get all Reservars");
        return reservarRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the Reservar with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Reservar> findAllWithEagerRelationships(Pageable pageable) {
        return reservarRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one reservar by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Reservar> findOne(Long id) {
        log.debug("Request to get Reservar : {}", id);
        return reservarRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the reservar by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Reservar : {}", id);
        reservarRepository.deleteById(id);
    }
}
