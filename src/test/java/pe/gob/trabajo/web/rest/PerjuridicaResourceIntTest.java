package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Perjuridica;
import pe.gob.trabajo.repository.PerjuridicaRepository;
import pe.gob.trabajo.repository.search.PerjuridicaSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PerjuridicaResource REST controller.
 *
 * @see PerjuridicaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class PerjuridicaResourceIntTest {

    private static final String DEFAULT_V_RAZSOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_V_RAZSOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_V_NOMALTER = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMALTER = "BBBBBBBBBB";

    private static final String DEFAULT_V_NUMDOC = "AAAAAAAAAA";
    private static final String UPDATED_V_NUMDOC = "BBBBBBBBBB";

    private static final String DEFAULT_V_EMAILPER = "AAAAAAAAAA";
    private static final String UPDATED_V_EMAILPER = "BBBBBBBBBB";

    private static final String DEFAULT_V_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_V_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_V_FAXPERJU = "AAAAAAAAAA";
    private static final String UPDATED_V_FAXPERJU = "BBBBBBBBBB";

    private static final String DEFAULT_V_ESTADO = "A";
    private static final String UPDATED_V_ESTADO = "B";

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
    private PerjuridicaRepository perjuridicaRepository;

    @Autowired
    private PerjuridicaSearchRepository perjuridicaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPerjuridicaMockMvc;

    private Perjuridica perjuridica;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PerjuridicaResource perjuridicaResource = new PerjuridicaResource(perjuridicaRepository, perjuridicaSearchRepository);
        this.restPerjuridicaMockMvc = MockMvcBuilders.standaloneSetup(perjuridicaResource)
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
    public static Perjuridica createEntity(EntityManager em) {
        Perjuridica perjuridica = new Perjuridica()
            .vRazsocial(DEFAULT_V_RAZSOCIAL)
            .vNomalter(DEFAULT_V_NOMALTER)
            .vNumdoc(DEFAULT_V_NUMDOC)
            .vEmailper(DEFAULT_V_EMAILPER)
            .vTelefono(DEFAULT_V_TELEFONO)
            .vFaxperju(DEFAULT_V_FAXPERJU)
            .vEstado(DEFAULT_V_ESTADO)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return perjuridica;
    }

    @Before
    public void initTest() {
        perjuridicaSearchRepository.deleteAll();
        perjuridica = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerjuridica() throws Exception {
        int databaseSizeBeforeCreate = perjuridicaRepository.findAll().size();

        // Create the Perjuridica
        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isCreated());

        // Validate the Perjuridica in the database
        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeCreate + 1);
        Perjuridica testPerjuridica = perjuridicaList.get(perjuridicaList.size() - 1);
        assertThat(testPerjuridica.getvRazsocial()).isEqualTo(DEFAULT_V_RAZSOCIAL);
        assertThat(testPerjuridica.getvNomalter()).isEqualTo(DEFAULT_V_NOMALTER);
        assertThat(testPerjuridica.getvNumdoc()).isEqualTo(DEFAULT_V_NUMDOC);
        assertThat(testPerjuridica.getvEmailper()).isEqualTo(DEFAULT_V_EMAILPER);
        assertThat(testPerjuridica.getvTelefono()).isEqualTo(DEFAULT_V_TELEFONO);
        assertThat(testPerjuridica.getvFaxperju()).isEqualTo(DEFAULT_V_FAXPERJU);
        assertThat(testPerjuridica.getvEstado()).isEqualTo(DEFAULT_V_ESTADO);
        assertThat(testPerjuridica.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testPerjuridica.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testPerjuridica.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testPerjuridica.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testPerjuridica.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testPerjuridica.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testPerjuridica.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Perjuridica in Elasticsearch
        Perjuridica perjuridicaEs = perjuridicaSearchRepository.findOne(testPerjuridica.getId());
        assertThat(perjuridicaEs).isEqualToComparingFieldByField(testPerjuridica);
    }

    @Test
    @Transactional
    public void createPerjuridicaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = perjuridicaRepository.findAll().size();

        // Create the Perjuridica with an existing ID
        perjuridica.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        // Validate the Perjuridica in the database
        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvRazsocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = perjuridicaRepository.findAll().size();
        // set the field null
        perjuridica.setvRazsocial(null);

        // Create the Perjuridica, which fails.

        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvNumdocIsRequired() throws Exception {
        int databaseSizeBeforeTest = perjuridicaRepository.findAll().size();
        // set the field null
        perjuridica.setvNumdoc(null);

        // Create the Perjuridica, which fails.

        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = perjuridicaRepository.findAll().size();
        // set the field null
        perjuridica.setvEstado(null);

        // Create the Perjuridica, which fails.

        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = perjuridicaRepository.findAll().size();
        // set the field null
        perjuridica.setnUsuareg(null);

        // Create the Perjuridica, which fails.

        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = perjuridicaRepository.findAll().size();
        // set the field null
        perjuridica.settFecreg(null);

        // Create the Perjuridica, which fails.

        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = perjuridicaRepository.findAll().size();
        // set the field null
        perjuridica.setnFlgactivo(null);

        // Create the Perjuridica, which fails.

        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = perjuridicaRepository.findAll().size();
        // set the field null
        perjuridica.setnSedereg(null);

        // Create the Perjuridica, which fails.

        restPerjuridicaMockMvc.perform(post("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isBadRequest());

        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPerjuridicas() throws Exception {
        // Initialize the database
        perjuridicaRepository.saveAndFlush(perjuridica);

        // Get all the perjuridicaList
        restPerjuridicaMockMvc.perform(get("/api/perjuridicas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perjuridica.getId().intValue())))
            .andExpect(jsonPath("$.[*].vRazsocial").value(hasItem(DEFAULT_V_RAZSOCIAL.toString())))
            .andExpect(jsonPath("$.[*].vNomalter").value(hasItem(DEFAULT_V_NOMALTER.toString())))
            .andExpect(jsonPath("$.[*].vNumdoc").value(hasItem(DEFAULT_V_NUMDOC.toString())))
            .andExpect(jsonPath("$.[*].vEmailper").value(hasItem(DEFAULT_V_EMAILPER.toString())))
            .andExpect(jsonPath("$.[*].vTelefono").value(hasItem(DEFAULT_V_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].vFaxperju").value(hasItem(DEFAULT_V_FAXPERJU.toString())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
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
    public void getPerjuridica() throws Exception {
        // Initialize the database
        perjuridicaRepository.saveAndFlush(perjuridica);

        // Get the perjuridica
        restPerjuridicaMockMvc.perform(get("/api/perjuridicas/{id}", perjuridica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(perjuridica.getId().intValue()))
            .andExpect(jsonPath("$.vRazsocial").value(DEFAULT_V_RAZSOCIAL.toString()))
            .andExpect(jsonPath("$.vNomalter").value(DEFAULT_V_NOMALTER.toString()))
            .andExpect(jsonPath("$.vNumdoc").value(DEFAULT_V_NUMDOC.toString()))
            .andExpect(jsonPath("$.vEmailper").value(DEFAULT_V_EMAILPER.toString()))
            .andExpect(jsonPath("$.vTelefono").value(DEFAULT_V_TELEFONO.toString()))
            .andExpect(jsonPath("$.vFaxperju").value(DEFAULT_V_FAXPERJU.toString()))
            .andExpect(jsonPath("$.vEstado").value(DEFAULT_V_ESTADO.toString()))
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
    public void getNonExistingPerjuridica() throws Exception {
        // Get the perjuridica
        restPerjuridicaMockMvc.perform(get("/api/perjuridicas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerjuridica() throws Exception {
        // Initialize the database
        perjuridicaRepository.saveAndFlush(perjuridica);
        perjuridicaSearchRepository.save(perjuridica);
        int databaseSizeBeforeUpdate = perjuridicaRepository.findAll().size();

        // Update the perjuridica
        Perjuridica updatedPerjuridica = perjuridicaRepository.findOne(perjuridica.getId());
        updatedPerjuridica
            .vRazsocial(UPDATED_V_RAZSOCIAL)
            .vNomalter(UPDATED_V_NOMALTER)
            .vNumdoc(UPDATED_V_NUMDOC)
            .vEmailper(UPDATED_V_EMAILPER)
            .vTelefono(UPDATED_V_TELEFONO)
            .vFaxperju(UPDATED_V_FAXPERJU)
            .vEstado(UPDATED_V_ESTADO)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restPerjuridicaMockMvc.perform(put("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPerjuridica)))
            .andExpect(status().isOk());

        // Validate the Perjuridica in the database
        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeUpdate);
        Perjuridica testPerjuridica = perjuridicaList.get(perjuridicaList.size() - 1);
        assertThat(testPerjuridica.getvRazsocial()).isEqualTo(UPDATED_V_RAZSOCIAL);
        assertThat(testPerjuridica.getvNomalter()).isEqualTo(UPDATED_V_NOMALTER);
        assertThat(testPerjuridica.getvNumdoc()).isEqualTo(UPDATED_V_NUMDOC);
        assertThat(testPerjuridica.getvEmailper()).isEqualTo(UPDATED_V_EMAILPER);
        assertThat(testPerjuridica.getvTelefono()).isEqualTo(UPDATED_V_TELEFONO);
        assertThat(testPerjuridica.getvFaxperju()).isEqualTo(UPDATED_V_FAXPERJU);
        assertThat(testPerjuridica.getvEstado()).isEqualTo(UPDATED_V_ESTADO);
        assertThat(testPerjuridica.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testPerjuridica.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testPerjuridica.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testPerjuridica.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testPerjuridica.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testPerjuridica.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testPerjuridica.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Perjuridica in Elasticsearch
        Perjuridica perjuridicaEs = perjuridicaSearchRepository.findOne(testPerjuridica.getId());
        assertThat(perjuridicaEs).isEqualToComparingFieldByField(testPerjuridica);
    }

    @Test
    @Transactional
    public void updateNonExistingPerjuridica() throws Exception {
        int databaseSizeBeforeUpdate = perjuridicaRepository.findAll().size();

        // Create the Perjuridica

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPerjuridicaMockMvc.perform(put("/api/perjuridicas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(perjuridica)))
            .andExpect(status().isCreated());

        // Validate the Perjuridica in the database
        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePerjuridica() throws Exception {
        // Initialize the database
        perjuridicaRepository.saveAndFlush(perjuridica);
        perjuridicaSearchRepository.save(perjuridica);
        int databaseSizeBeforeDelete = perjuridicaRepository.findAll().size();

        // Get the perjuridica
        restPerjuridicaMockMvc.perform(delete("/api/perjuridicas/{id}", perjuridica.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean perjuridicaExistsInEs = perjuridicaSearchRepository.exists(perjuridica.getId());
        assertThat(perjuridicaExistsInEs).isFalse();

        // Validate the database is empty
        List<Perjuridica> perjuridicaList = perjuridicaRepository.findAll();
        assertThat(perjuridicaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPerjuridica() throws Exception {
        // Initialize the database
        perjuridicaRepository.saveAndFlush(perjuridica);
        perjuridicaSearchRepository.save(perjuridica);

        // Search the perjuridica
        restPerjuridicaMockMvc.perform(get("/api/_search/perjuridicas?query=id:" + perjuridica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perjuridica.getId().intValue())))
            .andExpect(jsonPath("$.[*].vRazsocial").value(hasItem(DEFAULT_V_RAZSOCIAL.toString())))
            .andExpect(jsonPath("$.[*].vNomalter").value(hasItem(DEFAULT_V_NOMALTER.toString())))
            .andExpect(jsonPath("$.[*].vNumdoc").value(hasItem(DEFAULT_V_NUMDOC.toString())))
            .andExpect(jsonPath("$.[*].vEmailper").value(hasItem(DEFAULT_V_EMAILPER.toString())))
            .andExpect(jsonPath("$.[*].vTelefono").value(hasItem(DEFAULT_V_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].vFaxperju").value(hasItem(DEFAULT_V_FAXPERJU.toString())))
            .andExpect(jsonPath("$.[*].vEstado").value(hasItem(DEFAULT_V_ESTADO.toString())))
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
        TestUtil.equalsVerifier(Perjuridica.class);
        Perjuridica perjuridica1 = new Perjuridica();
        perjuridica1.setId(1L);
        Perjuridica perjuridica2 = new Perjuridica();
        perjuridica2.setId(perjuridica1.getId());
        assertThat(perjuridica1).isEqualTo(perjuridica2);
        perjuridica2.setId(2L);
        assertThat(perjuridica1).isNotEqualTo(perjuridica2);
        perjuridica1.setId(null);
        assertThat(perjuridica1).isNotEqualTo(perjuridica2);
    }
}
