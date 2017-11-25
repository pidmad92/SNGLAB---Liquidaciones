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
 * LISTA DE LOS MOTIVOS DE ATENCION POR CADA OFICINA
 */
@ApiModel(description = "LISTA DE LOS MOTIVOS DE ATENCION POR CADA OFICINA")
@Entity
@Table(name = "motatenofic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "motatenofic")
public class Motatenofic implements Serializable {

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

    @ManyToOne
    private Motate motate;

    @ManyToOne
    private Oficina oficina;

    @OneToMany(mappedBy = "motatenofic")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Motateselec> motateselecs = new HashSet<>();

    @OneToMany(mappedBy = "motatenofic")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Motivpase> motivPases = new HashSet<>();

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

    public Motatenofic nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Motatenofic tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Motatenofic nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Motatenofic nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Motatenofic nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Motatenofic tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Motatenofic nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Motate getMotate() {
        return motate;
    }

    public Motatenofic motate(Motate motate) {
        this.motate = motate;
        return this;
    }

    public void setMotate(Motate motate) {
        this.motate = motate;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Motatenofic oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Set<Motateselec> getMotateselecs() {
        return motateselecs;
    }

    public Motatenofic motateselecs(Set<Motateselec> motateselecs) {
        this.motateselecs = motateselecs;
        return this;
    }

    public Motatenofic addMotateselec(Motateselec motateselec) {
        this.motateselecs.add(motateselec);
        motateselec.setMotatenofic(this);
        return this;
    }

    public Motatenofic removeMotateselec(Motateselec motateselec) {
        this.motateselecs.remove(motateselec);
        motateselec.setMotatenofic(null);
        return this;
    }

    public void setMotateselecs(Set<Motateselec> motateselecs) {
        this.motateselecs = motateselecs;
    }

    public Set<Motivpase> getMotivPases() {
        return motivPases;
    }

    public Motatenofic motivPases(Set<Motivpase> motivpases) {
        this.motivPases = motivpases;
        return this;
    }

    public Motatenofic addMotivPase(Motivpase motivpase) {
        this.motivPases.add(motivpase);
        motivpase.setMotatenofic(this);
        return this;
    }

    public Motatenofic removeMotivPase(Motivpase motivpase) {
        this.motivPases.remove(motivpase);
        motivpase.setMotatenofic(null);
        return this;
    }

    public void setMotivPases(Set<Motivpase> motivpases) {
        this.motivPases = motivpases;
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
        Motatenofic motatenofic = (Motatenofic) o;
        if (motatenofic.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), motatenofic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Motatenofic{" +
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
