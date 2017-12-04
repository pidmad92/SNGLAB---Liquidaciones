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
 * LISTA DE MOTIVOS DE ATENCION SELECCIONADAS EN CADA ATENCION
 */
@ApiModel(description = "LISTA DE MOTIVOS DE ATENCION SELECCIONADAS EN CADA ATENCION")
@Entity
@Table(name = "glmvd_motateselec")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "glmvd_motateselec")
public class Motateselec implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codmtasel", nullable = false)
    private Long id;

    /**
     * OBSERVIACION DEL MOTIVO DE ATENCION SELECCIONADO EN LA ATENCION.
     */
    @Size(max = 500)
    @ApiModelProperty(value = "OBSERVIACION DEL MOTIVO DE ATENCION SELECCIONADO EN LA ATENCION.")
    @Column(name = "v_obsmoseat", length = 500)
    private String vObsmoseat;

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
     * CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO.FECHA Y HORA DE LA MODIFICACION DEL REGISTRO.
     */
    @ApiModelProperty(value = "CODIGO DEL USUARIO QUE MODIFICA EL REGISTRO.FECHA Y HORA DE LA MODIFICACION DEL REGISTRO.")
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
    @JoinColumn(name = "n_codaten")
    private Atencion atencion;

    @ManyToOne
    @JoinColumn(name = "n_coddalter")
    private Direcalter direcalter;

    @ManyToOne
    @JoinColumn(name = "n_codmtatof")
    private Motatenofic motatenofic;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvObsmoseat() {
        return vObsmoseat;
    }

    public Motateselec vObsmoseat(String vObsmoseat) {
        this.vObsmoseat = vObsmoseat;
        return this;
    }

    public void setvObsmoseat(String vObsmoseat) {
        this.vObsmoseat = vObsmoseat;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Motateselec nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Motateselec tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Motateselec nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Motateselec nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Motateselec nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Motateselec tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Motateselec nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Atencion getAtencion() {
        return atencion;
    }

    public Motateselec atencion(Atencion atencion) {
        this.atencion = atencion;
        return this;
    }

    public void setAtencion(Atencion atencion) {
        this.atencion = atencion;
    }

    public Direcalter getDirecalter() {
        return direcalter;
    }

    public Motateselec direcalter(Direcalter direcalter) {
        this.direcalter = direcalter;
        return this;
    }

    public void setDirecalter(Direcalter direcalter) {
        this.direcalter = direcalter;
    }

    public Motatenofic getMotatenofic() {
        return motatenofic;
    }

    public Motateselec motatenofic(Motatenofic motatenofic) {
        this.motatenofic = motatenofic;
        return this;
    }

    public void setMotatenofic(Motatenofic motatenofic) {
        this.motatenofic = motatenofic;
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
        Motateselec motateselec = (Motateselec) o;
        if (motateselec.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), motateselec.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Motateselec{" +
            "id=" + getId() +
            ", vObsmoseat='" + getvObsmoseat() + "'" +
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
