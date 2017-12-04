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
 * LISTA DE TRABAJADORES
 */
@ApiModel(description = "LISTA DE TRABAJADORES")
@Entity
@Table(name = "gltbc_trabajador")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_trabajador")
public class Trabajador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codtrab", nullable = false)
    private Long id;

    /**
     * BANDERA QUE INDICA SI TIENE UN SUCESOR (CON SUCESOR=1, SIN SUCESOR=0)
     */
    @NotNull
    @ApiModelProperty(value = "BANDERA QUE INDICA SI TIENE UN SUCESOR (CON SUCESOR=1, SIN SUCESOR=0)", required = true)
    @Column(name = "n_flgsuces", nullable = false)
    private Boolean nFlgsuces;

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

    @ManyToOne
    @JoinColumn(name = "n_codcartra")
    private Cartrab cartrab;

    @ManyToOne
    @JoinColumn(name = "n_codpernat")
    private Pernatural pernatural;

    @OneToMany(mappedBy = "trabajador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Datlab> datlabs = new HashSet<>();

    @OneToMany(mappedBy = "trabajador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Atencion> atencions = new HashSet<>();

    @OneToMany(mappedBy = "trabajador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sucesor> sucesors = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isnFlgsuces() {
        return nFlgsuces;
    }

    public Trabajador nFlgsuces(Boolean nFlgsuces) {
        this.nFlgsuces = nFlgsuces;
        return this;
    }

    public void setnFlgsuces(Boolean nFlgsuces) {
        this.nFlgsuces = nFlgsuces;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Trabajador nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Trabajador tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Trabajador nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Trabajador nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Trabajador nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Trabajador tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Trabajador nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Cartrab getCartrab() {
        return cartrab;
    }

    public Trabajador cartrab(Cartrab cartrab) {
        this.cartrab = cartrab;
        return this;
    }

    public void setCartrab(Cartrab cartrab) {
        this.cartrab = cartrab;
    }

    public Pernatural getPernatural() {
        return pernatural;
    }

    public Trabajador pernatural(Pernatural pernatural) {
        this.pernatural = pernatural;
        return this;
    }

    public void setPernatural(Pernatural pernatural) {
        this.pernatural = pernatural;
    }

    public Set<Datlab> getDatlabs() {
        return datlabs;
    }

    public Trabajador datlabs(Set<Datlab> datlabs) {
        this.datlabs = datlabs;
        return this;
    }

    public Trabajador addDatlab(Datlab datlab) {
        this.datlabs.add(datlab);
        datlab.setTrabajador(this);
        return this;
    }

    public Trabajador removeDatlab(Datlab datlab) {
        this.datlabs.remove(datlab);
        datlab.setTrabajador(null);
        return this;
    }

    public void setDatlabs(Set<Datlab> datlabs) {
        this.datlabs = datlabs;
    }

    public Set<Atencion> getAtencions() {
        return atencions;
    }

    public Trabajador atencions(Set<Atencion> atencions) {
        this.atencions = atencions;
        return this;
    }

    public Trabajador addAtencion(Atencion atencion) {
        this.atencions.add(atencion);
        atencion.setTrabajador(this);
        return this;
    }

    public Trabajador removeAtencion(Atencion atencion) {
        this.atencions.remove(atencion);
        atencion.setTrabajador(null);
        return this;
    }

    public void setAtencions(Set<Atencion> atencions) {
        this.atencions = atencions;
    }

    public Set<Sucesor> getSucesors() {
        return sucesors;
    }

    public Trabajador sucesors(Set<Sucesor> sucesors) {
        this.sucesors = sucesors;
        return this;
    }

    public Trabajador addSucesor(Sucesor sucesor) {
        this.sucesors.add(sucesor);
        sucesor.setTrabajador(this);
        return this;
    }

    public Trabajador removeSucesor(Sucesor sucesor) {
        this.sucesors.remove(sucesor);
        sucesor.setTrabajador(null);
        return this;
    }

    public void setSucesors(Set<Sucesor> sucesors) {
        this.sucesors = sucesors;
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
        Trabajador trabajador = (Trabajador) o;
        if (trabajador.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trabajador.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trabajador{" +
            "id=" + getId() +
            ", nFlgsuces='" + isnFlgsuces() + "'" +
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
