package br.com.samarino.web.rest;

import br.com.samarino.TrabalhoEsApp;

import br.com.samarino.domain.Reservar;
import br.com.samarino.domain.Hotel;
import br.com.samarino.domain.Cliente;
import br.com.samarino.repository.ReservarRepository;
import br.com.samarino.service.ReservarService;
import br.com.samarino.web.rest.errors.ExceptionTranslator;
import br.com.samarino.service.dto.ReservarCriteria;
import br.com.samarino.service.ReservarQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static br.com.samarino.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReservarResource REST controller.
 *
 * @see ReservarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrabalhoEsApp.class)
public class ReservarResourceIntTest {

    private static final Integer DEFAULT_DIAS = 1;
    private static final Integer UPDATED_DIAS = 2;

    private static final LocalDate DEFAULT_DATA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VALOR_TOT_RESERVA = 1;
    private static final Integer UPDATED_VALOR_TOT_RESERVA = 2;

    @Autowired
    private ReservarRepository reservarRepository;

    @Mock
    private ReservarRepository reservarRepositoryMock;
    

    @Mock
    private ReservarService reservarServiceMock;

    @Autowired
    private ReservarService reservarService;

    @Autowired
    private ReservarQueryService reservarQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReservarMockMvc;

    private Reservar reservar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservarResource reservarResource = new ReservarResource(reservarService, reservarQueryService);
        this.restReservarMockMvc = MockMvcBuilders.standaloneSetup(reservarResource)
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
    public static Reservar createEntity(EntityManager em) {
        Reservar reservar = new Reservar()
            .dias(DEFAULT_DIAS)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataEntrega(DEFAULT_DATA_ENTREGA)
            .valorTotReserva(DEFAULT_VALOR_TOT_RESERVA);
        return reservar;
    }

