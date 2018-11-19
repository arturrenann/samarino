package br.com.samarino.web.rest;

import br.com.samarino.TrabalhoEsApp;

import br.com.samarino.domain.Hotel;
import br.com.samarino.domain.Reservar;
import br.com.samarino.repository.HotelRepository;
import br.com.samarino.service.HotelService;
import br.com.samarino.web.rest.errors.ExceptionTranslator;
import br.com.samarino.service.dto.HotelCriteria;
import br.com.samarino.service.HotelQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static br.com.samarino.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HotelResource REST controller.
 *
 * @see HotelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrabalhoEsApp.class)
public class HotelResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIZACAO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final Integer DEFAULT_QTD_ACOMODACOES = 1;
    private static final Integer UPDATED_QTD_ACOMODACOES = 2;

    private static final Integer DEFAULT_VALOR_DIARIA = 1;
    private static final Integer UPDATED_VALOR_DIARIA = 2;

    @Autowired
    private HotelRepository hotelRepository;
    
    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelQueryService hotelQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHotelMockMvc;

    private Hotel hotel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HotelResource hotelResource = new HotelResource(hotelService, hotelQueryService);
        this.restHotelMockMvc = MockMvcBuilders.standaloneSetup(hotelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hotel createEntity(EntityManager em) {
        Hotel hotel = new Hotel()
            .nome(DEFAULT_NOME)
            .localizacao(DEFAULT_LOCALIZACAO)
            .cep(DEFAULT_CEP)
            .qtdAcomodacoes(DEFAULT_QTD_ACOMODACOES)
            .valorDiaria(DEFAULT_VALOR_DIARIA);
        return hotel;
    }

    @Before
    public void initTest() {
        hotel = createEntity(em);
    }

    @Test
    @Transactional
    public void createHotel() throws Exception {
        int databaseSizeBeforeCreate = hotelRepository.findAll().size();

        // Create the Hotel
        restHotelMockMvc.perform(post("/api/hotels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isCreated());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeCreate + 1);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testHotel.getLocalizacao()).isEqualTo(DEFAULT_LOCALIZACAO);
        assertThat(testHotel.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testHotel.getQtdAcomodacoes()).isEqualTo(DEFAULT_QTD_ACOMODACOES);
        assertThat(testHotel.getValorDiaria()).isEqualTo(DEFAULT_VALOR_DIARIA);
    }

    @Test
    @Transactional
    public void createHotelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hotelRepository.findAll().size();

        // Create the Hotel with an existing ID
        hotel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotelMockMvc.perform(post("/api/hotels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelRepository.findAll().size();
        // set the field null
        hotel.setNome(null);

        // Create the Hotel, which fails.

        restHotelMockMvc.perform(post("/api/hotels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isBadRequest());

        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHotels() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList
        restHotelMockMvc.perform(get("/api/hotels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].localizacao").value(hasItem(DEFAULT_LOCALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].qtdAcomodacoes").value(hasItem(DEFAULT_QTD_ACOMODACOES)))
            .andExpect(jsonPath("$.[*].valorDiaria").value(hasItem(DEFAULT_VALOR_DIARIA)));
    }
    
    @Test
    @Transactional
    public void getHotel() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get the hotel
        restHotelMockMvc.perform(get("/api/hotels/{id}", hotel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hotel.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.localizacao").value(DEFAULT_LOCALIZACAO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.qtdAcomodacoes").value(DEFAULT_QTD_ACOMODACOES))
            .andExpect(jsonPath("$.valorDiaria").value(DEFAULT_VALOR_DIARIA));
    }

    @Test
    @Transactional
    public void getAllHotelsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where nome equals to DEFAULT_NOME
        defaultHotelShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the hotelList where nome equals to UPDATED_NOME
        defaultHotelShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllHotelsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultHotelShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the hotelList where nome equals to UPDATED_NOME
        defaultHotelShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllHotelsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where nome is not null
        defaultHotelShouldBeFound("nome.specified=true");

        // Get all the hotelList where nome is null
        defaultHotelShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllHotelsByLocalizacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where localizacao equals to DEFAULT_LOCALIZACAO
        defaultHotelShouldBeFound("localizacao.equals=" + DEFAULT_LOCALIZACAO);

        // Get all the hotelList where localizacao equals to UPDATED_LOCALIZACAO
        defaultHotelShouldNotBeFound("localizacao.equals=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    public void getAllHotelsByLocalizacaoIsInShouldWork() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where localizacao in DEFAULT_LOCALIZACAO or UPDATED_LOCALIZACAO
        defaultHotelShouldBeFound("localizacao.in=" + DEFAULT_LOCALIZACAO + "," + UPDATED_LOCALIZACAO);

        // Get all the hotelList where localizacao equals to UPDATED_LOCALIZACAO
        defaultHotelShouldNotBeFound("localizacao.in=" + UPDATED_LOCALIZACAO);
    }

    @Test
    @Transactional
    public void getAllHotelsByLocalizacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where localizacao is not null
        defaultHotelShouldBeFound("localizacao.specified=true");

        // Get all the hotelList where localizacao is null
        defaultHotelShouldNotBeFound("localizacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllHotelsByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cep equals to DEFAULT_CEP
        defaultHotelShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the hotelList where cep equals to UPDATED_CEP
        defaultHotelShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllHotelsByCepIsInShouldWork() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultHotelShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the hotelList where cep equals to UPDATED_CEP
        defaultHotelShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllHotelsByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where cep is not null
        defaultHotelShouldBeFound("cep.specified=true");

        // Get all the hotelList where cep is null
        defaultHotelShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    public void getAllHotelsByQtdAcomodacoesIsEqualToSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where qtdAcomodacoes equals to DEFAULT_QTD_ACOMODACOES
        defaultHotelShouldBeFound("qtdAcomodacoes.equals=" + DEFAULT_QTD_ACOMODACOES);

        // Get all the hotelList where qtdAcomodacoes equals to UPDATED_QTD_ACOMODACOES
        defaultHotelShouldNotBeFound("qtdAcomodacoes.equals=" + UPDATED_QTD_ACOMODACOES);
    }

    @Test
    @Transactional
    public void getAllHotelsByQtdAcomodacoesIsInShouldWork() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where qtdAcomodacoes in DEFAULT_QTD_ACOMODACOES or UPDATED_QTD_ACOMODACOES
        defaultHotelShouldBeFound("qtdAcomodacoes.in=" + DEFAULT_QTD_ACOMODACOES + "," + UPDATED_QTD_ACOMODACOES);

        // Get all the hotelList where qtdAcomodacoes equals to UPDATED_QTD_ACOMODACOES
        defaultHotelShouldNotBeFound("qtdAcomodacoes.in=" + UPDATED_QTD_ACOMODACOES);
    }

    @Test
    @Transactional
    public void getAllHotelsByQtdAcomodacoesIsNullOrNotNull() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where qtdAcomodacoes is not null
        defaultHotelShouldBeFound("qtdAcomodacoes.specified=true");

        // Get all the hotelList where qtdAcomodacoes is null
        defaultHotelShouldNotBeFound("qtdAcomodacoes.specified=false");
    }

    @Test
    @Transactional
    public void getAllHotelsByQtdAcomodacoesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where qtdAcomodacoes greater than or equals to DEFAULT_QTD_ACOMODACOES
        defaultHotelShouldBeFound("qtdAcomodacoes.greaterOrEqualThan=" + DEFAULT_QTD_ACOMODACOES);

        // Get all the hotelList where qtdAcomodacoes greater than or equals to UPDATED_QTD_ACOMODACOES
        defaultHotelShouldNotBeFound("qtdAcomodacoes.greaterOrEqualThan=" + UPDATED_QTD_ACOMODACOES);
    }

    @Test
    @Transactional
    public void getAllHotelsByQtdAcomodacoesIsLessThanSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where qtdAcomodacoes less than or equals to DEFAULT_QTD_ACOMODACOES
        defaultHotelShouldNotBeFound("qtdAcomodacoes.lessThan=" + DEFAULT_QTD_ACOMODACOES);

        // Get all the hotelList where qtdAcomodacoes less than or equals to UPDATED_QTD_ACOMODACOES
        defaultHotelShouldBeFound("qtdAcomodacoes.lessThan=" + UPDATED_QTD_ACOMODACOES);
    }


    @Test
    @Transactional
    public void getAllHotelsByValorDiariaIsEqualToSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where valorDiaria equals to DEFAULT_VALOR_DIARIA
        defaultHotelShouldBeFound("valorDiaria.equals=" + DEFAULT_VALOR_DIARIA);

        // Get all the hotelList where valorDiaria equals to UPDATED_VALOR_DIARIA
        defaultHotelShouldNotBeFound("valorDiaria.equals=" + UPDATED_VALOR_DIARIA);
    }

    @Test
    @Transactional
    public void getAllHotelsByValorDiariaIsInShouldWork() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where valorDiaria in DEFAULT_VALOR_DIARIA or UPDATED_VALOR_DIARIA
        defaultHotelShouldBeFound("valorDiaria.in=" + DEFAULT_VALOR_DIARIA + "," + UPDATED_VALOR_DIARIA);

        // Get all the hotelList where valorDiaria equals to UPDATED_VALOR_DIARIA
        defaultHotelShouldNotBeFound("valorDiaria.in=" + UPDATED_VALOR_DIARIA);
    }

    @Test
    @Transactional
    public void getAllHotelsByValorDiariaIsNullOrNotNull() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where valorDiaria is not null
        defaultHotelShouldBeFound("valorDiaria.specified=true");

        // Get all the hotelList where valorDiaria is null
        defaultHotelShouldNotBeFound("valorDiaria.specified=false");
    }

    @Test
    @Transactional
    public void getAllHotelsByValorDiariaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where valorDiaria greater than or equals to DEFAULT_VALOR_DIARIA
        defaultHotelShouldBeFound("valorDiaria.greaterOrEqualThan=" + DEFAULT_VALOR_DIARIA);

        // Get all the hotelList where valorDiaria greater than or equals to UPDATED_VALOR_DIARIA
        defaultHotelShouldNotBeFound("valorDiaria.greaterOrEqualThan=" + UPDATED_VALOR_DIARIA);
    }

    @Test
    @Transactional
    public void getAllHotelsByValorDiariaIsLessThanSomething() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList where valorDiaria less than or equals to DEFAULT_VALOR_DIARIA
        defaultHotelShouldNotBeFound("valorDiaria.lessThan=" + DEFAULT_VALOR_DIARIA);

        // Get all the hotelList where valorDiaria less than or equals to UPDATED_VALOR_DIARIA
        defaultHotelShouldBeFound("valorDiaria.lessThan=" + UPDATED_VALOR_DIARIA);
    }


    @Test
    @Transactional
    public void getAllHotelsByReservarIsEqualToSomething() throws Exception {
        // Initialize the database
        Reservar reservar = ReservarResourceIntTest.createEntity(em);
        em.persist(reservar);
        em.flush();
        hotel.addReservar(reservar);
        hotelRepository.saveAndFlush(hotel);
        Long reservarId = reservar.getId();

        // Get all the hotelList where reservar equals to reservarId
        defaultHotelShouldBeFound("reservarId.equals=" + reservarId);

        // Get all the hotelList where reservar equals to reservarId + 1
        defaultHotelShouldNotBeFound("reservarId.equals=" + (reservarId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHotelShouldBeFound(String filter) throws Exception {
        restHotelMockMvc.perform(get("/api/hotels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].localizacao").value(hasItem(DEFAULT_LOCALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].qtdAcomodacoes").value(hasItem(DEFAULT_QTD_ACOMODACOES)))
            .andExpect(jsonPath("$.[*].valorDiaria").value(hasItem(DEFAULT_VALOR_DIARIA)));

        // Check, that the count call also returns 1
        restHotelMockMvc.perform(get("/api/hotels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHotelShouldNotBeFound(String filter) throws Exception {
        restHotelMockMvc.perform(get("/api/hotels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHotelMockMvc.perform(get("/api/hotels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHotel() throws Exception {
        // Get the hotel
        restHotelMockMvc.perform(get("/api/hotels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHotel() throws Exception {
        // Initialize the database
        hotelService.save(hotel);

        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();

        // Update the hotel
        Hotel updatedHotel = hotelRepository.findById(hotel.getId()).get();
        // Disconnect from session so that the updates on updatedHotel are not directly saved in db
        em.detach(updatedHotel);
        updatedHotel
            .nome(UPDATED_NOME)
            .localizacao(UPDATED_LOCALIZACAO)
            .cep(UPDATED_CEP)
            .qtdAcomodacoes(UPDATED_QTD_ACOMODACOES)
            .valorDiaria(UPDATED_VALOR_DIARIA);

        restHotelMockMvc.perform(put("/api/hotels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHotel)))
            .andExpect(status().isOk());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHotel.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testHotel.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testHotel.getQtdAcomodacoes()).isEqualTo(UPDATED_QTD_ACOMODACOES);
        assertThat(testHotel.getValorDiaria()).isEqualTo(UPDATED_VALOR_DIARIA);
    }

    @Test
    @Transactional
    public void updateNonExistingHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();

        // Create the Hotel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotelMockMvc.perform(put("/api/hotels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHotel() throws Exception {
        // Initialize the database
        hotelService.save(hotel);

        int databaseSizeBeforeDelete = hotelRepository.findAll().size();

        // Get the hotel
        restHotelMockMvc.perform(delete("/api/hotels/{id}", hotel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hotel.class);
        Hotel hotel1 = new Hotel();
        hotel1.setId(1L);
        Hotel hotel2 = new Hotel();
        hotel2.setId(hotel1.getId());
        assertThat(hotel1).isEqualTo(hotel2);
        hotel2.setId(2L);
        assertThat(hotel1).isNotEqualTo(hotel2);
        hotel1.setId(null);
        assertThat(hotel1).isNotEqualTo(hotel2);
    }
}
