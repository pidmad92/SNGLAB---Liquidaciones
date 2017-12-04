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
 * LISTA MAESTRA DE PERSONAS JURIDICAS
 */
@ApiModel(description = "LISTA MAESTRA DE PERSONAS JURIDICAS")
@Entity
@Table(name = "gltbc_perjuridica")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_perjuridica")
public class Perjuridica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codperjur", nullable = false)
    private Long id;

    /**
     * RAZON SOCIAL DE LA PERSONA JURIDICA.
     */
    @NotNull
    @Size(max = 200)
    @ApiModelProperty(value = "RAZON SOCIAL DE LA PERSONA JURIDICA.", required = true)
    @Column(name = "v_razsocial", length = 200, nullable = false)
    private String vRazsocial;

    /**
     * NOMBRE ALTERNATIVO DE LA PERSONA JURIDICA.
     */
    @Size(max = 200)
    @ApiModelProperty(value = "NOMBRE ALTERNATIVO DE LA PERSONA JURIDICA.")
    @Column(name = "v_nomalter", length = 200)
    private String vNomalter;

    /**
     * NUMERO DE DOCUMENTO DE IDENTIDAD DE LA PERSONA JURIDICA.
     */
    @NotNull
    @Size(max = 15)
    @ApiModelProperty(value = "NUMERO DE DOCUMENTO DE IDENTIDAD DE LA PERSONA JURIDICA.", required = true)
    @Column(name = "v_numdoc", length = 15, nullable = false)
    private String vNumdoc;

    /**
     * CORREO ELECTRONICO DE LA PERSONA JURIDICA.
     */
    @Size(max = 100)
    @ApiModelProperty(value = "CORREO ELECTRONICO DE LA PERSONA JURIDICA.")
    @Column(name = "v_emailper", length = 100)
    private String vEmailper;

    /**
     * TELEFONO DE REFERENCIA DE LA PERSONA JURIDICA
     */
    @Size(max = 15)
    @ApiModelProperty(value = "TELEFONO DE REFERENCIA DE LA PERSONA JURIDICA")
    @Column(name = "v_telefono", length = 15)
    private String vTelefono;

    /**
     * NUMERO DE FAX DE  LA PERSONA JURIDICA.
     */
    @Size(max = 15)
    @ApiModelProperty(value = "NUMERO DE FAX DE  LA PERSONA JURIDICA.")
    @Column(name = "v_faxperju", length = 15)
    private String vFaxperju;

    /**
     * ESTADO DE LA PERSONA JURIDICA.
     */
    @NotNull
    @Size(max = 1)
    @ApiModelProperty(value = "ESTADO DE LA PERSONA JURIDICA.", required = true)
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

    @ManyToOne
    @JoinColumn(name = "n_codacteco")
    private Actiecon actiecon;

    @OneToMany(mappedBy = "perjuridica")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dirperjuri> dirperjuris = new HashSet<>();

    @OneToMany(mappedBy = "perjuridica")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Empleador> empleadors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "n_codtdiden")
    private Tipdocident tipdocident;

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

    public Perjuridica vRazsocial(String vRazsocial) {
        this.vRazsocial = vRazsocial;
        return this;
    }

    public void setvRazsocial(String vRazsocial) {
        this.vRazsocial = vRazsocial;
    }

    public String getvNomalter() {
        return vNomalter;
    }

    public Perjuridica vNomalter(String vNomalter) {
        this.vNomalter = vNomalter;
        return this;
    }

    public void setvNomalter(String vNomalter) {
        this.vNomalter = vNomalter;
    }

    public String getvNumdoc() {
        return vNumdoc;
    }

    public Perjuridica vNumdoc(String vNumdoc) {
        this.vNumdoc = vNumdoc;
        return this;
    }

    public void setvNumdoc(String vNumdoc) {
        this.vNumdoc = vNumdoc;
    }

    public String getvEmailper() {
        return vEmailper;
    }

    public Perjuridica vEmailper(String vEmailper) {
        this.vEmailper = vEmailper;
        return this;
    }

    public void setvEmailper(String vEmailper) {
        this.vEmailper = vEmailper;
    }

    public String getvTelefono() {
        return vTelefono;
    }

    public Perjuridica vTelefono(String vTelefono) {
        this.vTelefono = vTelefono;
        return this;
    }

    public void setvTelefono(String vTelefono) {
        this.vTelefono = vTelefono;
    }

    public String getvFaxperju() {
        return vFaxperju;
    }

    public Perjuridica vFaxperju(String vFaxperju) {
        this.vFaxperju = vFaxperju;
        return this;
    }

    public void setvFaxperju(String vFaxperju) {
        this.vFaxperju = vFaxperju;
    }

    public String getvEstado() {
        return vEstado;
    }

    public Perjuridica vEstado(String vEstado) {
        this.vEstado = vEstado;
        return this;
    }

    public void setvEstado(String vEstado) {
        this.vEstado = vEstado;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Perjuridica nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Perjuridica tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Perjuridica nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Perjuridica nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Perjuridica nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Perjuridica tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Perjuridica nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Actiecon getActiecon() {
        return actiecon;
    }

    public Perjuridica actiecon(Actiecon actiecon) {
        this.actiecon = actiecon;
        return this;
    }

    public void setActiecon(Actiecon actiecon) {
        this.actiecon = actiecon;
    }

    public Set<Dirperjuri> getDirperjuris() {
        return dirperjuris;
    }

    public Perjuridica dirperjuris(Set<Dirperjuri> dirperjuris) {
        this.dirperjuris = dirperjuris;
        return this;
    }

    public Perjuridica addDirperjuri(Dirperjuri dirperjuri) {
        this.dirperjuris.add(dirperjuri);
        dirperjuri.setPerjuridica(this);
        return this;
    }

    public Perjuridica removeDirperjuri(Dirperjuri dirperjuri) {
        this.dirperjuris.remove(dirperjuri);
        dirperjuri.setPerjuridica(null);
        return this;
    }

    public void setDirperjuris(Set<Dirperjuri> dirperjuris) {
        this.dirperjuris = dirperjuris;
    }

    public Set<Empleador> getEmpleadors() {
        return empleadors;
    }

    public Perjuridica empleadors(Set<Empleador> empleadors) {
        this.empleadors = empleadors;
        return this;
    }

    public Perjuridica addEmpleador(Empleador empleador) {
        this.empleadors.add(empleador);
        empleador.setPerjuridica(this);
        return this;
    }

    public Perjuridica removeEmpleador(Empleador empleador) {
        this.empleadors.remove(empleador);
        empleador.setPerjuridica(null);
        return this;
    }

    public void setEmpleadors(Set<Empleador> empleadors) {
        this.empleadors = empleadors;
    }

    public Tipdocident getTipdocident() {
        return tipdocident;
    }

    public Perjuridica tipdocident(Tipdocident tipdocident) {
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
        Perjuridica perjuridica = (Perjuridica) o;
        if (perjuridica.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), perjuridica.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Perjuridica{" +
            "id=" + getId() +
            ", vRazsocial='" + getvRazsocial() + "'" +
            ", vNomalter='" + getvNomalter() + "'" +
            ", vNumdoc='" + getvNumdoc() + "'" +
            ", vEmailper='" + getvEmailper() + "'" +
            ", vTelefono='" + getvTelefono() + "'" +
            ", vFaxperju='" + getvFaxperju() + "'" +
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