    @Before
    public void initTest() {
        reservar = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservar() throws Exception {
        int databaseSizeBeforeCreate = reservarRepository.findAll().size();

        // Create the Reservar
        restReservarMockMvc.perform(post("/api/reservars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservar)))
            .andExpect(status().isCreated());

        // Validate the Reservar in the database
        List<Reservar> reservarList = reservarRepository.findAll();
        assertThat(reservarList).hasSize(databaseSizeBeforeCreate + 1);
        Reservar testReservar = reservarList.get(reservarList.size() - 1);
        assertThat(testReservar.getDias()).isEqualTo(DEFAULT_DIAS);
        assertThat(testReservar.getDataInicio()).isEqualTo(DEFAULT_DATA_INICIO);
        assertThat(testReservar.getDataEntrega()).isEqualTo(DEFAULT_DATA_ENTREGA);
        assertThat(testReservar.getValorTotReserva()).isEqualTo(DEFAULT_VALOR_TOT_RESERVA);
    }

    @Test
    @Transactional
    public void createReservarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservarRepository.findAll().size();

        // Create the Reservar with an existing ID
        reservar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservarMockMvc.perform(post("/api/reservars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservar)))
            .andExpect(status().isBadRequest());

        // Validate the Reservar in the database
        List<Reservar> reservarList = reservarRepository.findAll();
        assertThat(reservarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReservars() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList
        restReservarMockMvc.perform(get("/api/reservars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservar.getId().intValue())))
            .andExpect(jsonPath("$.[*].dias").value(hasItem(DEFAULT_DIAS)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataEntrega").value(hasItem(DEFAULT_DATA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].valorTotReserva").value(hasItem(DEFAULT_VALOR_TOT_RESERVA)));
    }
    
    public void getAllReservarsWithEagerRelationshipsIsEnabled() throws Exception {
        ReservarResource reservarResource = new ReservarResource(reservarServiceMock, reservarQueryService);
        when(reservarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restReservarMockMvc = MockMvcBuilders.standaloneSetup(reservarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restReservarMockMvc.perform(get("/api/reservars?eagerload=true"))
        .andExpect(status().isOk());

        verify(reservarServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllReservarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ReservarResource reservarResource = new ReservarResource(reservarServiceMock, reservarQueryService);
            when(reservarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restReservarMockMvc = MockMvcBuilders.standaloneSetup(reservarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restReservarMockMvc.perform(get("/api/reservars?eagerload=true"))
        .andExpect(status().isOk());

            verify(reservarServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getReservar() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get the reservar
        restReservarMockMvc.perform(get("/api/reservars/{id}", reservar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservar.getId().intValue()))
            .andExpect(jsonPath("$.dias").value(DEFAULT_DIAS))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataEntrega").value(DEFAULT_DATA_ENTREGA.toString()))
            .andExpect(jsonPath("$.valorTotReserva").value(DEFAULT_VALOR_TOT_RESERVA));
    }

    @Test
    @Transactional
    public void getAllReservarsByDiasIsEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dias equals to DEFAULT_DIAS
        defaultReservarShouldBeFound("dias.equals=" + DEFAULT_DIAS);

        // Get all the reservarList where dias equals to UPDATED_DIAS
        defaultReservarShouldNotBeFound("dias.equals=" + UPDATED_DIAS);
    }

    @Test
    @Transactional
    public void getAllReservarsByDiasIsInShouldWork() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dias in DEFAULT_DIAS or UPDATED_DIAS
        defaultReservarShouldBeFound("dias.in=" + DEFAULT_DIAS + "," + UPDATED_DIAS);

        // Get all the reservarList where dias equals to UPDATED_DIAS
        defaultReservarShouldNotBeFound("dias.in=" + UPDATED_DIAS);
    }

    @Test
    @Transactional
    public void getAllReservarsByDiasIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dias is not null
        defaultReservarShouldBeFound("dias.specified=true");

        // Get all the reservarList where dias is null
        defaultReservarShouldNotBeFound("dias.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservarsByDiasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dias greater than or equals to DEFAULT_DIAS
        defaultReservarShouldBeFound("dias.greaterOrEqualThan=" + DEFAULT_DIAS);

        // Get all the reservarList where dias greater than or equals to UPDATED_DIAS
        defaultReservarShouldNotBeFound("dias.greaterOrEqualThan=" + UPDATED_DIAS);
    }

    @Test
    @Transactional
    public void getAllReservarsByDiasIsLessThanSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dias less than or equals to DEFAULT_DIAS
        defaultReservarShouldNotBeFound("dias.lessThan=" + DEFAULT_DIAS);

        // Get all the reservarList where dias less than or equals to UPDATED_DIAS
        defaultReservarShouldBeFound("dias.lessThan=" + UPDATED_DIAS);
    }


    @Test
    @Transactional
    public void getAllReservarsByDataInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataInicio equals to DEFAULT_DATA_INICIO
        defaultReservarShouldBeFound("dataInicio.equals=" + DEFAULT_DATA_INICIO);

        // Get all the reservarList where dataInicio equals to UPDATED_DATA_INICIO
        defaultReservarShouldNotBeFound("dataInicio.equals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllReservarsByDataInicioIsInShouldWork() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataInicio in DEFAULT_DATA_INICIO or UPDATED_DATA_INICIO
        defaultReservarShouldBeFound("dataInicio.in=" + DEFAULT_DATA_INICIO + "," + UPDATED_DATA_INICIO);

        // Get all the reservarList where dataInicio equals to UPDATED_DATA_INICIO
        defaultReservarShouldNotBeFound("dataInicio.in=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllReservarsByDataInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataInicio is not null
        defaultReservarShouldBeFound("dataInicio.specified=true");

        // Get all the reservarList where dataInicio is null
        defaultReservarShouldNotBeFound("dataInicio.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservarsByDataInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataInicio greater than or equals to DEFAULT_DATA_INICIO
        defaultReservarShouldBeFound("dataInicio.greaterOrEqualThan=" + DEFAULT_DATA_INICIO);

        // Get all the reservarList where dataInicio greater than or equals to UPDATED_DATA_INICIO
        defaultReservarShouldNotBeFound("dataInicio.greaterOrEqualThan=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllReservarsByDataInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataInicio less than or equals to DEFAULT_DATA_INICIO
        defaultReservarShouldNotBeFound("dataInicio.lessThan=" + DEFAULT_DATA_INICIO);

        // Get all the reservarList where dataInicio less than or equals to UPDATED_DATA_INICIO
        defaultReservarShouldBeFound("dataInicio.lessThan=" + UPDATED_DATA_INICIO);
    }


    @Test
    @Transactional
    public void getAllReservarsByDataEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataEntrega equals to DEFAULT_DATA_ENTREGA
        defaultReservarShouldBeFound("dataEntrega.equals=" + DEFAULT_DATA_ENTREGA);

        // Get all the reservarList where dataEntrega equals to UPDATED_DATA_ENTREGA
        defaultReservarShouldNotBeFound("dataEntrega.equals=" + UPDATED_DATA_ENTREGA);
    }

    @Test
    @Transactional
    public void getAllReservarsByDataEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataEntrega in DEFAULT_DATA_ENTREGA or UPDATED_DATA_ENTREGA
        defaultReservarShouldBeFound("dataEntrega.in=" + DEFAULT_DATA_ENTREGA + "," + UPDATED_DATA_ENTREGA);

        // Get all the reservarList where dataEntrega equals to UPDATED_DATA_ENTREGA
        defaultReservarShouldNotBeFound("dataEntrega.in=" + UPDATED_DATA_ENTREGA);
    }

    @Test
    @Transactional
    public void getAllReservarsByDataEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataEntrega is not null
        defaultReservarShouldBeFound("dataEntrega.specified=true");

        // Get all the reservarList where dataEntrega is null
        defaultReservarShouldNotBeFound("dataEntrega.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservarsByDataEntregaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataEntrega greater than or equals to DEFAULT_DATA_ENTREGA
        defaultReservarShouldBeFound("dataEntrega.greaterOrEqualThan=" + DEFAULT_DATA_ENTREGA);

        // Get all the reservarList where dataEntrega greater than or equals to UPDATED_DATA_ENTREGA
        defaultReservarShouldNotBeFound("dataEntrega.greaterOrEqualThan=" + UPDATED_DATA_ENTREGA);
    }

    @Test
    @Transactional
    public void getAllReservarsByDataEntregaIsLessThanSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where dataEntrega less than or equals to DEFAULT_DATA_ENTREGA
        defaultReservarShouldNotBeFound("dataEntrega.lessThan=" + DEFAULT_DATA_ENTREGA);

        // Get all the reservarList where dataEntrega less than or equals to UPDATED_DATA_ENTREGA
        defaultReservarShouldBeFound("dataEntrega.lessThan=" + UPDATED_DATA_ENTREGA);
    }


    @Test
    @Transactional
    public void getAllReservarsByValorTotReservaIsEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where valorTotReserva equals to DEFAULT_VALOR_TOT_RESERVA
        defaultReservarShouldBeFound("valorTotReserva.equals=" + DEFAULT_VALOR_TOT_RESERVA);

        // Get all the reservarList where valorTotReserva equals to UPDATED_VALOR_TOT_RESERVA
        defaultReservarShouldNotBeFound("valorTotReserva.equals=" + UPDATED_VALOR_TOT_RESERVA);
    }

    @Test
    @Transactional
    public void getAllReservarsByValorTotReservaIsInShouldWork() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where valorTotReserva in DEFAULT_VALOR_TOT_RESERVA or UPDATED_VALOR_TOT_RESERVA
        defaultReservarShouldBeFound("valorTotReserva.in=" + DEFAULT_VALOR_TOT_RESERVA + "," + UPDATED_VALOR_TOT_RESERVA);

        // Get all the reservarList where valorTotReserva equals to UPDATED_VALOR_TOT_RESERVA
        defaultReservarShouldNotBeFound("valorTotReserva.in=" + UPDATED_VALOR_TOT_RESERVA);
    }

    @Test
    @Transactional
    public void getAllReservarsByValorTotReservaIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where valorTotReserva is not null
        defaultReservarShouldBeFound("valorTotReserva.specified=true");

        // Get all the reservarList where valorTotReserva is null
        defaultReservarShouldNotBeFound("valorTotReserva.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservarsByValorTotReservaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where valorTotReserva greater than or equals to DEFAULT_VALOR_TOT_RESERVA
        defaultReservarShouldBeFound("valorTotReserva.greaterOrEqualThan=" + DEFAULT_VALOR_TOT_RESERVA);

        // Get all the reservarList where valorTotReserva greater than or equals to UPDATED_VALOR_TOT_RESERVA
        defaultReservarShouldNotBeFound("valorTotReserva.greaterOrEqualThan=" + UPDATED_VALOR_TOT_RESERVA);
    }

    @Test
    @Transactional
    public void getAllReservarsByValorTotReservaIsLessThanSomething() throws Exception {
        // Initialize the database
        reservarRepository.saveAndFlush(reservar);

        // Get all the reservarList where valorTotReserva less than or equals to DEFAULT_VALOR_TOT_RESERVA
        defaultReservarShouldNotBeFound("valorTotReserva.lessThan=" + DEFAULT_VALOR_TOT_RESERVA);

        // Get all the reservarList where valorTotReserva less than or equals to UPDATED_VALOR_TOT_RESERVA
        defaultReservarShouldBeFound("valorTotReserva.lessThan=" + UPDATED_VALOR_TOT_RESERVA);
    }


    @Test
    @Transactional
    public void getAllReservarsByHoteisIsEqualToSomething() throws Exception {
        // Initialize the database
        Hotel hoteis = HotelResourceIntTest.createEntity(em);
        em.persist(hoteis);
        em.flush();
        reservar.addHoteis(hoteis);
        reservarRepository.saveAndFlush(reservar);
        Long hoteisId = hoteis.getId();

        // Get all the reservarList where hoteis equals to hoteisId
        defaultReservarShouldBeFound("hoteisId.equals=" + hoteisId);

        // Get all the reservarList where hoteis equals to hoteisId + 1
        defaultReservarShouldNotBeFound("hoteisId.equals=" + (hoteisId + 1));
    }


    @Test
    @Transactional
    public void getAllReservarsByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        Cliente cliente = ClienteResourceIntTest.createEntity(em);
        em.persist(cliente);
        em.flush();
        reservar.addCliente(cliente);
        reservarRepository.saveAndFlush(reservar);
        Long clienteId = cliente.getId();

        // Get all the reservarList where cliente equals to clienteId
        defaultReservarShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the reservarList where cliente equals to clienteId + 1
        defaultReservarShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReservarShouldBeFound(String filter) throws Exception {
        restReservarMockMvc.perform(get("/api/reservars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservar.getId().intValue())))
            .andExpect(jsonPath("$.[*].dias").value(hasItem(DEFAULT_DIAS)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataEntrega").value(hasItem(DEFAULT_DATA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].valorTotReserva").value(hasItem(DEFAULT_VALOR_TOT_RESERVA)));

        // Check, that the count call also returns 1
        restReservarMockMvc.perform(get("/api/reservars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReservarShouldNotBeFound(String filter) throws Exception {
        restReservarMockMvc.perform(get("/api/reservars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReservarMockMvc.perform(get("/api/reservars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingReservar() throws Exception {
        // Get the reservar
        restReservarMockMvc.perform(get("/api/reservars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservar() throws Exception {
        // Initialize the database
        reservarService.save(reservar);

        int databaseSizeBeforeUpdate = reservarRepository.findAll().size();

        // Update the reservar
        Reservar updatedReservar = reservarRepository.findById(reservar.getId()).get();
        // Disconnect from session so that the updates on updatedReservar are not directly saved in db
        em.detach(updatedReservar);
        updatedReservar
            .dias(UPDATED_DIAS)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataEntrega(UPDATED_DATA_ENTREGA)
            .valorTotReserva(UPDATED_VALOR_TOT_RESERVA);

        restReservarMockMvc.perform(put("/api/reservars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReservar)))
            .andExpect(status().isOk());

        // Validate the Reservar in the database
        List<Reservar> reservarList = reservarRepository.findAll();
        assertThat(reservarList).hasSize(databaseSizeBeforeUpdate);
        Reservar testReservar = reservarList.get(reservarList.size() - 1);
        assertThat(testReservar.getDias()).isEqualTo(UPDATED_DIAS);
        assertThat(testReservar.getDataInicio()).isEqualTo(UPDATED_DATA_INICIO);
        assertThat(testReservar.getDataEntrega()).isEqualTo(UPDATED_DATA_ENTREGA);
        assertThat(testReservar.getValorTotReserva()).isEqualTo(UPDATED_VALOR_TOT_RESERVA);
    }

    @Test
    @Transactional
    public void updateNonExistingReservar() throws Exception {
        int databaseSizeBeforeUpdate = reservarRepository.findAll().size();

        // Create the Reservar

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservarMockMvc.perform(put("/api/reservars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservar)))
            .andExpect(status().isBadRequest());

        // Validate the Reservar in the database
        List<Reservar> reservarList = reservarRepository.findAll();
        assertThat(reservarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReservar() throws Exception {
        // Initialize the database
        reservarService.save(reservar);

        int databaseSizeBeforeDelete = reservarRepository.findAll().size();

        // Get the reservar
        restReservarMockMvc.perform(delete("/api/reservars/{id}", reservar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reservar> reservarList = reservarRepository.findAll();
        assertThat(reservarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservar.class);
        Reservar reservar1 = new Reservar();
        reservar1.setId(1L);
        Reservar reservar2 = new Reservar();
        reservar2.setId(reservar1.getId());
        assertThat(reservar1).isEqualTo(reservar2);
        reservar2.setId(2L);
        assertThat(reservar1).isNotEqualTo(reservar2);
        reservar1.setId(null);
        assertThat(reservar1).isNotEqualTo(reservar2);
    }
}
