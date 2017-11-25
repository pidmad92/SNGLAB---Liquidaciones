package pe.gob.trabajo.domain;

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
import java.time.LocalDate;
import java.util.Objects;

/**
 * LISTA RESUMEN DEL CALCULO DE UN BENEFICIO SOCIAL EN UN PERIODO
 */
@ApiModel(description = "LISTA RESUMEN DEL CALCULO DE UN BENEFICIO SOCIAL EN UN PERIODO")
@Entity
@Table(name = "calperiodo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "calperiodo")
public class Calperiodo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * MONTO TOTAL CALCULADO DEL BENEFICIO SOCIAL EN EL PERIODO
     */
    @ApiModelProperty(value = "MONTO TOTAL CALCULADO DEL BENEFICIO SOCIAL EN EL PERIODO")
    @Column(name = "n_calper", precision=10, scale=2)
    private BigDecimal nCalper;

    /**
     * MONTO TOTAL2 CALCULADO DEL BENEFICIO SOCIAL EN EL PERIODO
     */
    @ApiModelProperty(value = "MONTO TOTAL2 CALCULADO DEL BENEFICIO SOCIAL EN EL PERIODO")
    @Column(name = "n_calper_2", precision=10, scale=2)
    private BigDecimal nCalper2;

    /**
     * NUMERO DE PERIODO EN LA LISTA PARA CALCULO
     */
    @NotNull
    @ApiModelProperty(value = "NUMERO DE PERIODO EN LA LISTA PARA CALCULO", required = true)
    @Column(name = "n_numper", nullable = false)
    private Integer nNumper;

    /**
     * FECHA DE INICIO DEL PERIODO
     */
    @NotNull
    @ApiModelProperty(value = "FECHA DE INICIO DEL PERIODO", required = true)
    @Column(name = "t_fecini", nullable = false)
    private LocalDate tFecini;

    /**
     * FECHA FIN DEL PERIODO
     */
    @NotNull
    @ApiModelProperty(value = "FECHA FIN DEL PERIODO", required = true)
    @Column(name = "t_fecfin", nullable = false)
    private LocalDate tFecfin;

    /**
     * TIEMPO NO COMPUTABLE
     */
    @ApiModelProperty(value = "TIEMPO NO COMPUTABLE")
    @Column(name = "n_tnocomput")
    private Integer nTnocomput;

    /**
     * EN CASO SE TRATASE DE VACACIONES - NUMERO DE DIAS GOZADOS EN EL PERIODO
     */
    @ApiModelProperty(value = "EN CASO SE TRATASE DE VACACIONES - NUMERO DE DIAS GOZADOS EN EL PERIODO")
    @Column(name = "n_dgozados")
    private Integer nDgozados;

    /**
     * EN CASO SE TRATASE DE REMUNERACIONES INSOLUTAS - NUMERO DE DIAS ADEUDOS EN EL PERIODO
     */
    @ApiModelProperty(value = "EN CASO SE TRATASE DE REMUNERACIONES INSOLUTAS - NUMERO DE DIAS ADEUDOS EN EL PERIODO")
    @Column(name = "n_dadeudos")
    private Integer nDadeudos;

    /**
     * Aï¿½O BASE EN CASO SEA DEL TIPO DE PERIODO ANUAL (1)
     */
    @ApiModelProperty(value = "Aï¿½O BASE EN CASO SEA DEL TIPO DE PERIODO ANUAL (1)")
    @Column(name = "n_anobase")
    private Integer nAnobase;

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

    @ManyToOne
    private Calbensoc calbensoc;

    @ManyToOne
    private Segsalud segsalud;

    @ManyToOne
    private Estperical estperical;

    @ManyToOne
    private Tipcalperi tipcalperi;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getnCalper() {
        return nCalper;
    }

    public Calperiodo nCalper(BigDecimal nCalper) {
        this.nCalper = nCalper;
        return this;
    }

    public void setnCalper(BigDecimal nCalper) {
        this.nCalper = nCalper;
    }

    public BigDecimal getnCalper2() {
        return nCalper2;
    }

    public Calperiodo nCalper2(BigDecimal nCalper2) {
        this.nCalper2 = nCalper2;
        return this;
    }

    public void setnCalper2(BigDecimal nCalper2) {
        this.nCalper2 = nCalper2;
    }

    public Integer getnNumper() {
        return nNumper;
    }

    public Calperiodo nNumper(Integer nNumper) {
        this.nNumper = nNumper;
        return this;
    }

    public void setnNumper(Integer nNumper) {
        this.nNumper = nNumper;
    }

    public LocalDate gettFecini() {
        return tFecini;
    }

    public Calperiodo tFecini(LocalDate tFecini) {
        this.tFecini = tFecini;
        return this;
    }

    public void settFecini(LocalDate tFecini) {
        this.tFecini = tFecini;
    }

    public LocalDate gettFecfin() {
        return tFecfin;
    }

    public Calperiodo tFecfin(LocalDate tFecfin) {
        this.tFecfin = tFecfin;
        return this;
    }

    public void settFecfin(LocalDate tFecfin) {
        this.tFecfin = tFecfin;
    }

    public Integer getnTnocomput() {
        return nTnocomput;
    }

    public Calperiodo nTnocomput(Integer nTnocomput) {
        this.nTnocomput = nTnocomput;
        return this;
    }

    public void setnTnocomput(Integer nTnocomput) {
        this.nTnocomput = nTnocomput;
    }

    public Integer getnDgozados() {
        return nDgozados;
    }

    public Calperiodo nDgozados(Integer nDgozados) {
        this.nDgozados = nDgozados;
        return this;
    }

    public void setnDgozados(Integer nDgozados) {
        this.nDgozados = nDgozados;
    }

    public Integer getnDadeudos() {
        return nDadeudos;
    }

    public Calperiodo nDadeudos(Integer nDadeudos) {
        this.nDadeudos = nDadeudos;
        return this;
    }

    public void setnDadeudos(Integer nDadeudos) {
        this.nDadeudos = nDadeudos;
    }

    public Integer getnAnobase() {
        return nAnobase;
    }

    public Calperiodo nAnobase(Integer nAnobase) {
        this.nAnobase = nAnobase;
        return this;
    }

    public void setnAnobase(Integer nAnobase) {
        this.nAnobase = nAnobase;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Calperiodo nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Calperiodo tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Calperiodo nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Calperiodo nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Calperiodo nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Calperiodo tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Calperiodo nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Calbensoc getCalbensoc() {
        return calbensoc;
    }

    public Calperiodo calbensoc(Calbensoc calbensoc) {
        this.calbensoc = calbensoc;
        return this;
    }

    public void setCalbensoc(Calbensoc calbensoc) {
        this.calbensoc = calbensoc;
    }

    public Segsalud getSegsalud() {
        return segsalud;
    }

    public Calperiodo segsalud(Segsalud segsalud) {
        this.segsalud = segsalud;
        return this;
    }

    public void setSegsalud(Segsalud segsalud) {
        this.segsalud = segsalud;
    }

    public Estperical getEstperical() {
        return estperical;
    }

    public Calperiodo estperical(Estperical estperical) {
        this.estperical = estperical;
        return this;
    }

    public void setEstperical(Estperical estperical) {
        this.estperical = estperical;
    }

    public Tipcalperi getTipcalperi() {
        return tipcalperi;
    }

    public Calperiodo tipcalperi(Tipcalperi tipcalperi) {
        this.tipcalperi = tipcalperi;
        return this;
    }

    public void setTipcalperi(Tipcalperi tipcalperi) {
        this.tipcalperi = tipcalperi;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Calperiodo calperiodo = (Calperiodo) o;
        if (calperiodo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), calperiodo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Calperiodo{" +
            "id=" + getId() +
            ", nCalper='" + getnCalper() + "'" +
            ", nCalper2='" + getnCalper2() + "'" +
            ", nNumper='" + getnNumper() + "'" +
            ", tFecini='" + gettFecini() + "'" +
            ", tFecfin='" + gettFecfin() + "'" +
            ", nTnocomput='" + getnTnocomput() + "'" +
            ", nDgozados='" + getnDgozados() + "'" +
            ", nDadeudos='" + getnDadeudos() + "'" +
            ", nAnobase='" + getnAnobase() + "'" +
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
