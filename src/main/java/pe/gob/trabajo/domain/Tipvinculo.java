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
 * LISTA MAESTRA DE LOS TIPOS DE VINCULO LABORAL PARA  LOS DATOS LABORALES
 */
@ApiModel(description = "LISTA MAESTRA DE LOS TIPOS DE VINCULO LABORAL PARA  LOS DATOS LABORALES")
@Entity
@Table(name = "tipvinculo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tipvinculo")
public class Tipvinculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * DESCRIPCION DEL TIPO DE VINCULO LABORAL.
     */
    @NotNull
    @Size(max = 100)
    @ApiModelProperty(value = "DESCRIPCION DEL TIPO DE VINCULO LABORAL.", required = true)
    @Column(name = "v_destipvin", length = 100, nullable = false)
    private String vDestipvin;

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

    @OneToMany(mappedBy = "tipvinculo")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Datlab> datlabs = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvDestipvin() {
        return vDestipvin;
    }

    public Tipvinculo vDestipvin(String vDestipvin) {
        this.vDestipvin = vDestipvin;
        return this;
    }

    public void setvDestipvin(String vDestipvin) {
        this.vDestipvin = vDestipvin;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Tipvinculo nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Tipvinculo tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Tipvinculo nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Tipvinculo nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Tipvinculo nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Tipvinculo tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Tipvinculo nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Set<Datlab> getDatlabs() {
        return datlabs;
    }

    public Tipvinculo datlabs(Set<Datlab> datlabs) {
        this.datlabs = datlabs;
        return this;
    }

    public Tipvinculo addDatlab(Datlab datlab) {
        this.datlabs.add(datlab);
        datlab.setTipvinculo(this);
        return this;
    }

    public Tipvinculo removeDatlab(Datlab datlab) {
        this.datlabs.remove(datlab);
        datlab.setTipvinculo(null);
        return this;
    }

    public void setDatlabs(Set<Datlab> datlabs) {
        this.datlabs = datlabs;
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
        Tipvinculo tipvinculo = (Tipvinculo) o;
        if (tipvinculo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipvinculo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tipvinculo{" +
            "id=" + getId() +
            ", vDestipvin='" + getvDestipvin() + "'" +
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
