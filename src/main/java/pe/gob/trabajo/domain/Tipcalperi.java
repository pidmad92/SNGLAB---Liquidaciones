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
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * LISTA MAESTRA DE LOS TIPOS DE CALCULO PARA LOS PERIODOS (MENSUAL, SEMESTRAL, ANUAL)
 */
@ApiModel(description = "LISTA MAESTRA DE LOS TIPOS DE CALCULO PARA LOS PERIODOS (MENSUAL, SEMESTRAL, ANUAL)")
@Entity
@Table(name = "tipcalperi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tipcalperi")
public class Tipcalperi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * NOMBRE DEL TIPO DE CALCULO DEL PERIODO
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "NOMBRE DEL TIPO DE CALCULO DEL PERIODO", required = true)
    @Column(name = "v_nomtipcal", length = 100, nullable = false)
    private String vNomtipcal;

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

    @OneToMany(mappedBy = "tipcalperi")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Calperiodo> calperiodos = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvNomtipcal() {
        return vNomtipcal;
    }

    public Tipcalperi vNomtipcal(String vNomtipcal) {
        this.vNomtipcal = vNomtipcal;
        return this;
    }

    public void setvNomtipcal(String vNomtipcal) {
        this.vNomtipcal = vNomtipcal;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Tipcalperi nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Tipcalperi tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Tipcalperi nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Tipcalperi nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Tipcalperi nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Tipcalperi tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Tipcalperi nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Calperiodo> getCalperiodos() {
        return calperiodos;
    }

    public Tipcalperi calperiodos(Set<Calperiodo> calperiodos) {
        this.calperiodos = calperiodos;
        return this;
    }

    public Tipcalperi addCalperiodo(Calperiodo calperiodo) {
        this.calperiodos.add(calperiodo);
        calperiodo.setTipcalperi(this);
        return this;
    }

    public Tipcalperi removeCalperiodo(Calperiodo calperiodo) {
        this.calperiodos.remove(calperiodo);
        calperiodo.setTipcalperi(null);
        return this;
    }

    public void setCalperiodos(Set<Calperiodo> calperiodos) {
        this.calperiodos = calperiodos;
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
        Tipcalperi tipcalperi = (Tipcalperi) o;
        if (tipcalperi.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipcalperi.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tipcalperi{" +
            "id=" + getId() +
            ", vNomtipcal='" + getvNomtipcal() + "'" +
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
