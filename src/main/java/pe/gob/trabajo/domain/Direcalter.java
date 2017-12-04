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
 * LISTA DE RAZON SOCIAL Y DIRECCION ALTERNATIVA PARA DOCUMENTO DE INSPECCION POLICIAL
 */
@ApiModel(description = "LISTA DE RAZON SOCIAL Y DIRECCION ALTERNATIVA PARA DOCUMENTO DE INSPECCION POLICIAL")
@Entity
@Table(name = "gltbc_direcalter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_direcalter")
public class Direcalter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_coddalter", nullable = false)
    private Long id;

    /**
     * RAZON SOCIAL ALTERNATIVA
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "RAZON SOCIAL ALTERNATIVA", required = true)
    @Column(name = "v_razsocial", length = 100, nullable = false)
    private String vRazsocial;

    /**
     * DIRECCION DE LA RAZON SOCIAL ALTERNATIVA.
     */
    @NotNull
    @Size(max = 200)
    @ApiModelProperty(value = "DIRECCION DE LA RAZON SOCIAL ALTERNATIVA.", required = true)
    @Column(name = "v_direccion", length = 200, nullable = false)
    private String vDireccion;

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

    @OneToMany(mappedBy = "direcalter")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Motateselec> motateselecs = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvRazsocial() {
        return vRazsocial;
    }

    public Direcalter vRazsocial(String vRazsocial) {
        this.vRazsocial = vRazsocial;
        return this;
    }

    public void setvRazsocial(String vRazsocial) {
        this.vRazsocial = vRazsocial;
    }

    public String getvDireccion() {
        return vDireccion;
    }

    public Direcalter vDireccion(String vDireccion) {
        this.vDireccion = vDireccion;
        return this;
    }

    public void setvDireccion(String vDireccion) {
        this.vDireccion = vDireccion;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Direcalter nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Direcalter tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Direcalter nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Direcalter nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Direcalter nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Direcalter tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Direcalter nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Motateselec> getMotateselecs() {
        return motateselecs;
    }

    public Direcalter motateselecs(Set<Motateselec> motateselecs) {
        this.motateselecs = motateselecs;
        return this;
    }

    public Direcalter addMotateselec(Motateselec motateselec) {
        this.motateselecs.add(motateselec);
        motateselec.setDirecalter(this);
        return this;
    }

    public Direcalter removeMotateselec(Motateselec motateselec) {
        this.motateselecs.remove(motateselec);
        motateselec.setDirecalter(null);
        return this;
    }

    public void setMotateselecs(Set<Motateselec> motateselecs) {
        this.motateselecs = motateselecs;
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
        Direcalter direcalter = (Direcalter) o;
        if (direcalter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), direcalter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Direcalter{" +
            "id=" + getId() +
            ", vRazsocial='" + getvRazsocial() + "'" +
            ", vDireccion='" + getvDireccion() + "'" +
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
