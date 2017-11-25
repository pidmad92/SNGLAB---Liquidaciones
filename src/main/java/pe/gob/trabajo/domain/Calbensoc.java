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
 * LISTA RESUMEN DEL CALCULO DE BENEFICIOS SOCIALES EN UNA LIQUIDACION
 */
@ApiModel(description = "LISTA RESUMEN DEL CALCULO DE BENEFICIOS SOCIALES EN UNA LIQUIDACION")
@Entity
@Table(name = "calbensoc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "calbensoc")
public class Calbensoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * MONTO TOTAL CALCULADO DEL BENEFICIO SOCIAL
     */
    @ApiModelProperty(value = "MONTO TOTAL CALCULADO DEL BENEFICIO SOCIAL")
    @Column(name = "n_calbens", precision=10, scale=2)
    private BigDecimal nCalbens;

    /**
     * MONTO TOTAL2 CALCULADO DEL BENEFICIO SOCIAL
     */
    @ApiModelProperty(value = "MONTO TOTAL2 CALCULADO DEL BENEFICIO SOCIAL")
    @Column(name = "n_calbens_2", precision=10, scale=2)
    private BigDecimal nCalbens2;

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
    private Bensocial bensocial;

    @OneToMany(mappedBy = "calbensoc")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Calperiodo> calperiodos = new HashSet<>();

    @ManyToOne
    private Liquidacion liquidacion;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getnCalbens() {
        return nCalbens;
    }

    public Calbensoc nCalbens(BigDecimal nCalbens) {
        this.nCalbens = nCalbens;
        return this;
    }

    public void setnCalbens(BigDecimal nCalbens) {
        this.nCalbens = nCalbens;
    }

    public BigDecimal getnCalbens2() {
        return nCalbens2;
    }

    public Calbensoc nCalbens2(BigDecimal nCalbens2) {
        this.nCalbens2 = nCalbens2;
        return this;
    }

    public void setnCalbens2(BigDecimal nCalbens2) {
        this.nCalbens2 = nCalbens2;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Calbensoc nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Calbensoc tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Calbensoc nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Calbensoc nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Calbensoc nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Calbensoc tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Calbensoc nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Bensocial getBensocial() {
        return bensocial;
    }

    public Calbensoc bensocial(Bensocial bensocial) {
        this.bensocial = bensocial;
        return this;
    }

    public void setBensocial(Bensocial bensocial) {
        this.bensocial = bensocial;
    }

    public Set<Calperiodo> getCalperiodos() {
        return calperiodos;
    }

    public Calbensoc calperiodos(Set<Calperiodo> calperiodos) {
        this.calperiodos = calperiodos;
        return this;
    }

    public Calbensoc addCalperiodo(Calperiodo calperiodo) {
        this.calperiodos.add(calperiodo);
        calperiodo.setCalbensoc(this);
        return this;
    }

    public Calbensoc removeCalperiodo(Calperiodo calperiodo) {
        this.calperiodos.remove(calperiodo);
        calperiodo.setCalbensoc(null);
        return this;
    }

    public void setCalperiodos(Set<Calperiodo> calperiodos) {
        this.calperiodos = calperiodos;
    }

    public Liquidacion getLiquidacion() {
        return liquidacion;
    }

    public Calbensoc liquidacion(Liquidacion liquidacion) {
        this.liquidacion = liquidacion;
        return this;
    }

    public void setLiquidacion(Liquidacion liquidacion) {
        this.liquidacion = liquidacion;
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
        Calbensoc calbensoc = (Calbensoc) o;
        if (calbensoc.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), calbensoc.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Calbensoc{" +
            "id=" + getId() +
            ", nCalbens='" + getnCalbens() + "'" +
            ", nCalbens2='" + getnCalbens2() + "'" +
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
