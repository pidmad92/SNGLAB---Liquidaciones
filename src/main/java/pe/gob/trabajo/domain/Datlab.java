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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * LISTA DE LOS DATOS LABORALES QUE RELACIONA A UN EMPLEADOR CON UN TRABAJADOR
 */
@ApiModel(description = "LISTA DE LOS DATOS LABORALES QUE RELACIONA A UN EMPLEADOR CON UN TRABAJADOR")
@Entity
@Table(name = "gltbc_datlab")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_datlab")
public class Datlab implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_coddatlab", nullable = false)
    private Long id;

    /**
     * BANDERA QUE INDICA SI EL TRABAJADOR MANTIENE SU VINCULO LABORAL CON EL EMPLEADOR (MANTIENE VINCULO = 1, NO MANTIENE VINCULO = 0)
     */
    @NotNull
    @ApiModelProperty(value = "BANDERA QUE INDICA SI EL TRABAJADOR MANTIENE SU VINCULO LABORAL CON EL EMPLEADOR (MANTIENE VINCULO = 1, NO MANTIENE VINCULO = 0)", required = true)
    @Column(name = "n_flgsitlab", nullable = false)
    private Boolean nFlgsitlab;

    /**
     * FECHA DE INICIO DEL VINCULO O FECHA DE INICIO DEL CONTRATO.
     */
    @ApiModelProperty(value = "FECHA DE INICIO DEL VINCULO O FECHA DE INICIO DEL CONTRATO.")
    @Column(name = "d_fecvincul")
    private LocalDate dFecvincul;

    /**
     * FECHA DE CESE.
     */
    @ApiModelProperty(value = "FECHA DE CESE.")
    @Column(name = "d_feccese")
    private LocalDate dFeccese;

    /**
     * FECHA DE FIN DE CONTRATO.
     */
    @ApiModelProperty(value = "FECHA DE FIN DE CONTRATO.")
    @Column(name = "d_fecfincon")
    private LocalDate dFecfincon;

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

    @OneToMany(mappedBy = "datlab")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Docinperdlb> docinperdlbs = new HashSet<>();

    @OneToMany(mappedBy = "datlab")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Atencion> atencions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "n_codemplea")
    private Empleador empleador;

    @ManyToOne
    @JoinColumn(name = "n_codmdcont")
    private Modcontrato modcontrato;

    @ManyToOne
    @JoinColumn(name = "n_codmotces")
    private Motcese motcese;

    @ManyToOne
    @JoinColumn(name = "n_codreglab")
    private Regimenlab regimenlab;

    @ManyToOne
    @JoinColumn(name = "n_codtrab")
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "n_codtipvin")
    private Tipvinculo tipvinculo;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isnFlgsitlab() {
        return nFlgsitlab;
    }

    public Datlab nFlgsitlab(Boolean nFlgsitlab) {
        this.nFlgsitlab = nFlgsitlab;
        return this;
    }

    public void setnFlgsitlab(Boolean nFlgsitlab) {
        this.nFlgsitlab = nFlgsitlab;
    }

    public LocalDate getdFecvincul() {
        return dFecvincul;
    }

    public Datlab dFecvincul(LocalDate dFecvincul) {
        this.dFecvincul = dFecvincul;
        return this;
    }

    public void setdFecvincul(LocalDate dFecvincul) {
        this.dFecvincul = dFecvincul;
    }

    public LocalDate getdFeccese() {
        return dFeccese;
    }

    public Datlab dFeccese(LocalDate dFeccese) {
        this.dFeccese = dFeccese;
        return this;
    }

    public void setdFeccese(LocalDate dFeccese) {
        this.dFeccese = dFeccese;
    }

    public LocalDate getdFecfincon() {
        return dFecfincon;
    }

    public Datlab dFecfincon(LocalDate dFecfincon) {
        this.dFecfincon = dFecfincon;
        return this;
    }

    public void setdFecfincon(LocalDate dFecfincon) {
        this.dFecfincon = dFecfincon;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Datlab nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Datlab tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Datlab nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Datlab nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Datlab nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Datlab tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Datlab nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Docinperdlb> getDocinperdlbs() {
        return docinperdlbs;
    }

    public Datlab docinperdlbs(Set<Docinperdlb> docinperdlbs) {
        this.docinperdlbs = docinperdlbs;
        return this;
    }

    public Datlab addDocinperdlb(Docinperdlb docinperdlb) {
        this.docinperdlbs.add(docinperdlb);
        docinperdlb.setDatlab(this);
        return this;
    }

    public Datlab removeDocinperdlb(Docinperdlb docinperdlb) {
        this.docinperdlbs.remove(docinperdlb);
        docinperdlb.setDatlab(null);
        return this;
    }

    public void setDocinperdlbs(Set<Docinperdlb> docinperdlbs) {
        this.docinperdlbs = docinperdlbs;
    }

    public Set<Atencion> getAtencions() {
        return atencions;
    }

    public Datlab atencions(Set<Atencion> atencions) {
        this.atencions = atencions;
        return this;
    }

    public Datlab addAtencion(Atencion atencion) {
        this.atencions.add(atencion);
        atencion.setDatlab(this);
        return this;
    }

    public Datlab removeAtencion(Atencion atencion) {
        this.atencions.remove(atencion);
        atencion.setDatlab(null);
        return this;
    }

    public void setAtencions(Set<Atencion> atencions) {
        this.atencions = atencions;
    }

    public Empleador getEmpleador() {
        return empleador;
    }

    public Datlab empleador(Empleador empleador) {
        this.empleador = empleador;
        return this;
    }

    public void setEmpleador(Empleador empleador) {
        this.empleador = empleador;
    }

    public Modcontrato getModcontrato() {
        return modcontrato;
    }

    public Datlab modcontrato(Modcontrato modcontrato) {
        this.modcontrato = modcontrato;
        return this;
    }

    public void setModcontrato(Modcontrato modcontrato) {
        this.modcontrato = modcontrato;
    }

    public Motcese getMotcese() {
        return motcese;
    }

    public Datlab motcese(Motcese motcese) {
        this.motcese = motcese;
        return this;
    }

    public void setMotcese(Motcese motcese) {
        this.motcese = motcese;
    }

    public Regimenlab getRegimenlab() {
        return regimenlab;
    }

    public Datlab regimenlab(Regimenlab regimenlab) {
        this.regimenlab = regimenlab;
        return this;
    }

    public void setRegimenlab(Regimenlab regimenlab) {
        this.regimenlab = regimenlab;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public Datlab trabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
        return this;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Tipvinculo getTipvinculo() {
        return tipvinculo;
    }

    public Datlab tipvinculo(Tipvinculo tipvinculo) {
        this.tipvinculo = tipvinculo;
        return this;
    }

    public void setTipvinculo(Tipvinculo tipvinculo) {
        this.tipvinculo = tipvinculo;
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
        Datlab datlab = (Datlab) o;
        if (datlab.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), datlab.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Datlab{" +
            "id=" + getId() +
            ", nFlgsitlab='" + isnFlgsitlab() + "'" +
            ", dFecvincul='" + getdFecvincul() + "'" +
            ", dFeccese='" + getdFeccese() + "'" +
            ", dFecfincon='" + getdFecfincon() + "'" +
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
