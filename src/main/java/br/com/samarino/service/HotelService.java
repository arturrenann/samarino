package br.com.samarino.service;

import br.com.samarino.domain.Hotel;
import br.com.samarino.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Hotel.
 */
@Service
@Transactional
public class HotelService {

    private final Logger log = LoggerFactory.getLogger(HotelService.class);

    private HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Save a hotel.
     *
     * @param hotel the entity to save
     * @return the persisted entity
     */
    public Hotel save(Hotel hotel) {
        log.debug("Request to save Hotel : {}", hotel);
        return hotelRepository.save(hotel);
    }

    /**
     * Get all the hotels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Hotel> findAll(Pageable pageable) {
        log.debug("Request to get all Hotels");
        return hotelRepository.findAll(pageable);
    }


    /**
     * Get one hotel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Hotel> findOne(Long id) {
        log.debug("Request to get Hotel : {}", id);
        return hotelRepository.findById(id);
    }

    /**
     * Delete the hotel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Hotel : {}", id);
        hotelRepository.deleteById(id);
    }
}
