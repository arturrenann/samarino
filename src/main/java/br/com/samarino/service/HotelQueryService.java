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

import br.com.samarino.domain.Hotel;
import br.com.samarino.domain.*; // for static metamodels
import br.com.samarino.repository.HotelRepository;
import br.com.samarino.service.dto.HotelCriteria;

/**
 * Service for executing complex queries for Hotel entities in the database.
 * The main input is a {@link HotelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Hotel} or a {@link Page} of {@link Hotel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HotelQueryService extends QueryService<Hotel> {

    private final Logger log = LoggerFactory.getLogger(HotelQueryService.class);

    private HotelRepository hotelRepository;

    public HotelQueryService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Return a {@link List} of {@link Hotel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Hotel> findByCriteria(HotelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Hotel> specification = createSpecification(criteria);
        return hotelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Hotel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Hotel> findByCriteria(HotelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Hotel> specification = createSpecification(criteria);
        return hotelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HotelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Hotel> specification = createSpecification(criteria);
        return hotelRepository.count(specification);
    }

    /**
     * Function to convert HotelCriteria to a {@link Specification}
     */
    private Specification<Hotel> createSpecification(HotelCriteria criteria) {
        Specification<Hotel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Hotel_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Hotel_.nome));
            }
            if (criteria.getLocalizacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocalizacao(), Hotel_.localizacao));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Hotel_.cep));
            }
            if (criteria.getQtdAcomodacoes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQtdAcomodacoes(), Hotel_.qtdAcomodacoes));
            }
            if (criteria.getValorDiaria() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorDiaria(), Hotel_.valorDiaria));
            }
            if (criteria.getReservarId() != null) {
                specification = specification.and(buildSpecification(criteria.getReservarId(),
                    root -> root.join(Hotel_.reservars, JoinType.LEFT).get(Reservar_.id)));
            }
        }
        return specification;
    }
}
