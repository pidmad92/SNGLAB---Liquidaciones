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
 * LISTA DE PASES GENERADOS POR LAS ATENCIONES
 */
@ApiModel(description = "LISTA DE PASES GENERADOS POR LAS ATENCIONES")
@Entity
@Table(name = "gltbc_pasegl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gltbc_pasegl")
public class Pasegl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codpase", nullable = false)
    private Long id;

    /**
     * OBSERVACION DEL PASE
     */
    @Size(max = 500)
    @ApiModelProperty(value = "OBSERVACION DEL PASE")
    @Column(name = "v_obspase", length = 500)
    private String vObspase;

    /**
     * ESTADO DEL PASE.
     */
    @NotNull
    @Size(max = 1)
    @ApiModelProperty(value = "ESTADO DEL PASE.", required = true)
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
    @JoinColumn(name = "n_codofic")
    private Oficina oficina;

    @OneToOne
    //@JsonIgnore
    @JoinColumn(unique = true, name = "n_codaten")
    private Atencion atencion;

    @OneToMany(mappedBy = "pasegl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Motivpase> motivpases = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvObspase() {
        return vObspase;
    }

    public Pasegl vObspase(String vObspase) {
        this.vObspase = vObspase;
        return this;
    }

    public void setvObspase(String vObspase) {
        this.vObspase = vObspase;
    }

    public String getvEstado() {
        return vEstado;
    }

    public Pasegl vEstado(String vEstado) {
        this.vEstado = vEstado;
        return this;
    }

    public void setvEstado(String vEstado) {
        this.vEstado = vEstado;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Pasegl nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Pasegl tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Pasegl nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Pasegl nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Pasegl nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Pasegl tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Pasegl nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Pasegl oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Atencion getAtencion() {
        return atencion;
    }

    public Pasegl atencion(Atencion atencion) {
        this.atencion = atencion;
        return this;
    }

    public void setAtencion(Atencion atencion) {
        this.atencion = atencion;
    }

    public Set<Motivpase> getMotivpases() {
        return motivpases;
    }

    public Pasegl motivpases(Set<Motivpase> motivpases) {
        this.motivpases = motivpases;
        return this;
    }

    public Pasegl addMotivpase(Motivpase motivpase) {
        this.motivpases.add(motivpase);
        motivpase.setPasegl(this);
        return this;
    }

    public Pasegl removeMotivpase(Motivpase motivpase) {
        this.motivpases.remove(motivpase);
        motivpase.setPasegl(null);
        return this;
    }

    public void setMotivpases(Set<Motivpase> motivpases) {
        this.motivpases = motivpases;
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
        Pasegl pasegl = (Pasegl) o;
        if (pasegl.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pasegl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pasegl{" +
            "id=" + getId() +
            ", vObspase='" + getvObspase() + "'" +
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
