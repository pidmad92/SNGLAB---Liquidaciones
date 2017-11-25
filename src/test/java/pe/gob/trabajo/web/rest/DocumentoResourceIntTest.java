package pe.gob.trabajo.web.rest;

import pe.gob.trabajo.LiquidacionesApp;

import pe.gob.trabajo.domain.Documento;
import pe.gob.trabajo.repository.DocumentoRepository;
import pe.gob.trabajo.repository.search.DocumentoSearchRepository;
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
 * Test class for the DocumentoResource REST controller.
 *
 * @see DocumentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiquidacionesApp.class)
public class DocumentoResourceIntTest {

    private static final String DEFAULT_V_DESDOC = "AAAAAAAAAA";
    private static final String UPDATED_V_DESDOC = "BBBBBBBBBB";

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
    private DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoSearchRepository documentoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentoMockMvc;

    private Documento documento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentoResource documentoResource = new DocumentoResource(documentoRepository, documentoSearchRepository);
        this.restDocumentoMockMvc = MockMvcBuilders.standaloneSetup(documentoResource)
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
    public static Documento createEntity(EntityManager em) {
        Documento documento = new Documento()
            .vDesdoc(DEFAULT_V_DESDOC)
            .nUsuareg(DEFAULT_N_USUAREG)
            .tFecreg(DEFAULT_T_FECREG)
            .nFlgactivo(DEFAULT_N_FLGACTIVO)
            .nSedereg(DEFAULT_N_SEDEREG)
            .nUsuaupd(DEFAULT_N_USUAUPD)
            .tFecupd(DEFAULT_T_FECUPD)
            .nSedeupd(DEFAULT_N_SEDEUPD);
        return documento;
    }

