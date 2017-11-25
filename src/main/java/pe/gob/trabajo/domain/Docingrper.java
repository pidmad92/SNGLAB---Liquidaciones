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
 * LISTA MAESTRA DE DOCUMENTOS DE INGRESOS PERCIBIDOS PARA REGISTRO DE DATOS LABORALES
 */
@ApiModel(description = "LISTA MAESTRA DE DOCUMENTOS DE INGRESOS PERCIBIDOS PARA REGISTRO DE DATOS LABORALES")
@Entity
@Table(name = "docingrper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "docingrper")
public class Docingrper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * DESCRIPCION DEL DOCUMENTO DE INGRESO PERCIBIDO.
     */
    @NotNull
    @Size(max = 200)
    @ApiModelProperty(value = "DESCRIPCION DEL DOCUMENTO DE INGRESO PERCIBIDO.", required = true)
    @Column(name = "v_desdocing", length = 200, nullable = false)
    private String vDesdocing;

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

    @OneToMany(mappedBy = "docingrper")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Docinperdlb> docinperdlbs = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvDesdocing() {
        return vDesdocing;
    }

    public Docingrper vDesdocing(String vDesdocing) {
        this.vDesdocing = vDesdocing;
        return this;
    }

    public void setvDesdocing(String vDesdocing) {
        this.vDesdocing = vDesdocing;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Docingrper nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Docingrper tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Docingrper nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Docingrper nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Docingrper nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Docingrper tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Docingrper nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Docinperdlb> getDocinperdlbs() {
        return docinperdlbs;
    }

    public Docingrper docinperdlbs(Set<Docinperdlb> docinperdlbs) {
        this.docinperdlbs = docinperdlbs;
        return this;
    }

    public Docingrper addDocinperdlb(Docinperdlb docinperdlb) {
        this.docinperdlbs.add(docinperdlb);
        docinperdlb.setDocingrper(this);
        return this;
    }

    public Docingrper removeDocinperdlb(Docinperdlb docinperdlb) {
        this.docinperdlbs.remove(docinperdlb);
        docinperdlb.setDocingrper(null);
        return this;
    }

    public void setDocinperdlbs(Set<Docinperdlb> docinperdlbs) {
        this.docinperdlbs = docinperdlbs;
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
        Docingrper docingrper = (Docingrper) o;
        if (docingrper.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), docingrper.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Docingrper{" +
            "id=" + getId() +
            ", vDesdocing='" + getvDesdocing() + "'" +
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
