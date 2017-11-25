package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Datlab;
import pe.gob.trabajo.repository.DatlabRepository;
import pe.gob.trabajo.repository.search.DatlabSearchRepository;
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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DatlabResource REST controller.
 *
 * @see DatlabResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DatlabResourceIntTest {

    private static final Boolean DEFAULT_N_FLGSITLAB = false;
    private static final Boolean UPDATED_N_FLGSITLAB = true;

    private static final LocalDate DEFAULT_D_FECVINCUL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_D_FECVINCUL = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_D_FECCESE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_D_FECCESE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_D_FECFINCON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_D_FECFINCON = LocalDate.now(ZoneId.systemDefault());

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
    private DatlabRepository datlabRepository;

    @Autowired
    private DatlabSearchRepository datlabSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDatlabMockMvc;

    private Datlab datlab;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DatlabResource datlabResource = new DatlabResource(datlabRepository, datlabSearchRepository);
        this.restDatlabMockMvc = MockMvcBuilders.standaloneSetup(datlabResource)
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
    public static Datlab createEntity(EntityManager em) {
        Datlab datlab = new Datlab()
            .nFlgsitlab(DEFAULT_N_FLGSITLAB)
            .dFecvincul(DEFAULT_D_FECVINCUL)
            .dFeccese(DEFAULT_D_FECCESE)
            .dFecfincon(DEFAULT_D_FECFINCON)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return datlab;
    }

    @Before
    public void initTest() {
        datlabSearchRepository.deleteAll();
        datlab = createEntity(em);
    }

    @Test
    @Transactional
    public void createDatlab() throws Exception {
        int databaseSizeBeforeCreate = datlabRepository.findAll().size();

        // Create the Datlab
        restDatlabMockMvc.perform(post("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isCreated());

        // Validate the Datlab in the database
        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeCreate + 1);
        Datlab testDatlab = datlabList.get(datlabList.size() - 1);
        assertThat(testDatlab.isnFlgsitlab()).isEqualTo(DEFAULT_N_FLGSITLAB);
        assertThat(testDatlab.getdFecvincul()).isEqualTo(DEFAULT_D_FECVINCUL);
        assertThat(testDatlab.getdFeccese()).isEqualTo(DEFAULT_D_FECCESE);
        assertThat(testDatlab.getdFecfincon()).isEqualTo(DEFAULT_D_FECFINCON);
        assertThat(testDatlab.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDatlab.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDatlab.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDatlab.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDatlab.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDatlab.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDatlab.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Datlab in Elasticsearch
        Datlab datlabEs = datlabSearchRepository.findOne(testDatlab.getId());
        assertThat(datlabEs).isEqualToComparingFieldByField(testDatlab);
    }

    @Test
    @Transactional
    public void createDatlabWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = datlabRepository.findAll().size();

        // Create the Datlab with an existing ID
        datlab.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatlabMockMvc.perform(post("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isBadRequest());

        // Validate the Datlab in the database
        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checknFlgsitlabIsRequired() throws Exception {
        int databaseSizeBeforeTest = datlabRepository.findAll().size();
        // set the field null
        datlab.setnFlgsitlab(null);

        // Create the Datlab, which fails.

        restDatlabMockMvc.perform(post("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isBadRequest());

        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = datlabRepository.findAll().size();
        // set the field null
        datlab.setnUsuareg(null);

        // Create the Datlab, which fails.

        restDatlabMockMvc.perform(post("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isBadRequest());

        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = datlabRepository.findAll().size();
        // set the field null
        datlab.settFecreg(null);

        // Create the Datlab, which fails.

        restDatlabMockMvc.perform(post("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isBadRequest());

        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = datlabRepository.findAll().size();
        // set the field null
        datlab.setnFlgactivo(null);

        // Create the Datlab, which fails.

        restDatlabMockMvc.perform(post("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isBadRequest());

        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = datlabRepository.findAll().size();
        // set the field null
        datlab.setnSedereg(null);

        // Create the Datlab, which fails.

        restDatlabMockMvc.perform(post("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isBadRequest());

        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDatlabs() throws Exception {
        // Initialize the database
        datlabRepository.saveAndFlush(datlab);

        // Get all the datlabList
        restDatlabMockMvc.perform(get("/api/datlabs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datlab.getId().intValue())))
            .andExpect(jsonPath("$.[*].nFlgsitlab").value(hasItem(DEFAULT_N_FLGSITLAB.booleanValue())))
            .andExpect(jsonPath("$.[*].dFecvincul").value(hasItem(DEFAULT_D_FECVINCUL.toString())))
            .andExpect(jsonPath("$.[*].dFeccese").value(hasItem(DEFAULT_D_FECCESE.toString())))
            .andExpect(jsonPath("$.[*].dFecfincon").value(hasItem(DEFAULT_D_FECFINCON.toString())))
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
    public void getDatlab() throws Exception {
        // Initialize the database
        datlabRepository.saveAndFlush(datlab);

        // Get the datlab
        restDatlabMockMvc.perform(get("/api/datlabs/{id}", datlab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(datlab.getId().intValue()))
            .andExpect(jsonPath("$.nFlgsitlab").value(DEFAULT_N_FLGSITLAB.booleanValue()))
            .andExpect(jsonPath("$.dFecvincul").value(DEFAULT_D_FECVINCUL.toString()))
            .andExpect(jsonPath("$.dFeccese").value(DEFAULT_D_FECCESE.toString()))
            .andExpect(jsonPath("$.dFecfincon").value(DEFAULT_D_FECFINCON.toString()))
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
    public void getNonExistingDatlab() throws Exception {
        // Get the datlab
        restDatlabMockMvc.perform(get("/api/datlabs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDatlab() throws Exception {
        // Initialize the database
        datlabRepository.saveAndFlush(datlab);
        datlabSearchRepository.save(datlab);
        int databaseSizeBeforeUpdate = datlabRepository.findAll().size();

        // Update the datlab
        Datlab updatedDatlab = datlabRepository.findOne(datlab.getId());
        updatedDatlab
            .nFlgsitlab(UPDATED_N_FLGSITLAB)
            .dFecvincul(UPDATED_D_FECVINCUL)
            .dFeccese(UPDATED_D_FECCESE)
            .dFecfincon(UPDATED_D_FECFINCON)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDatlabMockMvc.perform(put("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDatlab)))
            .andExpect(status().isOk());

        // Validate the Datlab in the database
        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeUpdate);
        Datlab testDatlab = datlabList.get(datlabList.size() - 1);
        assertThat(testDatlab.isnFlgsitlab()).isEqualTo(UPDATED_N_FLGSITLAB);
        assertThat(testDatlab.getdFecvincul()).isEqualTo(UPDATED_D_FECVINCUL);
        assertThat(testDatlab.getdFeccese()).isEqualTo(UPDATED_D_FECCESE);
        assertThat(testDatlab.getdFecfincon()).isEqualTo(UPDATED_D_FECFINCON);
        assertThat(testDatlab.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDatlab.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDatlab.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDatlab.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDatlab.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDatlab.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDatlab.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Datlab in Elasticsearch
        Datlab datlabEs = datlabSearchRepository.findOne(testDatlab.getId());
        assertThat(datlabEs).isEqualToComparingFieldByField(testDatlab);
    }

    @Test
    @Transactional
    public void updateNonExistingDatlab() throws Exception {
        int databaseSizeBeforeUpdate = datlabRepository.findAll().size();

        // Create the Datlab

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDatlabMockMvc.perform(put("/api/datlabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datlab)))
            .andExpect(status().isCreated());

        // Validate the Datlab in the database
        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDatlab() throws Exception {
        // Initialize the database
        datlabRepository.saveAndFlush(datlab);
        datlabSearchRepository.save(datlab);
        int databaseSizeBeforeDelete = datlabRepository.findAll().size();

        // Get the datlab
        restDatlabMockMvc.perform(delete("/api/datlabs/{id}", datlab.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean datlabExistsInEs = datlabSearchRepository.exists(datlab.getId());
        assertThat(datlabExistsInEs).isFalse();

        // Validate the database is empty
        List<Datlab> datlabList = datlabRepository.findAll();
        assertThat(datlabList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDatlab() throws Exception {
        // Initialize the database
        datlabRepository.saveAndFlush(datlab);
        datlabSearchRepository.save(datlab);

        // Search the datlab
        restDatlabMockMvc.perform(get("/api/_search/datlabs?query=id:" + datlab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datlab.getId().intValue())))
            .andExpect(jsonPath("$.[*].nFlgsitlab").value(hasItem(DEFAULT_N_FLGSITLAB.booleanValue())))
            .andExpect(jsonPath("$.[*].dFecvincul").value(hasItem(DEFAULT_D_FECVINCUL.toString())))
            .andExpect(jsonPath("$.[*].dFeccese").value(hasItem(DEFAULT_D_FECCESE.toString())))
            .andExpect(jsonPath("$.[*].dFecfincon").value(hasItem(DEFAULT_D_FECFINCON.toString())))
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
        TestUtil.equalsVerifier(Datlab.class);
        Datlab datlab1 = new Datlab();
        datlab1.setId(1L);
        Datlab datlab2 = new Datlab();
        datlab2.setId(datlab1.getId());
        assertThat(datlab1).isEqualTo(datlab2);
        datlab2.setId(2L);
        assertThat(datlab1).isNotEqualTo(datlab2);
        datlab1.setId(null);
        assertThat(datlab1).isNotEqualTo(datlab2);
    }
}
