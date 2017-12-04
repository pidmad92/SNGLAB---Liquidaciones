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
 * LISTA MAESTRA DE LAS ACTIVIDADES ECONOMICAS
 */
@ApiModel(description = "LISTA MAESTRA DE LAS ACTIVIDADES ECONOMICAS")
@Entity
@Table(name = "gltbc_actiecon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_actiecon")
public class Actiecon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codacteco", nullable = false)
    private Long id;

    /**
     * CLASIFICACION INDUSTRIAL INTERNACIONAL UNIFORME DE LA ACTIVIDAD ECONOMICA.
     */
    @NotNull
    @Size(max = 5)
    @ApiModelProperty(value = "CLASIFICACION INDUSTRIAL INTERNACIONAL UNIFORME DE LA ACTIVIDAD ECONOMICA.", required = true)
    @Column(name = "v_ciuuaceco", length = 5, nullable = false)
    private String vCiuuaceco;

    /**
     * DESCRIPCION DE LA ACTIVIDAD ECONOMICA.
     */
    @NotNull
    @Size(max = 200)
    @ApiModelProperty(value = "DESCRIPCION DE LA ACTIVIDAD ECONOMICA.", required = true)
    @Column(name = "v_desacteco", length = 200, nullable = false)
    private String vDesacteco;

    /**
     * CODIGO DE USUARIO QUE REGISTRA.
     */
    @NotNull
    @ApiModelProperty(value = "CODIGO DE USUARIO QUE REGISTRA.", required = true)
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
     * CODIGO DE LA SEDE QUE REGISTRA.
     */
    @NotNull
    @ApiModelProperty(value = "CODIGO DE LA SEDE QUE REGISTRA.", required = true)
    @Column(name = "n_sedereg", nullable = false)
    private Integer nSedereg;

    /**
     * CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO
     */
    @ApiModelProperty(value = "CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO")
    @Column(name = "n_usuaupd")
    private Integer nUsuaupd;

    /**
     * FECHA Y HORA DE LA MODIFICACION DEL REGISTRO.
     */
    @ApiModelProperty(value = "FECHA Y HORA DE LA MODIFICACION DEL REGISTRO.")
    @Column(name = "t_fecupd")
    private Instant tFecupd;

    /**
     * CODIGO DE LA SEDE DONDE SE MODIFICA EL REGISTRO.
     */
    @ApiModelProperty(value = "CODIGO DE LA SEDE DONDE SE MODIFICA EL REGISTRO.")
    @Column(name = "n_sedeupd")
    private Integer nSedeupd;

    @OneToMany(mappedBy = "actiecon")
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

    public String getvCiuuaceco() {
        return vCiuuaceco;
    }

    public Actiecon vCiuuaceco(String vCiuuaceco) {
        this.vCiuuaceco = vCiuuaceco;
        return this;
    }

    public void setvCiuuaceco(String vCiuuaceco) {
        this.vCiuuaceco = vCiuuaceco;
    }

    public String getvDesacteco() {
        return vDesacteco;
    }

    public Actiecon vDesacteco(String vDesacteco) {
        this.vDesacteco = vDesacteco;
        return this;
    }

    public void setvDesacteco(String vDesacteco) {
        this.vDesacteco = vDesacteco;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Actiecon nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Actiecon tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Actiecon nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Actiecon nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Actiecon nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Actiecon tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Actiecon nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Perjuridica> getPerjuridicas() {
        return perjuridicas;
    }

    public Actiecon perjuridicas(Set<Perjuridica> perjuridicas) {
        this.perjuridicas = perjuridicas;
        return this;
    }

    public Actiecon addPerjuridica(Perjuridica perjuridica) {
        this.perjuridicas.add(perjuridica);
        perjuridica.setActiecon(this);
        return this;
    }

    public Actiecon removePerjuridica(Perjuridica perjuridica) {
        this.perjuridicas.remove(perjuridica);
        perjuridica.setActiecon(null);
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
        Actiecon actiecon = (Actiecon) o;
        if (actiecon.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actiecon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Actiecon{" +
            "id=" + getId() +
            ", vCiuuaceco='" + getvCiuuaceco() + "'" +
            ", vDesacteco='" + getvDesacteco() + "'" +
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
