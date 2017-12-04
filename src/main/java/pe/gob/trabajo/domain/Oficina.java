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
 * LISTA MAESTRA DE LAS OFICINAS
 */
@ApiModel(description = "LISTA MAESTRA DE LAS OFICINAS")
@Entity
@Table(name = "gltbc_oficina")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_oficina")
public class Oficina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codofic", nullable = false)
    private Long id;

    /**
     * DESCRIPCION DE LA OFICINA.
     */
    @NotNull
    @Size(max = 200)
    @ApiModelProperty(value = "DESCRIPCION DE LA OFICINA.", required = true)
    @Column(name = "v_desofic", length = 200, nullable = false)
    private String vDesofic;

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

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Atencion> atencions = new HashSet<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Motatenofic> motatenofics = new HashSet<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pasegl> pasegls = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvDesofic() {
        return vDesofic;
    }

    public Oficina vDesofic(String vDesofic) {
        this.vDesofic = vDesofic;
        return this;
    }

    public void setvDesofic(String vDesofic) {
        this.vDesofic = vDesofic;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Oficina nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Oficina tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Oficina nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Oficina nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Oficina nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Oficina tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Oficina nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Atencion> getAtencions() {
        return atencions;
    }

    public Oficina atencions(Set<Atencion> atencions) {
        this.atencions = atencions;
        return this;
    }

    public Oficina addAtencion(Atencion atencion) {
        this.atencions.add(atencion);
        atencion.setOficina(this);
        return this;
    }

    public Oficina removeAtencion(Atencion atencion) {
        this.atencions.remove(atencion);
        atencion.setOficina(null);
        return this;
    }

    public void setAtencions(Set<Atencion> atencions) {
        this.atencions = atencions;
    }

    public Set<Motatenofic> getMotatenofics() {
        return motatenofics;
    }

    public Oficina motatenofics(Set<Motatenofic> motatenofics) {
        this.motatenofics = motatenofics;
        return this;
    }

    public Oficina addMotatenofic(Motatenofic motatenofic) {
        this.motatenofics.add(motatenofic);
        motatenofic.setOficina(this);
        return this;
    }

    public Oficina removeMotatenofic(Motatenofic motatenofic) {
        this.motatenofics.remove(motatenofic);
        motatenofic.setOficina(null);
        return this;
    }

    public void setMotatenofics(Set<Motatenofic> motatenofics) {
        this.motatenofics = motatenofics;
    }

    public Set<Pasegl> getPasegls() {
        return pasegls;
    }

    public Oficina pasegls(Set<Pasegl> pasegls) {
        this.pasegls = pasegls;
        return this;
    }

    public Oficina addPasegl(Pasegl pasegl) {
        this.pasegls.add(pasegl);
        pasegl.setOficina(this);
        return this;
    }

    public Oficina removePasegl(Pasegl pasegl) {
        this.pasegls.remove(pasegl);
        pasegl.setOficina(null);
        return this;
    }

    public void setPasegls(Set<Pasegl> pasegls) {
        this.pasegls = pasegls;
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
        Oficina oficina = (Oficina) o;
        if (oficina.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), oficina.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Oficina{" +
            "id=" + getId() +
            ", vDesofic='" + getvDesofic() + "'" +
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
