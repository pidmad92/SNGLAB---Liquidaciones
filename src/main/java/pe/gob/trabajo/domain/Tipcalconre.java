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
 * LISTA MAESTRA DE LOS TIPOS DE CALCULO PARA LOS CONCEPTOS REMUNERATIVOS (FIJO, VARIABLE)
 */
@ApiModel(description = "LISTA MAESTRA DE LOS TIPOS DE CALCULO PARA LOS CONCEPTOS REMUNERATIVOS (FIJO, VARIABLE)")
@Entity
@Table(name = "litbc_tipcalconre")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "litbc_tipcalconre")
public class Tipcalconre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codtcal", nullable = false)
    private Long id;

    /**
     * NOMBRE DEL TIPO DE CALCULO DE CONCEPTO REMUNERATIVO
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "NOMBRE DEL TIPO DE CALCULO DE CONCEPTO REMUNERATIVO", required = true)
    @Column(name = "v_nomtcal", length = 100, nullable = false)
    private String vNomtcal;

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

    @OneToMany(mappedBy = "tipcalconre")
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

    public String getvNomtcal() {
        return vNomtcal;
    }

    public Tipcalconre vNomtcal(String vNomtcal) {
        this.vNomtcal = vNomtcal;
        return this;
    }

    public void setvNomtcal(String vNomtcal) {
        this.vNomtcal = vNomtcal;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Tipcalconre nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Tipcalconre tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Tipcalconre nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Tipcalconre nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Tipcalconre nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Tipcalconre tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Tipcalconre nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Conceprem> getConceprems() {
        return conceprems;
    }

    public Tipcalconre conceprems(Set<Conceprem> conceprems) {
        this.conceprems = conceprems;
        return this;
    }

    public Tipcalconre addConceprem(Conceprem conceprem) {
        this.conceprems.add(conceprem);
        conceprem.setTipcalconre(this);
        return this;
    }

    public Tipcalconre removeConceprem(Conceprem conceprem) {
        this.conceprems.remove(conceprem);
        conceprem.setTipcalconre(null);
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
        Tipcalconre tipcalconre = (Tipcalconre) o;
        if (tipcalconre.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipcalconre.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tipcalconre{" +
            "id=" + getId() +
            ", vNomtcal='" + getvNomtcal() + "'" +
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
