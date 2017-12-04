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
 * LISTA MAESTRA DE TIPOS DE DOCUMENTOS DE IDENTIDAD
 */
@ApiModel(description = "LISTA MAESTRA DE TIPOS DE DOCUMENTOS DE IDENTIDAD")
@Entity
@Table(name = "gltbc_tipdocident")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_tipdocident")
public class Tipdocident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codtdiden", nullable = false)
    private Long id;

    /**
     * DESCRIPCION DEL TIPO DE DOCUMENTO DE IDENTIDAD.
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "DESCRIPCION DEL TIPO DE DOCUMENTO DE IDENTIDAD.", required = true)
    @Column(name = "v_desdocide", length = 100, nullable = false)
    private String vDesdocide;

    /**
     * NUMERO DE DIGITOS DEL  DOCUMENTO DE IDENTIDAD.
     */
    @NotNull
    @ApiModelProperty(value = "NUMERO DE DIGITOS DEL  DOCUMENTO DE IDENTIDAD.", required = true)
    @Column(name = "n_numdigi", nullable = false)
    private Integer nNumdigi;

    /**
     * DESCRIPCION CORTA DEL DOCUMENTO DE IDENTIDAD.
     */
    @NotNull
    @Size(max = 15)
    @ApiModelProperty(value = "DESCRIPCION CORTA DEL DOCUMENTO DE IDENTIDAD.", required = true)
    @Column(name = "v_descorta", length = 15, nullable = false)
    private String vDescorta;

    /**
     * CODIGO DEL USUARIO QUE REGISTRA.
     */
    @NotNull
    @ApiModelProperty(value = "CODIGO DEL USUARIO QUE REGISTRA.", required = true)
    @Column(name = "n_usuareg", nullable = false)
    private Integer nUsuareg;

    /**
     * FECHA Y HORA DEL REGISTRO.
     */
    @NotNull
    @ApiModelProperty(value = "FECHA Y HORA DEL REGISTRO.", required = true)
    @Column(name = "t_fecreg", nullable = false)
    private Instant tFecreg;

    /**
     * ESTADO ACTIVO DEL REGISTRO (1=ACTIVO, 0=INACTIVO)
     */
    @NotNull
    @ApiModelProperty(value = "ESTADO ACTIVO DEL REGISTRO (1=ACTIVO, 0=INACTIVO)", required = true)
    @Column(name = "n_flgactivo", nullable = false)
    private Boolean nFlgactivo;

    /**
     * CODIGO DE LA SEDE DONDE SE REGISTRA.
     */
    @NotNull
    @ApiModelProperty(value = "CODIGO DE LA SEDE DONDE SE REGISTRA.", required = true)
    @Column(name = "n_sedereg", nullable = false)
    private Integer nSedereg;

    /**
     * CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO.
     */
    @ApiModelProperty(value = "CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO.")
    @Column(name = "n_usuaupd")
    private Integer nUsuaupd;

    /**
     * CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO.
     */
    @ApiModelProperty(value = "CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO.")
    @Column(name = "t_fecupd")
    private Instant tFecupd;

    /**
     * CODIGO DE LA SEDE DONDE SE MODIFICA EL REGISTRO.
     */
    @ApiModelProperty(value = "CODIGO DE LA SEDE DONDE SE MODIFICA EL REGISTRO.")
    @Column(name = "n_sedeupd")
    private Integer nSedeupd;

    @OneToMany(mappedBy = "tipdocident")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pernatural> pernaturals = new HashSet<>();

    @OneToMany(mappedBy = "tipdocident")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Perjuridica> perjuridicas = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvDesdocide() {
        return vDesdocide;
    }

    public Tipdocident vDesdocide(String vDesdocide) {
        this.vDesdocide = vDesdocide;
        return this;
    }

    public void setvDesdocide(String vDesdocide) {
        this.vDesdocide = vDesdocide;
    }

    public Integer getnNumdigi() {
        return nNumdigi;
    }

    public Tipdocident nNumdigi(Integer nNumdigi) {
        this.nNumdigi = nNumdigi;
        return this;
    }

    public void setnNumdigi(Integer nNumdigi) {
        this.nNumdigi = nNumdigi;
    }

    public String getvDescorta() {
        return vDescorta;
    }

    public Tipdocident vDescorta(String vDescorta) {
        this.vDescorta = vDescorta;
        return this;
    }

    public void setvDescorta(String vDescorta) {
        this.vDescorta = vDescorta;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Tipdocident nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Tipdocident tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Tipdocident nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Tipdocident nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Tipdocident nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Tipdocident tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Tipdocident nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Pernatural> getPernaturals() {
        return pernaturals;
    }

    public Tipdocident pernaturals(Set<Pernatural> pernaturals) {
        this.pernaturals = pernaturals;
        return this;
    }

    public Tipdocident addPernatural(Pernatural pernatural) {
        this.pernaturals.add(pernatural);
        pernatural.setTipdocident(this);
        return this;
    }

    public Tipdocident removePernatural(Pernatural pernatural) {
        this.pernaturals.remove(pernatural);
        pernatural.setTipdocident(null);
        return this;
    }

    public void setPernaturals(Set<Pernatural> pernaturals) {
        this.pernaturals = pernaturals;
    }

    public Set<Perjuridica> getPerjuridicas() {
        return perjuridicas;
    }

    public Tipdocident perjuridicas(Set<Perjuridica> perjuridicas) {
        this.perjuridicas = perjuridicas;
        return this;
    }

    public Tipdocident addPerjuridica(Perjuridica perjuridica) {
        this.perjuridicas.add(perjuridica);
        perjuridica.setTipdocident(this);
        return this;
    }

    public Tipdocident removePerjuridica(Perjuridica perjuridica) {
        this.perjuridicas.remove(perjuridica);
        perjuridica.setTipdocident(null);
        return this;
    }

    public void setPerjuridicas(Set<Perjuridica> perjuridicas) {
        this.perjuridicas = perjuridicas;
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
        Tipdocident tipdocident = (Tipdocident) o;
        if (tipdocident.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipdocident.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tipdocident{" +
            "id=" + getId() +
            ", vDesdocide='" + getvDesdocide() + "'" +
            ", nNumdigi='" + getnNumdigi() + "'" +
            ", vDescorta='" + getvDescorta() + "'" +
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
