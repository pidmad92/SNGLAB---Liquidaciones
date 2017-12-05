package pe.gob.trabajo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 *
 * @author chuangal
 * SITB_DEPARTAMENTO
 */
@Entity
@Table(name = "SITB_DEPARTAMENTO", schema = "SIMINTRA1")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "SITB_DEPARTAMENTO")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "v_coddep", nullable = false)
    private String vCoddep;

    @Column(name = "v_desdep")
    private String vDesdep;

    @Column(name = "v_coddepren")
    private String vCoddepren;

    @Column(name = "v_flgact")
    private String vFlgact;

    /**
     * @return the vCoddep
     */
    public String getvCoddep() {
        return vCoddep;
    }

    /**
     * @return the vDesdep
     */
    public String getvDesdep() {
        return vDesdep;
    }

    /**
     * @return the vCoddepren
     */
    public String getvCoddepren() {
        return vCoddepren;
    }

    /**
     * @return the vFlgact
     */
    public String getvFlgact() {
        return vFlgact;
    }

}