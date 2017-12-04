package pe.gob.trabajo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * LISTA DE LAS DIRECCIONES DE LAS PERSONAS NATURALES
 */
@ApiModel(description = "LISTA DE LAS DIRECCIONES DE LAS PERSONAS NATURALES")
@Entity
@Table(name = "glmvd_dirpernat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "glmvd_dirpernat")
public class Dirpernat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codpnadir", nullable = false)
    private Long id;

    /**
     * CODIGO DEL DEPARTAMENTO.
     */
    @ApiModelProperty(value = "CODIGO DEL DEPARTAMENTO.")
    @Column(name = "n_coddepto")
    private Integer nCoddepto;

    /**
     * CODIGO D ELA PROVINCIA.
     */
    @ApiModelProperty(value = "CODIGO D ELA PROVINCIA.")
    @Column(name = "n_codprov")
    private Integer nCodprov;

    /**
     * CODIGO DEL DISTRITO.
     */
    @ApiModelProperty(value = "CODIGO DEL DISTRITO.")
    @Column(name = "n_coddist")
    private Integer nCoddist;

    /**
     * DIRECCION COMPLETA
     */
    @NotNull
    @Size(max = 200)
    @ApiModelProperty(value = "DIRECCION COMPLETA", required = true)
    @Column(name = "v_dircomple", length = 200, nullable = false)
    private String vDircomple;

    /**
     * REFERENCIA DE LA DIRECCION.
     */
    @Size(max = 500)
    @ApiModelProperty(value = "REFERENCIA DE LA DIRECCION.")
    @Column(name = "v_referen", length = 500)
    private String vReferen;

    /**
     * BANDERA QUE INDICA QUE LA DIRECCION ESTA ACTIVO O NO PARA ENVIAR NOTIFICACION (ACTIVO=1, INACTIVO=0)
     */
    @NotNull
    @ApiModelProperty(value = "BANDERA QUE INDICA QUE LA DIRECCION ESTA ACTIVO O NO PARA ENVIAR NOTIFICACION (ACTIVO=1, INACTIVO=0)", required = true)
    @Column(name = "n_flgnotifi", nullable = false)
    private Boolean nFlgnotifi;

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
     * FECHA Y HORA DE LA MODIFICACION DEL REGISTRO.
     */
    @ApiModelProperty(value = "FECHA Y HORA DE LA MODIFICACION DEL REGISTRO.")
    @Column(name = "t_fecupd")
    private Instant tFecupd;

    /**
     * CODIGO DE LA SEDE DONDE SE MODIFICA EL REGISTRO.
     */
    @ApiModelProperty(value = "CODIGO DE LA SEDE DONDE SE MODIFICA EL REGISTRO.")
    @Column(name = "n_sedeupd")
    private Integer nSedeupd;

    @ManyToOne
    @JoinColumn(name = "n_codpernat")
    private Pernatural pernatural;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getnCoddepto() {
        return nCoddepto;
    }

    public Dirpernat nCoddepto(Integer nCoddepto) {
        this.nCoddepto = nCoddepto;
        return this;
    }

    public void setnCoddepto(Integer nCoddepto) {
        this.nCoddepto = nCoddepto;
    }

    public Integer getnCodprov() {
        return nCodprov;
    }

    public Dirpernat nCodprov(Integer nCodprov) {
        this.nCodprov = nCodprov;
        return this;
    }

    public void setnCodprov(Integer nCodprov) {
        this.nCodprov = nCodprov;
    }

    public Integer getnCoddist() {
        return nCoddist;
    }

    public Dirpernat nCoddist(Integer nCoddist) {
        this.nCoddist = nCoddist;
        return this;
    }

    public void setnCoddist(Integer nCoddist) {
        this.nCoddist = nCoddist;
    }

    public String getvDircomple() {
        return vDircomple;
    }

    public Dirpernat vDircomple(String vDircomple) {
        this.vDircomple = vDircomple;
        return this;
    }

    public void setvDircomple(String vDircomple) {
        this.vDircomple = vDircomple;
    }

    public String getvReferen() {
        return vReferen;
    }

    public Dirpernat vReferen(String vReferen) {
        this.vReferen = vReferen;
        return this;
    }

    public void setvReferen(String vReferen) {
        this.vReferen = vReferen;
    }

    public Boolean isnFlgnotifi() {
        return nFlgnotifi;
    }

    public Dirpernat nFlgnotifi(Boolean nFlgnotifi) {
        this.nFlgnotifi = nFlgnotifi;
        return this;
    }

    public void setnFlgnotifi(Boolean nFlgnotifi) {
        this.nFlgnotifi = nFlgnotifi;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Dirpernat nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Dirpernat tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Dirpernat nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Dirpernat nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Dirpernat nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Dirpernat tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Dirpernat nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Pernatural getPernatural() {
        return pernatural;
    }

    public Dirpernat pernatural(Pernatural pernatural) {
        this.pernatural = pernatural;
        return this;
    }

    public void setPernatural(Pernatural pernatural) {
        this.pernatural = pernatural;
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
        Dirpernat dirpernat = (Dirpernat) o;
        if (dirpernat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dirpernat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Dirpernat{" +
            "id=" + getId() +
            ", nCoddepto='" + getnCoddepto() + "'" +
            ", nCodprov='" + getnCodprov() + "'" +
            ", nCoddist='" + getnCoddist() + "'" +
            ", vDircomple='" + getvDircomple() + "'" +
            ", vReferen='" + getvReferen() + "'" +
            ", nFlgnotifi='" + isnFlgnotifi() + "'" +
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
