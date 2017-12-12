package pe.gob.trabajo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * LISTA DE CONCEPTOS REMUNERATIVOS PARA UN CALCULO DE RCM
 */
@ApiModel(description = "LISTA DE CONCEPTOS REMUNERATIVOS PARA UN CALCULO DE RCM")
@Entity
@Table(name = "limvc_conceprem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "limvc_conceprem")
public class Conceprem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codconrem", nullable = false)
    private Long id;

    /**
     * NOMBRE DEL CONCEPTO REMUNERATIVO
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "NOMBRE DEL CONCEPTO REMUNERATIVO", required = true)
    @Column(name = "v_nomconrem", length = 100, nullable = false)
    private String vNomconrem;

    /**
     * VALOR O MONTO DEL CONCEPTO REMUNERATIVO
     */
    @ApiModelProperty(value = "VALOR O MONTO DEL CONCEPTO REMUNERATIVO")
    @Column(name = "n_valconrem", precision=10, scale=2)
    private BigDecimal nValconrem;

    /**
     * AUDITORIA USUARIO DE REGISTRO
     */
    @NotNull
    @ApiModelProperty(value = "AUDITORIA USUARIO DE REGISTRO", required = true)
    @Column(name = "n_usuareg", nullable = false)
    private Integer nUsuareg;

    /**
     * AUDITORIA FECHA DE REGISTRO
     */
    @NotNull
    @ApiModelProperty(value = "AUDITORIA FECHA DE REGISTRO", required = true)
    @Column(name = "t_fecreg", nullable = false)
    private Instant tFecreg;

    /**
     * AUDITORIA FLAG DE ACTIVO
     */
    @NotNull
    @ApiModelProperty(value = "AUDITORIA FLAG DE ACTIVO", required = true)
    @Column(name = "n_flgactivo", nullable = false)
    private Boolean nFlgactivo;

    /**
     * AUDITORIA SEDE DE REGISTRO
     */
    @NotNull
    @ApiModelProperty(value = "AUDITORIA SEDE DE REGISTRO", required = true)
    @Column(name = "n_sedereg", nullable = false)
    private Integer nSedereg;

    /**
     * AUDITORIA USUARIO DE ACTUALIZACION
     */
    @ApiModelProperty(value = "AUDITORIA USUARIO DE ACTUALIZACION")
    @Column(name = "n_usuaupd")
    private Integer nUsuaupd;

    /**
     * AUDITORIA FECHA DE ACTUALIZACION
     */
    @ApiModelProperty(value = "AUDITORIA FECHA DE ACTUALIZACION")
    @Column(name = "t_fecupd")
    private Instant tFecupd;

    /**
     * AUDITORIA SEDE DE ACTUALIZACION
     */
    @ApiModelProperty(value = "AUDITORIA SEDE DE ACTUALIZACION")
    @Column(name = "n_sedeupd")
    private Integer nSedeupd;

    // /**
    //  * CODIGO RECURSIVO DEL HIJO
    //  */
    // @Column(name = "n_codcrsup")
    // private Long nCodcrsup;

    @ManyToOne
    @JoinColumn(name = "n_codcalrcm")
    private Calrcmperi calrcmperi;

    @ManyToOne
    @JoinColumn(name = "n_codcrsup")
    private Conceprem conceprem;

    @OneToMany(mappedBy = "conceprem")
    //@JsonIgnore
    // @JoinColumn(name = "n_codconrem")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Conceprem> conceprems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "n_codtcal")
    private Tipcalconre tipcalconre;

    @ManyToOne
    @JoinColumn(name = "n_codtipcr")
    private Tipconrem tipconrem;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvNomconrem() {
        return vNomconrem;
    }

    public Conceprem vNomconrem(String vNomconrem) {
        this.vNomconrem = vNomconrem;
        return this;
    }

    public void setvNomconrem(String vNomconrem) {
        this.vNomconrem = vNomconrem;
    }

    public BigDecimal getnValconrem() {
        return nValconrem;
    }

    public Conceprem nValconrem(BigDecimal nValconrem) {
        this.nValconrem = nValconrem;
        return this;
    }

    public void setnValconrem(BigDecimal nValconrem) {
        this.nValconrem = nValconrem;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Conceprem nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Conceprem tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Conceprem nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Conceprem nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Conceprem nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Conceprem tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Conceprem nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Calrcmperi getCalrcmperi() {
        return calrcmperi;
    }

    public Conceprem calrcmperi(Calrcmperi calrcmperi) {
        this.calrcmperi = calrcmperi;
        return this;
    }

    public void setCalrcmperi(Calrcmperi calrcmperi) {
        this.calrcmperi = calrcmperi;
    }

    public Conceprem getConceprem() {
        return conceprem;
    }

    public Conceprem conceprem(Conceprem conceprem) {
        this.conceprem = conceprem;
        return this;
    }

    public void setConceprem(Conceprem conceprem) {
        this.conceprem = conceprem;
    }

    public Set<Conceprem> getConceprems() {
        return conceprems;
    }

    public Conceprem conceprems(Set<Conceprem> conceprems) {
        this.conceprems = conceprems;
        return this;
    }

    public Conceprem addConceprem(Conceprem conceprem) {
        this.conceprems.add(conceprem);
        conceprem.setConceprem(this);
        return this;
    }

    public Conceprem removeConceprem(Conceprem conceprem) {
        this.conceprems.remove(conceprem);
        conceprem.setConceprem(null);
        return this;
    }

    public void setConceprems(Set<Conceprem> conceprems) {
        this.conceprems = conceprems;
    }

    public Tipcalconre getTipcalconre() {
        return tipcalconre;
    }

    public Conceprem tipcalconre(Tipcalconre tipcalconre) {
        this.tipcalconre = tipcalconre;
        return this;
    }

    public void setTipcalconre(Tipcalconre tipcalconre) {
        this.tipcalconre = tipcalconre;
    }

    public Tipconrem getTipconrem() {
        return tipconrem;
    }

    public Conceprem tipconrem(Tipconrem tipconrem) {
        this.tipconrem = tipconrem;
        return this;
    }

    public void setTipconrem(Tipconrem tipconrem) {
        this.tipconrem = tipconrem;
    }
/*
   del atributo recursivo
*/
    // public Long getnCodcrsup() {
    //     return nCodcrsup;
    // }

    // public Conceprem nCodcrsup(Long nCodcrsup) {
    //     this.nCodcrsup = nCodcrsup;
    //     return this;
    // }

    // public void setnCodcrsup(Long nCodcrsup) {
    //     this.nCodcrsup = nCodcrsup;
    // }

    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conceprem conceprem = (Conceprem) o;
        if (conceprem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conceprem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Conceprem{" +
            "id=" + getId() +
            ", vNomconrem='" + getvNomconrem() + "'" +
            ", nValconrem='" + getnValconrem() + "'" +
            ", nUsuareg='" + getnUsuareg() + "'" +
            ", tFecreg='" + gettFecreg() + "'" +
            ", nFlgactivo='" + isnFlgactivo() + "'" +
            ", nSedereg='" + getnSedereg() + "'" +
            ", nUsuaupd='" + getnUsuaupd() + "'" +
            ", tFecupd='" + gettFecupd() + "'" +
            ", nSedeupd='" + getnSedeupd() + "'" +
            "}";
    }
}
