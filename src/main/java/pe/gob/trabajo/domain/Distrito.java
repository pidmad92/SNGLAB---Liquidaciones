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
@Table(name = "SITB_DISTRITO", schema = "SIMINTRA1")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "SITB_DISTRITO")
public class Distrito {

    
    @Column(name = "v_coddep", nullable = false)
    private String vCoddep;

    @Column(name = "v_codpro")
    private String vCodpro;

    @Id
    @Column(name = "v_coddis")
    private String vCoddis;

    @Column(name = "v_desdis")
    private String vDesdis;

    @Column(name = "v_abrdis")
    private String vAbrdis;

    @Column(name = "v_codreg")
    private String vCodreg;

    @Column(name = "v_codzon")
    private String vCodzon;

    @Column(name = "v_coddepren")
    private String vCoddepren;

    @Column(name = "v_codproren")
    private String vCodproren;

    @Column(name = "v_coddisren")
    private String vCoddisren;

    @Column(name = "v_flgact")
    private String vFlgact;

    /**
     * @return the vCoddep
     */
    public String getvCoddep() {
        return vCoddep;
    }

    /**
     * @param vCoddep the vCoddep to set
     */
    public void setvCoddep(String vCoddep) {
        this.vCoddep = vCoddep;
    }

    /**
     * @return the vCodpro
     */
    public String getvCodpro() {
        return vCodpro;
    }

    /**
     * @param vCodpro the vCodpro to set
     */
    public void setvCodpro(String vCodpro) {
        this.vCodpro = vCodpro;
    }

    /**
     * @return the vCoddis
     */
    public String getvCoddis() {
        return vCoddis;
    }

    /**
     * @param vCoddis the vCoddis to set
     */
    public void setvCoddis(String vCoddis) {
        this.vCoddis = vCoddis;
    }

    /**
     * @return the vDesdis
     */
    public String getvDesdis() {
        return vDesdis;
    }

    /**
     * @param vDesdis the vDesdis to set
     */
    public void setvDesdis(String vDesdis) {
        this.vDesdis = vDesdis;
    }

    /**
     * @return the vAbrdis
     */
    public String getvAbrdis() {
        return vAbrdis;
    }

    /**
     * @param vAbrdis the vAbrdis to set
     */
    public void setvAbrdis(String vAbrdis) {
        this.vAbrdis = vAbrdis;
    }

    /**
     * @return the vCodreg
     */
    public String getvCodreg() {
        return vCodreg;
    }

    /**
     * @param vCodreg the vCodreg to set
     */
    public void setvCodreg(String vCodreg) {
        this.vCodreg = vCodreg;
    }

    /**
     * @return the vCodzon
     */
    public String getvCodzon() {
        return vCodzon;
    }

    /**
     * @param vCodzon the vCodzon to set
     */
    public void setvCodzon(String vCodzon) {
        this.vCodzon = vCodzon;
    }

    /**
     * @return the vCoddepren
     */
    public String getvCoddepren() {
        return vCoddepren;
    }

    /**
     * @param vCoddepren the vCoddepren to set
     */
    public void setvCoddepren(String vCoddepren) {
        this.vCoddepren = vCoddepren;
    }

    /**
     * @return the vCodproren
     */
    public String getvCodproren() {
        return vCodproren;
    }

    /**
     * @param vCodproren the vCodproren to set
     */
    public void setvCodproren(String vCodproren) {
        this.vCodproren = vCodproren;
    }

    /**
     * @return the vCoddisren
     */
    public String getvCoddisren() {
        return vCoddisren;
    }

    /**
     * @param vCoddisren the vCoddisren to set
     */
    public void setvCoddisren(String vCoddisren) {
        this.vCoddisren = vCoddisren;
    }

    /**
     * @return the vFlgact
     */
    public String getvFlgact() {
        return vFlgact;
    }

    /**
     * @param vFlgact the vFlgact to set
     */
    public void setvFlgact(String vFlgact) {
        this.vFlgact = vFlgact;
    }

}