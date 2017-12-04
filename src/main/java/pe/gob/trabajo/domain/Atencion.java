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
 * LISTA DE ATENCIONES A LOS TRABAJADORES Y/O EMPLEADORES
 */
@ApiModel(description = "LISTA DE ATENCIONES A LOS TRABAJADORES Y/O EMPLEADORES")
@Entity
@Table(name = "glmvc_atencion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "glmvc_atencion")
public class Atencion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codaten", nullable = false)
    private Long id;

    /**
     * OBSERVACION EN LA ATENCION.
     */
    @Size(max = 500)
    @ApiModelProperty(value = "OBSERVACION EN LA ATENCION.")
    @Column(name = "v_obsatenci", length = 500)
    private String vObsatenci;

    /**
     * BANDERA QUE INDICA QUE EL TRABAJADOR PRESENTA EMBARAZO AL MOMENTO DE LA ATENCION (EMBARAZO=1, NO EMBARAZO=0)
     */
    @NotNull
    @ApiModelProperty(value = "BANDERA QUE INDICA QUE EL TRABAJADOR PRESENTA EMBARAZO AL MOMENTO DE LA ATENCION (EMBARAZO=1, NO EMBARAZO=0)", required = true)
    @Column(name = "n_flgembara", nullable = false)
    private Boolean nFlgembara;

    /**
     * ESTADO DE LA ATENCION.
     */
    @NotNull
    @Size(max = 1)
    @ApiModelProperty(value = "ESTADO DE LA ATENCION.", required = true)
    @Column(name = "v_estado", length = 1, nullable = false)
    private String vEstado;

    /**
     * NUMERO DE TICKET DE LA ATENCION.
     */
    @NotNull
    @Size(max = 10)
    @ApiModelProperty(value = "NUMERO DE TICKET DE LA ATENCION.", required = true)
    @Column(name = "v_numticket", length = 10, nullable = false)
    private String vNumticket;

    /**
     * CODIGO DEL TRABAJADOR REPRESENTANTE DE LA EMPRESA A QUIEN SE REALIZA LA ATENCION.
     */
    @ApiModelProperty(value = "CODIGO DEL TRABAJADOR REPRESENTANTE DE LA EMPRESA A QUIEN SE REALIZA LA ATENCION.")
    @Column(name = "n_codtrepre")
    private Integer nCodtrepre;

    /**
     * CODIGO DEL USUARIO QUE REGISTRA.
     */
    @NotNull
    @ApiModelProperty(value = "CODIGO DEL USUARIO QUE REGISTRA.", required = true)
    @Column(name = "n_usuareg", nullable = false)
    private Integer nUsuareg;

    @NotNull
    @Column(name = "t_fecreg", nullable = false)
    private Instant tFecreg;

    /**
     * FECHA Y HORA DEL REGISTRO.
     */
    @NotNull
    @ApiModelProperty(value = "FECHA Y HORA DEL REGISTRO.", required = true)
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

    @OneToOne
    @JoinColumn(unique = true)
    private Liquidacion liquidacion;

    @OneToMany(mappedBy = "atencion")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Discapate> discapates = new HashSet<>();

    @OneToMany(mappedBy = "atencion")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Docpresate> docpresates = new HashSet<>();

    @OneToMany(mappedBy = "atencion")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Accadoate> accadoates = new HashSet<>();

    @OneToMany(mappedBy = "atencion")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Motateselec> motateselecs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "n_coddatlab")
    private Datlab datlab;

    @ManyToOne
    @JoinColumn(name = "n_codemplea")
    private Empleador empleador;

    @ManyToOne
    @JoinColumn(name = "n_codofic")
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "n_codtipate")
    private Tipatencion tipatencion;

    @ManyToOne
    @JoinColumn(name = "n_codtrab")
    private Trabajador trabajador;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvObsatenci() {
        return vObsatenci;
    }

    public Atencion vObsatenci(String vObsatenci) {
        this.vObsatenci = vObsatenci;
        return this;
    }

    public void setvObsatenci(String vObsatenci) {
        this.vObsatenci = vObsatenci;
    }

    public Boolean isnFlgembara() {
        return nFlgembara;
    }

    public Atencion nFlgembara(Boolean nFlgembara) {
        this.nFlgembara = nFlgembara;
        return this;
    }

    public void setnFlgembara(Boolean nFlgembara) {
        this.nFlgembara = nFlgembara;
    }

    public String getvEstado() {
        return vEstado;
    }

    public Atencion vEstado(String vEstado) {
        this.vEstado = vEstado;
        return this;
    }

    public void setvEstado(String vEstado) {
        this.vEstado = vEstado;
    }

    public String getvNumticket() {
        return vNumticket;
    }

    public Atencion vNumticket(String vNumticket) {
        this.vNumticket = vNumticket;
        return this;
    }

    public void setvNumticket(String vNumticket) {
        this.vNumticket = vNumticket;
    }

    public Integer getnCodtrepre() {
        return nCodtrepre;
    }

    public Atencion nCodtrepre(Integer nCodtrepre) {
        this.nCodtrepre = nCodtrepre;
        return this;
    }

    public void setnCodtrepre(Integer nCodtrepre) {
        this.nCodtrepre = nCodtrepre;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Atencion nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Atencion tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Atencion nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Atencion nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Atencion nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Atencion tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Atencion nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }

    public Liquidacion getLiquidacion() {
        return liquidacion;
    }

    public Atencion liquidacion(Liquidacion liquidacion) {
        this.liquidacion = liquidacion;
        return this;
    }

    public void setLiquidacion(Liquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Set<Discapate> getDiscapates() {
        return discapates;
    }

    public Atencion discapates(Set<Discapate> discapates) {
        this.discapates = discapates;
        return this;
    }

    public Atencion addDiscapate(Discapate discapate) {
        this.discapates.add(discapate);
        discapate.setAtencion(this);
        return this;
    }

    public Atencion removeDiscapate(Discapate discapate) {
        this.discapates.remove(discapate);
        discapate.setAtencion(null);
        return this;
    }

    public void setDiscapates(Set<Discapate> discapates) {
        this.discapates = discapates;
    }

    public Set<Docpresate> getDocpresates() {
        return docpresates;
    }

    public Atencion docpresates(Set<Docpresate> docpresates) {
        this.docpresates = docpresates;
        return this;
    }

    public Atencion addDocpresate(Docpresate docpresate) {
        this.docpresates.add(docpresate);
        docpresate.setAtencion(this);
        return this;
    }

    public Atencion removeDocpresate(Docpresate docpresate) {
        this.docpresates.remove(docpresate);
        docpresate.setAtencion(null);
        return this;
    }

    public void setDocpresates(Set<Docpresate> docpresates) {
        this.docpresates = docpresates;
    }

    public Set<Accadoate> getAccadoates() {
        return accadoates;
    }

    public Atencion accadoates(Set<Accadoate> accadoates) {
        this.accadoates = accadoates;
        return this;
    }

    public Atencion addAccadoate(Accadoate accadoate) {
        this.accadoates.add(accadoate);
        accadoate.setAtencion(this);
        return this;
    }

    public Atencion removeAccadoate(Accadoate accadoate) {
        this.accadoates.remove(accadoate);
        accadoate.setAtencion(null);
        return this;
    }

    public void setAccadoates(Set<Accadoate> accadoates) {
        this.accadoates = accadoates;
    }

    public Set<Motateselec> getMotateselecs() {
        return motateselecs;
    }

    public Atencion motateselecs(Set<Motateselec> motateselecs) {
        this.motateselecs = motateselecs;
        return this;
    }

    public Atencion addMotateselec(Motateselec motateselec) {
        this.motateselecs.add(motateselec);
        motateselec.setAtencion(this);
        return this;
    }

    public Atencion removeMotateselec(Motateselec motateselec) {
        this.motateselecs.remove(motateselec);
        motateselec.setAtencion(null);
        return this;
    }

    public void setMotateselecs(Set<Motateselec> motateselecs) {
        this.motateselecs = motateselecs;
    }

    public Datlab getDatlab() {
        return datlab;
    }

    public Atencion datlab(Datlab datlab) {
        this.datlab = datlab;
        return this;
    }

    public void setDatlab(Datlab datlab) {
        this.datlab = datlab;
    }

    public Empleador getEmpleador() {
        return empleador;
    }

    public Atencion empleador(Empleador empleador) {
        this.empleador = empleador;
        return this;
    }

    public void setEmpleador(Empleador empleador) {
        this.empleador = empleador;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Atencion oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Tipatencion getTipatencion() {
        return tipatencion;
    }

    public Atencion tipatencion(Tipatencion tipatencion) {
        this.tipatencion = tipatencion;
        return this;
    }

    public void setTipatencion(Tipatencion tipatencion) {
        this.tipatencion = tipatencion;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public Atencion trabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
        return this;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
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
        Atencion atencion = (Atencion) o;
        if (atencion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), atencion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Atencion{" +
            "id=" + getId() +
            ", vObsatenci='" + getvObsatenci() + "'" +
            ", nFlgembara='" + isnFlgembara() + "'" +
            ", vEstado='" + getvEstado() + "'" +
            ", vNumticket='" + getvNumticket() + "'" +
            ", nCodtrepre='" + getnCodtrepre() + "'" +
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