    @Before
    public void initTest() {
        documentoSearchRepository.deleteAll();
        documento = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumento() throws Exception {
        int databaseSizeBeforeCreate = documentoRepository.findAll().size();

        // Create the Documento
        restDocumentoMockMvc.perform(post("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isCreated());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeCreate + 1);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getvDesdoc()).isEqualTo(DEFAULT_V_DESDOC);
        assertThat(testDocumento.getnUsuareg()).isEqualTo(DEFAULT_N_USUAREG);
        assertThat(testDocumento.gettFecreg()).isEqualTo(DEFAULT_T_FECREG);
        assertThat(testDocumento.isnFlgactivo()).isEqualTo(DEFAULT_N_FLGACTIVO);
        assertThat(testDocumento.getnSedereg()).isEqualTo(DEFAULT_N_SEDEREG);
        assertThat(testDocumento.getnUsuaupd()).isEqualTo(DEFAULT_N_USUAUPD);
        assertThat(testDocumento.gettFecupd()).isEqualTo(DEFAULT_T_FECUPD);
        assertThat(testDocumento.getnSedeupd()).isEqualTo(DEFAULT_N_SEDEUPD);

        // Validate the Documento in Elasticsearch
        Documento documentoEs = documentoSearchRepository.findOne(testDocumento.getId());
        assertThat(documentoEs).isEqualToComparingFieldByField(testDocumento);
    }

    @Test
    @Transactional
    public void createDocumentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentoRepository.findAll().size();

        // Create the Documento with an existing ID
        documento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoMockMvc.perform(post("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkvDesdocIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentoRepository.findAll().size();
        // set the field null
        documento.setvDesdoc(null);

        // Create the Documento, which fails.

        restDocumentoMockMvc.perform(post("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isBadRequest());

        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknUsuaregIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentoRepository.findAll().size();
        // set the field null
        documento.setnUsuareg(null);

        // Create the Documento, which fails.

        restDocumentoMockMvc.perform(post("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isBadRequest());

        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checktFecregIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentoRepository.findAll().size();
        // set the field null
        documento.settFecreg(null);

        // Create the Documento, which fails.

        restDocumentoMockMvc.perform(post("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isBadRequest());

        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknFlgactivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentoRepository.findAll().size();
        // set the field null
        documento.setnFlgactivo(null);

        // Create the Documento, which fails.

        restDocumentoMockMvc.perform(post("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isBadRequest());

        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknSederegIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentoRepository.findAll().size();
        // set the field null
        documento.setnSedereg(null);

        // Create the Documento, which fails.

        restDocumentoMockMvc.perform(post("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isBadRequest());

        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentos() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList
        restDocumentoMockMvc.perform(get("/api/documentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdoc").value(hasItem(DEFAULT_V_DESDOC.toString())))
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
    public void getDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get the documento
        restDocumentoMockMvc.perform(get("/api/documentos/{id}", documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documento.getId().intValue()))
            .andExpect(jsonPath("$.vDesdoc").value(DEFAULT_V_DESDOC.toString()))
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
    public void getNonExistingDocumento() throws Exception {
        // Get the documento
        restDocumentoMockMvc.perform(get("/api/documentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        documentoSearchRepository.save(documento);
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento
        Documento updatedDocumento = documentoRepository.findOne(documento.getId());
        updatedDocumento
            .vDesdoc(UPDATED_V_DESDOC)
            .nUsuareg(UPDATED_N_USUAREG)
            .tFecreg(UPDATED_T_FECREG)
            .nFlgactivo(UPDATED_N_FLGACTIVO)
            .nSedereg(UPDATED_N_SEDEREG)
            .nUsuaupd(UPDATED_N_USUAUPD)
            .tFecupd(UPDATED_T_FECUPD)
            .nSedeupd(UPDATED_N_SEDEUPD);

        restDocumentoMockMvc.perform(put("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumento)))
            .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getvDesdoc()).isEqualTo(UPDATED_V_DESDOC);
        assertThat(testDocumento.getnUsuareg()).isEqualTo(UPDATED_N_USUAREG);
        assertThat(testDocumento.gettFecreg()).isEqualTo(UPDATED_T_FECREG);
        assertThat(testDocumento.isnFlgactivo()).isEqualTo(UPDATED_N_FLGACTIVO);
        assertThat(testDocumento.getnSedereg()).isEqualTo(UPDATED_N_SEDEREG);
        assertThat(testDocumento.getnUsuaupd()).isEqualTo(UPDATED_N_USUAUPD);
        assertThat(testDocumento.gettFecupd()).isEqualTo(UPDATED_T_FECUPD);
        assertThat(testDocumento.getnSedeupd()).isEqualTo(UPDATED_N_SEDEUPD);

        // Validate the Documento in Elasticsearch
        Documento documentoEs = documentoSearchRepository.findOne(testDocumento.getId());
        assertThat(documentoEs).isEqualToComparingFieldByField(testDocumento);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Create the Documento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentoMockMvc.perform(put("/api/documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documento)))
            .andExpect(status().isCreated());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        documentoSearchRepository.save(documento);
        int databaseSizeBeforeDelete = documentoRepository.findAll().size();

        // Get the documento
        restDocumentoMockMvc.perform(delete("/api/documentos/{id}", documento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean documentoExistsInEs = documentoSearchRepository.exists(documento.getId());
        assertThat(documentoExistsInEs).isFalse();

        // Validate the database is empty
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        documentoSearchRepository.save(documento);

        // Search the documento
        restDocumentoMockMvc.perform(get("/api/_search/documentos?query=id:" + documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].vDesdoc").value(hasItem(DEFAULT_V_DESDOC.toString())))
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
        TestUtil.equalsVerifier(Documento.class);
        Documento documento1 = new Documento();
        documento1.setId(1L);
        Documento documento2 = new Documento();
        documento2.setId(documento1.getId());
        assertThat(documento1).isEqualTo(documento2);
        documento2.setId(2L);
        assertThat(documento1).isNotEqualTo(documento2);
        documento1.setId(null);
        assertThat(documento1).isNotEqualTo(documento2);
    }
}
