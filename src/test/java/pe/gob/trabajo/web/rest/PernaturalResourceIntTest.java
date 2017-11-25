package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Pernatural;
import pe.gob.trabajo.repository.PernaturalRepository;
import pe.gob.trabajo.repository.search.PernaturalSearchRepository;
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
 * Test class for the PernaturalResource REST controller.
 *
 * @see PernaturalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class PernaturalResourceIntTest {

    private static final String DEFAULT_V_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_V_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_V_APEPAT = "AAAAAAAAAA";
    private static final String UPDATED_V_APEPAT = "BBBBBBBBBB";

    private static final String DEFAULT_V_APEMAT = "AAAAAAAAAA";
    private static final String UPDATED_V_APEMAT = "BBBBBBBBBB";

    private static final String DEFAULT_V_NUMDOC = "AAAAAAAAAA";
    private static final String UPDATED_V_NUMDOC = "BBBBBBBBBB";

    private static final String DEFAULT_V_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_V_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_V_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_V_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_V_EMAILPER = "AAAAAAAAAA";
    private static final String UPDATED_V_EMAILPER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_D_FECNAC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_D_FECNAC = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_V_SEXOPER = "A";
    private static final String UPDATED_V_SEXOPER = "B";

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
    private PernaturalRepository pernaturalRepository;

    @Autowired
    private PernaturalSearchRepository pernaturalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPernaturalMockMvc;

    private Pernatural pernatural;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PernaturalResource pernaturalResource = new PernaturalResource(pernaturalRepository, pernaturalSearchRepository);
        this.restPernaturalMockMvc = MockMvcBuilders.standaloneSetup(pernaturalResource)
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
    public static Pernatural createEntity(EntityManager em) {
        Pernatural pernatural = new Pernatural()
            .vNombres(DEFAULT_V_NOMBRES)
            .vApepat(DEFAULT_V_APEPAT)
            .vApemat(DEFAULT_V_APEMAT)
            .vNumdoc(DEFAULT_V_NUMDOC)
            .vTelefono(DEFAULT_V_TELEFONO)
            .vCelular(DEFAULT_V_CELULAR)
            .vEmailper(DEFAULT_V_EMAILPER)
            .dFecnac(DEFAULT_D_FECNAC)
            .vSexoper(DEFAULT_V_SEXOPER)
            .vEstado(DEFAULT_V_ESTADO)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return pernatural;
    }

    @Before
    public void initTest() {
        pernaturalSearchRepository.deleteAll();
        pernatural = createEntity(em);
    }

    @Test
    @Transactional
    public void createPernatural() throws Exception {
        int databaseSizeBeforeCreate = pernaturalRepository.findAll().size();

        // Create the Pernatural
        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isCreated());

        // Validate the Pernatural in the database
        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeCreate + 1);
        Pernatural testPernatural = pernaturalList.get(pernaturalList.size() - 1);
        assertThat(testPernatural.getvNombres()).isEqualTo(DEFAULT_V_NOMBRES);
        assertThat(testPernatural.getvApepat()).isEqualTo(DEFAULT_V_APEPAT);
        assertThat(testPernatural.getvApemat()).isEqualTo(DEFAULT_V_APEMAT);
        assertThat(testPernatural.getvNumdoc()).isEqualTo(DEFAULT_V_NUMDOC);
        assertThat(testPernatural.getvTelefono()).isEqualTo(DEFAULT_V_TELEFONO);
        assertThat(testPernatural.getvCelular()).isEqualTo(DEFAULT_V_CELULAR);
        assertThat(testPernatural.getvEmailper()).isEqualTo(DEFAULT_V_EMAILPER);
        assertThat(testPernatural.getdFecnac()).isEqualTo(DEFAULT_D_FECNAC);
        assertThat(testPernatural.getvSexoper()).isEqualTo(DEFAULT_V_SEXOPER);
        assertThat(testPernatural.getvEstado()).isEqualTo(DEFAULT_V_ESTADO);
        assertThat(testPernatural.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testPernatural.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testPernatural.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testPernatural.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testPernatural.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testPernatural.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testPernatural.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Pernatural in Elasticsearch
        Pernatural pernaturalEs = pernaturalSearchRepository.findOne(testPernatural.getId());
        assertThat(pernaturalEs).isEqualToComparingFieldByField(testPernatural);
    }

    @Test
    @Transactional
    public void createPernaturalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pernaturalRepository.findAll().size();

        // Create the Pernatural with an existing ID
        pernatural.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        // Validate the Pernatural in the database
        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvNombresIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setvNombres(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvApepatIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setvApepat(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvApematIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setvApemat(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvNumdocIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setvNumdoc(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvSexoperIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setvSexoper(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkvEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setvEstado(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setnUsuareg(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.settFecreg(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setnFlgactivo(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = pernaturalRepository.findAll().size();
        // set the field null
        pernatural.setnSedereg(null);

        // Create the Pernatural, which fails.

        restPernaturalMockMvc.perform(post("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isBadRequest());

        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPernaturals() throws Exception {
        // Initialize the database
        pernaturalRepository.saveAndFlush(pernatural);

        // Get all the pernaturalList
        restPernaturalMockMvc.perform(get("/api/pernaturals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pernatural.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNombres").value(hasItem(DEFAULT_V_NOMBRES.toString())))
            .andExpect(jsonPath("$.[*].vApepat").value(hasItem(DEFAULT_V_APEPAT.toString())))
            .andExpect(jsonPath("$.[*].vApemat").value(hasItem(DEFAULT_V_APEMAT.toString())))
            .andExpect(jsonPath("$.[*].vNumdoc").value(hasItem(DEFAULT_V_NUMDOC.toString())))
            .andExpect(jsonPath("$.[*].vTelefono").value(hasItem(DEFAULT_V_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].vCelular").value(hasItem(DEFAULT_V_CELULAR.toString())))
            .andExpect(jsonPath("$.[*].vEmailper").value(hasItem(DEFAULT_V_EMAILPER.toString())))
            .andExpect(jsonPath("$.[*].dFecnac").value(hasItem(DEFAULT_D_FECNAC.toString())))
            .andExpect(jsonPath("$.[*].vSexoper").value(hasItem(DEFAULT_V_SEXOPER.toString())))
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
    public void getPernatural() throws Exception {
        // Initialize the database
        pernaturalRepository.saveAndFlush(pernatural);

        // Get the pernatural
        restPernaturalMockMvc.perform(get("/api/pernaturals/{id}", pernatural.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pernatural.getId().intValue()))
            .andExpect(jsonPath("$.vNombres").value(DEFAULT_V_NOMBRES.toString()))
            .andExpect(jsonPath("$.vApepat").value(DEFAULT_V_APEPAT.toString()))
            .andExpect(jsonPath("$.vApemat").value(DEFAULT_V_APEMAT.toString()))
            .andExpect(jsonPath("$.vNumdoc").value(DEFAULT_V_NUMDOC.toString()))
            .andExpect(jsonPath("$.vTelefono").value(DEFAULT_V_TELEFONO.toString()))
            .andExpect(jsonPath("$.vCelular").value(DEFAULT_V_CELULAR.toString()))
            .andExpect(jsonPath("$.vEmailper").value(DEFAULT_V_EMAILPER.toString()))
            .andExpect(jsonPath("$.dFecnac").value(DEFAULT_D_FECNAC.toString()))
            .andExpect(jsonPath("$.vSexoper").value(DEFAULT_V_SEXOPER.toString()))
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
    public void getNonExistingPernatural() throws Exception {
        // Get the pernatural
        restPernaturalMockMvc.perform(get("/api/pernaturals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePernatural() throws Exception {
        // Initialize the database
        pernaturalRepository.saveAndFlush(pernatural);
        pernaturalSearchRepository.save(pernatural);
        int databaseSizeBeforeUpdate = pernaturalRepository.findAll().size();

        // Update the pernatural
        Pernatural updatedPernatural = pernaturalRepository.findOne(pernatural.getId());
        updatedPernatural
            .vNombres(UPDATED_V_NOMBRES)
            .vApepat(UPDATED_V_APEPAT)
            .vApemat(UPDATED_V_APEMAT)
            .vNumdoc(UPDATED_V_NUMDOC)
            .vTelefono(UPDATED_V_TELEFONO)
            .vCelular(UPDATED_V_CELULAR)
            .vEmailper(UPDATED_V_EMAILPER)
            .dFecnac(UPDATED_D_FECNAC)
            .vSexoper(UPDATED_V_SEXOPER)
            .vEstado(UPDATED_V_ESTADO)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restPernaturalMockMvc.perform(put("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPernatural)))
            .andExpect(status().isOk());

        // Validate the Pernatural in the database
        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeUpdate);
        Pernatural testPernatural = pernaturalList.get(pernaturalList.size() - 1);
        assertThat(testPernatural.getvNombres()).isEqualTo(UPDATED_V_NOMBRES);
        assertThat(testPernatural.getvApepat()).isEqualTo(UPDATED_V_APEPAT);
        assertThat(testPernatural.getvApemat()).isEqualTo(UPDATED_V_APEMAT);
        assertThat(testPernatural.getvNumdoc()).isEqualTo(UPDATED_V_NUMDOC);
        assertThat(testPernatural.getvTelefono()).isEqualTo(UPDATED_V_TELEFONO);
        assertThat(testPernatural.getvCelular()).isEqualTo(UPDATED_V_CELULAR);
        assertThat(testPernatural.getvEmailper()).isEqualTo(UPDATED_V_EMAILPER);
        assertThat(testPernatural.getdFecnac()).isEqualTo(UPDATED_D_FECNAC);
        assertThat(testPernatural.getvSexoper()).isEqualTo(UPDATED_V_SEXOPER);
        assertThat(testPernatural.getvEstado()).isEqualTo(UPDATED_V_ESTADO);
        assertThat(testPernatural.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testPernatural.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testPernatural.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testPernatural.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testPernatural.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testPernatural.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testPernatural.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Pernatural in Elasticsearch
        Pernatural pernaturalEs = pernaturalSearchRepository.findOne(testPernatural.getId());
        assertThat(pernaturalEs).isEqualToComparingFieldByField(testPernatural);
    }

    @Test
    @Transactional
    public void updateNonExistingPernatural() throws Exception {
        int databaseSizeBeforeUpdate = pernaturalRepository.findAll().size();

        // Create the Pernatural

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPernaturalMockMvc.perform(put("/api/pernaturals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pernatural)))
            .andExpect(status().isCreated());

        // Validate the Pernatural in the database
        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePernatural() throws Exception {
        // Initialize the database
        pernaturalRepository.saveAndFlush(pernatural);
        pernaturalSearchRepository.save(pernatural);
        int databaseSizeBeforeDelete = pernaturalRepository.findAll().size();

        // Get the pernatural
        restPernaturalMockMvc.perform(delete("/api/pernaturals/{id}", pernatural.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pernaturalExistsInEs = pernaturalSearchRepository.exists(pernatural.getId());
        assertThat(pernaturalExistsInEs).isFalse();

        // Validate the database is empty
        List<Pernatural> pernaturalList = pernaturalRepository.findAll();
        assertThat(pernaturalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPernatural() throws Exception {
        // Initialize the database
        pernaturalRepository.saveAndFlush(pernatural);
        pernaturalSearchRepository.save(pernatural);

        // Search the pernatural
        restPernaturalMockMvc.perform(get("/api/_search/pernaturals?query=id:" + pernatural.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pernatural.getId().intValue())))
            .andExpect(jsonPath("$.[*].vNombres").value(hasItem(DEFAULT_V_NOMBRES.toString())))
            .andExpect(jsonPath("$.[*].vApepat").value(hasItem(DEFAULT_V_APEPAT.toString())))
            .andExpect(jsonPath("$.[*].vApemat").value(hasItem(DEFAULT_V_APEMAT.toString())))
            .andExpect(jsonPath("$.[*].vNumdoc").value(hasItem(DEFAULT_V_NUMDOC.toString())))
            .andExpect(jsonPath("$.[*].vTelefono").value(hasItem(DEFAULT_V_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].vCelular").value(hasItem(DEFAULT_V_CELULAR.toString())))
            .andExpect(jsonPath("$.[*].vEmailper").value(hasItem(DEFAULT_V_EMAILPER.toString())))
            .andExpect(jsonPath("$.[*].dFecnac").value(hasItem(DEFAULT_D_FECNAC.toString())))
            .andExpect(jsonPath("$.[*].vSexoper").value(hasItem(DEFAULT_V_SEXOPER.toString())))
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
        TestUtil.equalsVerifier(Pernatural.class);
        Pernatural pernatural1 = new Pernatural();
        pernatural1.setId(1L);
        Pernatural pernatural2 = new Pernatural();
        pernatural2.setId(pernatural1.getId());
        assertThat(pernatural1).isEqualTo(pernatural2);
        pernatural2.setId(2L);
        assertThat(pernatural1).isNotEqualTo(pernatural2);
        pernatural1.setId(null);
        assertThat(pernatural1).isNotEqualTo(pernatural2);
    }
}
