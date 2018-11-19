package br.com.samarino.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.com.samarino.domain.Reservar;
import br.com.samarino.domain.*; // for static metamodels
import br.com.samarino.repository.ReservarRepository;
import br.com.samarino.service.dto.ReservarCriteria;

/**
 * Service for executing complex queries for Reservar entities in the database.
 * The main input is a {@link ReservarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Reservar} or a {@link Page} of {@link Reservar} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReservarQueryService extends QueryService<Reservar> {

    private final Logger log = LoggerFactory.getLogger(ReservarQueryService.class);

    private ReservarRepository reservarRepository;

    public ReservarQueryService(ReservarRepository reservarRepository) {
        this.reservarRepository = reservarRepository;
    }

    /**
     * Return a {@link List} of {@link Reservar} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Reservar> findByCriteria(ReservarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reservar> specification = createSpecification(criteria);
        return reservarRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Reservar} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservar> findByCriteria(ReservarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reservar> specification = createSpecification(criteria);
        return reservarRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReservarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reservar> specification = createSpecification(criteria);
        return reservarRepository.count(specification);
    }

    /**
     * Function to convert ReservarCriteria to a {@link Specification}
     */
    private Specification<Reservar> createSpecification(ReservarCriteria criteria) {
        Specification<Reservar> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Reservar_.id));
            }
            if (criteria.getDias() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDias(), Reservar_.dias));
            }
            if (criteria.getDataInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInicio(), Reservar_.dataInicio));
            }
            if (criteria.getDataEntrega() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataEntrega(), Reservar_.dataEntrega));
            }
            if (criteria.getValorTotReserva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorTotReserva(), Reservar_.valorTotReserva));
            }
            if (criteria.getHoteisId() != null) {
                specification = specification.and(buildSpecification(criteria.getHoteisId(),
                    root -> root.join(Reservar_.hoteis, JoinType.LEFT).get(Hotel_.id)));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(buildSpecification(criteria.getClienteId(),
                    root -> root.join(Reservar_.clientes, JoinType.LEFT).get(Cliente_.id)));
            }
        }
        return specification;
    }
}
