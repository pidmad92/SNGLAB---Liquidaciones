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
@Table(name = "SITB_PROVINCIA", schema = "SIMINTRA1")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "SITB_PROVINCIA")
public class Provincia {
    
    @Column(name = "v_coddep", nullable = false)
    private String vCoddep;

    @Id
    @Column(name = "v_codpro")
    private String vCodpro;

    @Column(name = "v_despro")
    private String vDespro;

    @Column(name = "v_coddepren")
    private String vCoddepren;

    @Column(name = "v_codproren")
    private String vCodproren;

    @Column(name = "v_flgact")
    private String vFlgact;

    /**
     * @return the vCoddep
     */
    public String getvCoddep() {
        return vCoddep;
    }

    /**
     * @return the vCodpro
     */
    public String getvCodpro() {
        return vCodpro;
    }

    /**
     * @return the vDespro
     */
    public String getvDespro() {
        return vDespro;
    }

    /**
     * @return the vCoddepren
     */
    public String getvCoddepren() {
        return vCoddepren;
    }

    /**
     * @return the vCodproren
     */
    public String getvCodproren() {
        return vCodproren;
    }

    /**
     * @return the vFlgact
     */
    public String getvFlgact() {
        return vFlgact;
    }
    
    
    
}
