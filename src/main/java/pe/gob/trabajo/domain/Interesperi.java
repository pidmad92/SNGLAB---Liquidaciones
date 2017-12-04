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
import java.util.Objects;

/**
 * LISTA RESUMEN DEL CALCULO DEL INTERES EN UN PERIODO
 */
@ApiModel(description = "LISTA RESUMEN DEL CALCULO DEL INTERES EN UN PERIODO")
@Entity
@Table(name = "limvc_interesperi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "limvc_interesperi")
public class Interesperi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codintper", nullable = false)
    private Long id;

    /**
     * MONTO TOTAL CALCULADO DEL INTERES DEL BENEFICIO SOCIAL EN EL PERIODO
     */
    @ApiModelProperty(value = "MONTO TOTAL CALCULADO DEL INTERES DEL BENEFICIO SOCIAL EN EL PERIODO")
    @Column(name = "n_valintper", precision=10, scale=2)
    private BigDecimal nValintper;

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

    @OneToOne
    @JoinColumn(unique = true)
    private Calperiodo calperiodo;

    @ManyToOne
    @JoinColumn(name = "n_codtipint")
    private Tipinteres tipinteres;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getnValintper() {
        return nValintper;
    }

    public Interesperi nValintper(BigDecimal nValintper) {
        this.nValintper = nValintper;
        return this;
    }

    public void setnValintper(BigDecimal nValintper) {
        this.nValintper = nValintper;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Interesperi nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Interesperi tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Interesperi nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Interesperi nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Interesperi nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Interesperi tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Interesperi nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Calperiodo getCalperiodo() {
        return calperiodo;
    }

    public Interesperi calperiodo(Calperiodo calperiodo) {
        this.calperiodo = calperiodo;
        return this;
    }

    public void setCalperiodo(Calperiodo calperiodo) {
        this.calperiodo = calperiodo;
    }

    public Tipinteres getTipinteres() {
        return tipinteres;
    }

    public Interesperi tipinteres(Tipinteres tipinteres) {
        this.tipinteres = tipinteres;
        return this;
    }

    public void setTipinteres(Tipinteres tipinteres) {
        this.tipinteres = tipinteres;
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
        Interesperi interesperi = (Interesperi) o;
        if (interesperi.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), interesperi.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Interesperi{" +
            "id=" + getId() +
            ", nValintper='" + getnValintper() + "'" +
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
