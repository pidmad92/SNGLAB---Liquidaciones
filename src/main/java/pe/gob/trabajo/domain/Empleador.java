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
 * LISTA DE EMPLEADORES
 */
@ApiModel(description = "LISTA DE EMPLEADORES")
@Entity
@Table(name = "empleador")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "empleador")
public class Empleador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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

    @OneToMany(mappedBy = "empleador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Datlab> datlabs = new HashSet<>();

    @OneToMany(mappedBy = "empleador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Atencion> atencions = new HashSet<>();

    @ManyToOne
    private Perjuridica perjuridica;

    @ManyToOne
    private Pernatural pernatural;

    @ManyToOne
    private Tippersona tippersona;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Empleador nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Empleador tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Empleador nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Empleador nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Empleador nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Empleador tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Empleador nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Datlab> getDatlabs() {
        return datlabs;
    }

    public Empleador datlabs(Set<Datlab> datlabs) {
        this.datlabs = datlabs;
        return this;
    }

    public Empleador addDatlab(Datlab datlab) {
        this.datlabs.add(datlab);
        datlab.setEmpleador(this);
        return this;
    }

    public Empleador removeDatlab(Datlab datlab) {
        this.datlabs.remove(datlab);
        datlab.setEmpleador(null);
        return this;
    }

    public void setDatlabs(Set<Datlab> datlabs) {
        this.datlabs = datlabs;
    }

    public Set<Atencion> getAtencions() {
        return atencions;
    }

    public Empleador atencions(Set<Atencion> atencions) {
        this.atencions = atencions;
        return this;
    }

    public Empleador addAtencion(Atencion atencion) {
        this.atencions.add(atencion);
        atencion.setEmpleador(this);
        return this;
    }

    public Empleador removeAtencion(Atencion atencion) {
        this.atencions.remove(atencion);
        atencion.setEmpleador(null);
        return this;
    }

    public void setAtencions(Set<Atencion> atencions) {
        this.atencions = atencions;
    }

    public Perjuridica getPerjuridica() {
        return perjuridica;
    }

    public Empleador perjuridica(Perjuridica perjuridica) {
        this.perjuridica = perjuridica;
        return this;
    }

    public void setPerjuridica(Perjuridica perjuridica) {
        this.perjuridica = perjuridica;
    }

    public Pernatural getPernatural() {
        return pernatural;
    }

    public Empleador pernatural(Pernatural pernatural) {
        this.pernatural = pernatural;
        return this;
    }

    public void setPernatural(Pernatural pernatural) {
        this.pernatural = pernatural;
    }

    public Tippersona getTippersona() {
        return tippersona;
    }

    public Empleador tippersona(Tippersona tippersona) {
        this.tippersona = tippersona;
        return this;
    }

    public void setTippersona(Tippersona tippersona) {
        this.tippersona = tippersona;
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
        Empleador empleador = (Empleador) o;
        if (empleador.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), empleador.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Empleador{" +
            "id=" + getId() +
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
