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
 * LISTA MAESTRA DE PERSONAS NATURALES
 */
@ApiModel(description = "LISTA MAESTRA DE PERSONAS NATURALES")
@Entity
@Table(name = "pernatural")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pernatural")
public class Pernatural implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * NOMBRES DE LA PERSONA NATURAL.
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "NOMBRES DE LA PERSONA NATURAL.", required = true)
    @Column(name = "v_nombres", length = 100, nullable = false)
    private String vNombres;

    /**
     * APELLIDO PATERNO
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "APELLIDO PATERNO", required = true)
    @Column(name = "v_apepat", length = 100, nullable = false)
    private String vApepat;

    /**
     * APELLIDO MATERNO
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "APELLIDO MATERNO", required = true)
    @Column(name = "v_apemat", length = 100, nullable = false)
    private String vApemat;

    /**
     * NUMERO DE DOCUMENTO DE IDENTIDAD.
     */
    @NotNull
    @Size(max = 15)
    @ApiModelProperty(value = "NUMERO DE DOCUMENTO DE IDENTIDAD.", required = true)
    @Column(name = "v_numdoc", length = 15, nullable = false)
    private String vNumdoc;

    /**
     * NUMERO DE TELEFONO
     */
    @Size(max = 15)
    @ApiModelProperty(value = "NUMERO DE TELEFONO")
    @Column(name = "v_telefono", length = 15)
    private String vTelefono;

    /**
     * NUMERO DE TELEFONO CELULAR.
     */
    @Size(max = 15)
    @ApiModelProperty(value = "NUMERO DE TELEFONO CELULAR.")
    @Column(name = "v_celular", length = 15)
    private String vCelular;

    /**
     * CORREO ELECTRONICO
     */
    @Size(max = 100)
    @ApiModelProperty(value = "CORREO ELECTRONICO")
    @Column(name = "v_emailper", length = 100)
    private String vEmailper;

    /**
     * FECHA DE NACIMIENTO
     */
    @ApiModelProperty(value = "FECHA DE NACIMIENTO")
    @Column(name = "d_fecnac")
    private LocalDate dFecnac;

    /**
     * SEXO
     */
    @NotNull
    @Size(max = 1)
    @ApiModelProperty(value = "SEXO", required = true)
    @Column(name = "v_sexoper", length = 1, nullable = false)
    private String vSexoper;

    /**
     * ESTADO DE LA PERSONA NATURAL.
     */
    @NotNull
    @Size(max = 1)
    @ApiModelProperty(value = "ESTADO DE LA PERSONA NATURAL.", required = true)
    @Column(name = "v_estado", length = 1, nullable = false)
    private String vEstado;

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

    @OneToMany(mappedBy = "pernatural")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dirpernat> dirpernats = new HashSet<>();

    @OneToMany(mappedBy = "pernatural")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Empleador> empleadors = new HashSet<>();

    @OneToMany(mappedBy = "pernatural")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Trabajador> trabajadors = new HashSet<>();

    @OneToMany(mappedBy = "pernatural")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sucesor> sucesors = new HashSet<>();

    @ManyToOne
    private Tipdocident tipdocident;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvNombres() {
        return vNombres;
    }

    public Pernatural vNombres(String vNombres) {
        this.vNombres = vNombres;
        return this;
    }

    public void setvNombres(String vNombres) {
        this.vNombres = vNombres;
    }

    public String getvApepat() {
        return vApepat;
    }

    public Pernatural vApepat(String vApepat) {
        this.vApepat = vApepat;
        return this;
    }

    public void setvApepat(String vApepat) {
        this.vApepat = vApepat;
    }

    public String getvApemat() {
        return vApemat;
    }

    public Pernatural vApemat(String vApemat) {
        this.vApemat = vApemat;
        return this;
    }

    public void setvApemat(String vApemat) {
        this.vApemat = vApemat;
    }

    public String getvNumdoc() {
        return vNumdoc;
    }

    public Pernatural vNumdoc(String vNumdoc) {
        this.vNumdoc = vNumdoc;
        return this;
    }

    public void setvNumdoc(String vNumdoc) {
        this.vNumdoc = vNumdoc;
    }

    public String getvTelefono() {
        return vTelefono;
    }

    public Pernatural vTelefono(String vTelefono) {
        this.vTelefono = vTelefono;
        return this;
    }

    public void setvTelefono(String vTelefono) {
        this.vTelefono = vTelefono;
    }

    public String getvCelular() {
        return vCelular;
    }

    public Pernatural vCelular(String vCelular) {
        this.vCelular = vCelular;
        return this;
    }

    public void setvCelular(String vCelular) {
        this.vCelular = vCelular;
    }

    public String getvEmailper() {
        return vEmailper;
    }

    public Pernatural vEmailper(String vEmailper) {
        this.vEmailper = vEmailper;
        return this;
    }

    public void setvEmailper(String vEmailper) {
        this.vEmailper = vEmailper;
    }

    public LocalDate getdFecnac() {
        return dFecnac;
    }

    public Pernatural dFecnac(LocalDate dFecnac) {
        this.dFecnac = dFecnac;
        return this;
    }

    public void setdFecnac(LocalDate dFecnac) {
        this.dFecnac = dFecnac;
    }

    public String getvSexoper() {
        return vSexoper;
    }

    public Pernatural vSexoper(String vSexoper) {
        this.vSexoper = vSexoper;
        return this;
    }

    public void setvSexoper(String vSexoper) {
        this.vSexoper = vSexoper;
    }

    public String getvEstado() {
        return vEstado;
    }

    public Pernatural vEstado(String vEstado) {
        this.vEstado = vEstado;
        return this;
    }

    public void setvEstado(String vEstado) {
        this.vEstado = vEstado;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Pernatural nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Pernatural tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Pernatural nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Pernatural nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Pernatural nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Pernatural tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Pernatural nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Dirpernat> getDirpernats() {
        return dirpernats;
    }

    public Pernatural dirpernats(Set<Dirpernat> dirpernats) {
        this.dirpernats = dirpernats;
        return this;
    }

    public Pernatural addDirpernat(Dirpernat dirpernat) {
        this.dirpernats.add(dirpernat);
        dirpernat.setPernatural(this);
        return this;
    }

    public Pernatural removeDirpernat(Dirpernat dirpernat) {
        this.dirpernats.remove(dirpernat);
        dirpernat.setPernatural(null);
        return this;
    }

    public void setDirpernats(Set<Dirpernat> dirpernats) {
        this.dirpernats = dirpernats;
    }

    public Set<Empleador> getEmpleadors() {
        return empleadors;
    }

    public Pernatural empleadors(Set<Empleador> empleadors) {
        this.empleadors = empleadors;
        return this;
    }

    public Pernatural addEmpleador(Empleador empleador) {
        this.empleadors.add(empleador);
        empleador.setPernatural(this);
        return this;
    }

    public Pernatural removeEmpleador(Empleador empleador) {
        this.empleadors.remove(empleador);
        empleador.setPernatural(null);
        return this;
    }

    public void setEmpleadors(Set<Empleador> empleadors) {
        this.empleadors = empleadors;
    }

    public Set<Trabajador> getTrabajadors() {
        return trabajadors;
    }

    public Pernatural trabajadors(Set<Trabajador> trabajadors) {
        this.trabajadors = trabajadors;
        return this;
    }

    public Pernatural addTrabajador(Trabajador trabajador) {
        this.trabajadors.add(trabajador);
        trabajador.setPernatural(this);
        return this;
    }

    public Pernatural removeTrabajador(Trabajador trabajador) {
        this.trabajadors.remove(trabajador);
        trabajador.setPernatural(null);
        return this;
    }

    public void setTrabajadors(Set<Trabajador> trabajadors) {
        this.trabajadors = trabajadors;
    }

    public Set<Sucesor> getSucesors() {
        return sucesors;
    }

    public Pernatural sucesors(Set<Sucesor> sucesors) {
        this.sucesors = sucesors;
        return this;
    }

    public Pernatural addSucesor(Sucesor sucesor) {
        this.sucesors.add(sucesor);
        sucesor.setPernatural(this);
        return this;
    }

    public Pernatural removeSucesor(Sucesor sucesor) {
        this.sucesors.remove(sucesor);
        sucesor.setPernatural(null);
        return this;
    }

    public void setSucesors(Set<Sucesor> sucesors) {
        this.sucesors = sucesors;
    }

    public Tipdocident getTipdocident() {
        return tipdocident;
    }

    public Pernatural tipdocident(Tipdocident tipdocident) {
        this.tipdocident = tipdocident;
        return this;
    }

    public void setTipdocident(Tipdocident tipdocident) {
        this.tipdocident = tipdocident;
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
        Pernatural pernatural = (Pernatural) o;
        if (pernatural.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pernatural.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pernatural{" +
            "id=" + getId() +
            ", vNombres='" + getvNombres() + "'" +
            ", vApepat='" + getvApepat() + "'" +
            ", vApemat='" + getvApemat() + "'" +
            ", vNumdoc='" + getvNumdoc() + "'" +
            ", vTelefono='" + getvTelefono() + "'" +
            ", vCelular='" + getvCelular() + "'" +
            ", vEmailper='" + getvEmailper() + "'" +
            ", dFecnac='" + getdFecnac() + "'" +
            ", vSexoper='" + getvSexoper() + "'" +
            ", vEstado='" + getvEstado() + "'" +
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
