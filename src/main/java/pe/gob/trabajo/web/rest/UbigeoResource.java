package pe.gob.trabajo.web.rest;

import com.codahale.metrics.annotation.Timed;

import pe.gob.trabajo.domain.Departamento;
import pe.gob.trabajo.domain.Distrito;
import pe.gob.trabajo.domain.Provincia;
import pe.gob.trabajo.domain.Tipdocident;
import pe.gob.trabajo.repository.DepartamentoRepository;
import pe.gob.trabajo.repository.DistritoRepository;
import pe.gob.trabajo.repository.ProvinciaRepository;
import pe.gob.trabajo.repository.TipdocidentRepository;
import pe.gob.trabajo.repository.search.TipdocidentSearchRepository;
import pe.gob.trabajo.web.rest.errors.BadRequestAlertException;
import pe.gob.trabajo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Tipdocident.
 */
@RestController
@RequestMapping("/api")
public class UbigeoResource {

    private final Logger log = LoggerFactory.getLogger(UbigeoResource.class);

    private static final String ENTITY_NAME = "ubigeo";

    private final DepartamentoRepository departamentoRepository;
    private final ProvinciaRepository provinciaRepository;
    private final DistritoRepository distritoRepository;

    public UbigeoResource(DepartamentoRepository departamentoRepository, ProvinciaRepository provinciaRepository,
            DistritoRepository distritoRepository) {
        this.departamentoRepository = departamentoRepository;
        this.provinciaRepository = provinciaRepository;
        this.distritoRepository = distritoRepository;
    }

    /**
     * POST  /tipdocidents : Create a new tipdocident.
     *
     * @param tipdocident the tipdocident to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipdocident, or with status 400 (Bad Request) if the tipdocident has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @GetMapping("/departamentos")
    @Timed
    public List<Departamento> getDepartamentos(){
        return departamentoRepository.getAllDepartaments();
    }

    @GetMapping("/departamentos/{coddep}")
    @Timed
    public List<Departamento> getDepartamentosByCode(@PathVariable String coddep){
        return departamentoRepository.getAllByCode(coddep);
    }

    @GetMapping("/provincias/{coddep}/{codprov}")
    @Timed
    public List<Provincia> getProvincia(@PathVariable String coddep,@PathVariable String codprov){
        return provinciaRepository.getAllByCodeDepProv(coddep, codprov);
    }

    @GetMapping("/provincias/{coddep}")
    @Timed
    public List<Provincia> getProvincias(@PathVariable String coddep){
        return provinciaRepository.getAllByCodeDep(coddep);
    }

    @GetMapping("/provincias/")
    @Timed
    public List<Provincia> getProvinciasByCode(){
        return provinciaRepository.getAllProvincias();
    }

    @GetMapping("/distritos/{coddep}/{codprov}/{coddist}")
    @Timed
    public List<Distrito> getDistrito(@PathVariable String coddep,@PathVariable String codprov,@PathVariable String coddist){
        return distritoRepository.getAllByCodeDepProvDist(coddep, codprov, coddist);
    }

    @GetMapping("/distritos/{coddep}/{codprov}")
    @Timed
    public List<Distrito> getDistritos(@PathVariable String coddep,@PathVariable String codprov){
        return distritoRepository.getAllByCodeDepProv(coddep, codprov);
    }
}
