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
 * LISTA RESUMEN DEL CALCULO DE LA RCM EN UN PERIODO
 */
@ApiModel(description = "LISTA RESUMEN DEL CALCULO DE LA RCM EN UN PERIODO")
@Entity
@Table(name = "limvc_calrcmperi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "limvc_calrcmperi")
public class Calrcmperi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codcalrcm", nullable = false)
    private Long id;

    /**
     * MONTO TOTAL CALCULADO DEL RCM DEL BENEFICIO SOCIAL EN EL PERIODO
     */
    @ApiModelProperty(value = "MONTO TOTAL CALCULADO DEL RCM DEL BENEFICIO SOCIAL EN EL PERIODO")
    @Column(name = "n_calrcmper", precision=10, scale=2)
    private BigDecimal nCalrcmper;

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
    @JoinColumn(unique = true, name = "n_codcalper")
    private Calperiodo calperiodo;

    @OneToMany(mappedBy = "calrcmperi")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Conceprem> conceprems = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getnCalrcmper() {
        return nCalrcmper;
    }

    public Calrcmperi nCalrcmper(BigDecimal nCalrcmper) {
        this.nCalrcmper = nCalrcmper;
        return this;
    }

    public void setnCalrcmper(BigDecimal nCalrcmper) {
        this.nCalrcmper = nCalrcmper;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Calrcmperi nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Calrcmperi tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Calrcmperi nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Calrcmperi nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Calrcmperi nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Calrcmperi tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Calrcmperi nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Calperiodo getCalperiodo() {
        return calperiodo;
    }

    public Calrcmperi calperiodo(Calperiodo calperiodo) {
        this.calperiodo = calperiodo;
        return this;
    }

    public void setCalperiodo(Calperiodo calperiodo) {
        this.calperiodo = calperiodo;
    }

    public Set<Conceprem> getConceprems() {
        return conceprems;
    }

    public Calrcmperi conceprems(Set<Conceprem> conceprems) {
        this.conceprems = conceprems;
        return this;
    }

    public Calrcmperi addConceprem(Conceprem conceprem) {
        this.conceprems.add(conceprem);
        conceprem.setCalrcmperi(this);
        return this;
    }

    public Calrcmperi removeConceprem(Conceprem conceprem) {
        this.conceprems.remove(conceprem);
        conceprem.setCalrcmperi(null);
        return this;
    }

    public void setConceprems(Set<Conceprem> conceprems) {
        this.conceprems = conceprems;
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
        Calrcmperi calrcmperi = (Calrcmperi) o;
        if (calrcmperi.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), calrcmperi.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Calrcmperi{" +
            "id=" + getId() +
            ", nCalrcmper='" + getnCalrcmper() + "'" +
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
