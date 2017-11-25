package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Liquidacion;
import pe.gob.trabajo.repository.LiquidacionRepository;
import pe.gob.trabajo.repository.search.LiquidacionSearchRepository;
import pe.gob.trabajo.web.rest.errors.ExceptionTranslator;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LiquidacionResource REST controller.
 *
 * @see LiquidacionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class LiquidacionResourceIntTest {

    private static final BigDecimal DEFAULT_N_LIQUID = new BigDecimal(1);
    private static final BigDecimal UPDATED_N_LIQUID = new BigDecimal(2);

    private static final Integer DEFAULT_N_USUAREG = 1;
    private static final Integer UPDATED_N_USUAREG = 2;

    private static final Instant DEFAULT_T_FECREG = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_T_FECREG = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_N_FLGACTIVO = false;
    private static final Boolean UPDATED_N_FLGACTIVO = true;

    private static final Integer DEFAULT_N_SEDEREG = 1;
    private static final Integer UPDATED_N_SEDEREG = 2;

    private static final Integer DEFAULT_N_USUAUPD = 1;
    private static final Integer UPDATED_N_USUAUPD = 2;

    private static final Instant DEFAULT_T_FECUPD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_T_FECUPD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_N_SEDEUPD = 1;
    private static final Integer UPDATED_N_SEDEUPD = 2;

    @Autowired
    private LiquidacionRepository liquidacionRepository;

    @Autowired
    private LiquidacionSearchRepository liquidacionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLiquidacionMockMvc;

    private Liquidacion liquidacion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LiquidacionResource liquidacionResource = new LiquidacionResource(liquidacionRepository, liquidacionSearchRepository);
        this.restLiquidacionMockMvc = MockMvcBuilders.standaloneSetup(liquidacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Liquidacion createEntity(EntityManager em) {
        Liquidacion liquidacion = new Liquidacion()
            .nLiquid(DEFAULT_N_LIQUID)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return liquidacion;
    }

    @Before
    public void initTest() {
        liquidacionSearchRepository.deleteAll();
        liquidacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createLiquidacion() throws Exception {
        int databaseSizeBeforeCreate = liquidacionRepository.findAll().size();

        // Create the Liquidacion
        restLiquidacionMockMvc.perform(post("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(liquidacion)))
            .andExpect(status().isCreated());

        // Validate the Liquidacion in the database
        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeCreate + 1);
        Liquidacion testLiquidacion = liquidacionList.get(liquidacionList.size() - 1);
        assertThat(testLiquidacion.getnLiquid()).isEqualTo(DEFAULT_N_LIQUID);
        assertThat(testLiquidacion.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testLiquidacion.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testLiquidacion.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testLiquidacion.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testLiquidacion.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testLiquidacion.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testLiquidacion.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Liquidacion in Elasticsearch
        Liquidacion liquidacionEs = liquidacionSearchRepository.findOne(testLiquidacion.getId());
        assertThat(liquidacionEs).isEqualToComparingFieldByField(testLiquidacion);
    }

    @Test
    @Transactional
    public void createLiquidacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = liquidacionRepository.findAll().size();

        // Create the Liquidacion with an existing ID
        liquidacion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLiquidacionMockMvc.perform(post("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(liquidacion)))
            .andExpect(status().isBadRequest());

        // Validate the Liquidacion in the database
        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = liquidacionRepository.findAll().size();
        // set the field null
        liquidacion.setnUsuareg(null);

        // Create the Liquidacion, which fails.

        restLiquidacionMockMvc.perform(post("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(liquidacion)))
            .andExpect(status().isBadRequest());

        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = liquidacionRepository.findAll().size();
        // set the field null
        liquidacion.settFecreg(null);

        // Create the Liquidacion, which fails.

        restLiquidacionMockMvc.perform(post("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(liquidacion)))
            .andExpect(status().isBadRequest());

        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = liquidacionRepository.findAll().size();
        // set the field null
        liquidacion.setnFlgactivo(null);

        // Create the Liquidacion, which fails.

        restLiquidacionMockMvc.perform(post("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(liquidacion)))
            .andExpect(status().isBadRequest());

        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = liquidacionRepository.findAll().size();
        // set the field null
        liquidacion.setnSedereg(null);

        // Create the Liquidacion, which fails.

        restLiquidacionMockMvc.perform(post("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(liquidacion)))
            .andExpect(status().isBadRequest());

        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLiquidacions() throws Exception {
        // Initialize the database
        liquidacionRepository.saveAndFlush(liquidacion);

        // Get all the liquidacionList
        restLiquidacionMockMvc.perform(get("/api/liquidacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liquidacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nLiquid").value(hasItem(DEFAULT_N_LIQUID.intValue())))
            .andExpect(jsonPath("$.[*].nUsuareg").value(hasItem(DEFAULT_N_USUAREG)))
            .andExpect(jsonPath("$.[*].tFecreg").value(hasItem(DEFAULT_T_FECREG.toString())))
            .andExpect(jsonPath("$.[*].nFlgactivo").value(hasItem(DEFAULT_N_FLGACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].nSedereg").value(hasItem(DEFAULT_N_SEDEREG)))
            .andExpect(jsonPath("$.[*].nUsuaupd").value(hasItem(DEFAULT_N_USUAUPD)))
            .andExpect(jsonPath("$.[*].tFecupd").value(hasItem(DEFAULT_T_FECUPD.toString())))
            .andExpect(jsonPath("$.[*].nSedeupd").value(hasItem(DEFAULT_N_SEDEUPD)));
    }

    @Test
    @Transactional
    public void getLiquidacion() throws Exception {
        // Initialize the database
        liquidacionRepository.saveAndFlush(liquidacion);

        // Get the liquidacion
        restLiquidacionMockMvc.perform(get("/api/liquidacions/{id}", liquidacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(liquidacion.getId().intValue()))
            .andExpect(jsonPath("$.nLiquid").value(DEFAULT_N_LIQUID.intValue()))
            .andExpect(jsonPath("$.nUsuareg").value(DEFAULT_N_USUAREG))
            .andExpect(jsonPath("$.tFecreg").value(DEFAULT_T_FECREG.toString()))
            .andExpect(jsonPath("$.nFlgactivo").value(DEFAULT_N_FLGACTIVO.booleanValue()))
            .andExpect(jsonPath("$.nSedereg").value(DEFAULT_N_SEDEREG))
            .andExpect(jsonPath("$.nUsuaupd").value(DEFAULT_N_USUAUPD))
            .andExpect(jsonPath("$.tFecupd").value(DEFAULT_T_FECUPD.toString()))
            .andExpect(jsonPath("$.nSedeupd").value(DEFAULT_N_SEDEUPD));
    }

    @Test
    @Transactional
    public void getNonExistingLiquidacion() throws Exception {
        // Get the liquidacion
        restLiquidacionMockMvc.perform(get("/api/liquidacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLiquidacion() throws Exception {
        // Initialize the database
        liquidacionRepository.saveAndFlush(liquidacion);
        liquidacionSearchRepository.save(liquidacion);
        int databaseSizeBeforeUpdate = liquidacionRepository.findAll().size();

        // Update the liquidacion
        Liquidacion updatedLiquidacion = liquidacionRepository.findOne(liquidacion.getId());
        updatedLiquidacion
            .nLiquid(UPDATED_N_LIQUID)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restLiquidacionMockMvc.perform(put("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLiquidacion)))
            .andExpect(status().isOk());

        // Validate the Liquidacion in the database
        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeUpdate);
        Liquidacion testLiquidacion = liquidacionList.get(liquidacionList.size() - 1);
        assertThat(testLiquidacion.getnLiquid()).isEqualTo(UPDATED_N_LIQUID);
        assertThat(testLiquidacion.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testLiquidacion.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testLiquidacion.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testLiquidacion.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testLiquidacion.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testLiquidacion.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testLiquidacion.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Liquidacion in Elasticsearch
        Liquidacion liquidacionEs = liquidacionSearchRepository.findOne(testLiquidacion.getId());
        assertThat(liquidacionEs).isEqualToComparingFieldByField(testLiquidacion);
    }

    @Test
    @Transactional
    public void updateNonExistingLiquidacion() throws Exception {
        int databaseSizeBeforeUpdate = liquidacionRepository.findAll().size();

        // Create the Liquidacion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLiquidacionMockMvc.perform(put("/api/liquidacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(liquidacion)))
            .andExpect(status().isCreated());

        // Validate the Liquidacion in the database
        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLiquidacion() throws Exception {
        // Initialize the database
        liquidacionRepository.saveAndFlush(liquidacion);
        liquidacionSearchRepository.save(liquidacion);
        int databaseSizeBeforeDelete = liquidacionRepository.findAll().size();

        // Get the liquidacion
        restLiquidacionMockMvc.perform(delete("/api/liquidacions/{id}", liquidacion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean liquidacionExistsInEs = liquidacionSearchRepository.exists(liquidacion.getId());
        assertThat(liquidacionExistsInEs).isFalse();

        // Validate the database is empty
        List<Liquidacion> liquidacionList = liquidacionRepository.findAll();
        assertThat(liquidacionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLiquidacion() throws Exception {
        // Initialize the database
        liquidacionRepository.saveAndFlush(liquidacion);
        liquidacionSearchRepository.save(liquidacion);

        // Search the liquidacion
        restLiquidacionMockMvc.perform(get("/api/_search/liquidacions?query=id:" + liquidacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liquidacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nLiquid").value(hasItem(DEFAULT_N_LIQUID.intValue())))
            .andExpect(jsonPath("$.[*].nUsuareg").value(hasItem(DEFAULT_N_USUAREG)))
            .andExpect(jsonPath("$.[*].tFecreg").value(hasItem(DEFAULT_T_FECREG.toString())))
            .andExpect(jsonPath("$.[*].nFlgactivo").value(hasItem(DEFAULT_N_FLGACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].nSedereg").value(hasItem(DEFAULT_N_SEDEREG)))
            .andExpect(jsonPath("$.[*].nUsuaupd").value(hasItem(DEFAULT_N_USUAUPD)))
            .andExpect(jsonPath("$.[*].tFecupd").value(hasItem(DEFAULT_T_FECUPD.toString())))
            .andExpect(jsonPath("$.[*].nSedeupd").value(hasItem(DEFAULT_N_SEDEUPD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Liquidacion.class);
        Liquidacion liquidacion1 = new Liquidacion();
        liquidacion1.setId(1L);
        Liquidacion liquidacion2 = new Liquidacion();
        liquidacion2.setId(liquidacion1.getId());
        assertThat(liquidacion1).isEqualTo(liquidacion2);
        liquidacion2.setId(2L);
        assertThat(liquidacion1).isNotEqualTo(liquidacion2);
        liquidacion1.setId(null);
        assertThat(liquidacion1).isNotEqualTo(liquidacion2);
    }
}
