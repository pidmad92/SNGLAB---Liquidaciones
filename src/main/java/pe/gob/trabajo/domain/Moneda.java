package pe.gob.trabajo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Moneda.
 */
@Entity
@Table(name = "litbc_moneda")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "litbc_moneda")
public class Moneda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "n_codmoneda", nullable = false)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "v_nommoneda", length = 100, nullable = false)
    private String vNommoneda;

    @NotNull
    @Column(name = "n_usuareg", nullable = false)
    private Integer nUsuareg;

    @NotNull
    @Column(name = "t_fecreg", nullable = false)
    private Instant tFecreg;

    @NotNull
    @Column(name = "n_flgactivo", nullable = false)
    private Boolean nFlgactivo;

    @NotNull
    @Column(name = "n_sedereg", nullable = false)
    private Integer nSedereg;

    @Column(name = "n_usuaupd")
    private Integer nUsuaupd;

    @Column(name = "t_fecupd")
    private Instant tFecupd;

    @Column(name = "n_sedeupd")
    private Integer nSedeupd;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvNommoneda() {
        return vNommoneda;
    }

    public Moneda vNommoneda(String vNommoneda) {
        this.vNommoneda = vNommoneda;
        return this;
    }

    public void setvNommoneda(String vNommoneda) {
        this.vNommoneda = vNommoneda;
    }

    public Integer getnUsuareg() {
        return nUsuareg;
    }

    public Moneda nUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
        return this;
    }

    public void setnUsuareg(Integer nUsuareg) {
        this.nUsuareg = nUsuareg;
    }

    public Instant gettFecreg() {
        return tFecreg;
    }

    public Moneda tFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
        return this;
    }

    public void settFecreg(Instant tFecreg) {
        this.tFecreg = tFecreg;
    }

    public Boolean isnFlgactivo() {
        return nFlgactivo;
    }

    public Moneda nFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
        return this;
    }

    public void setnFlgactivo(Boolean nFlgactivo) {
        this.nFlgactivo = nFlgactivo;
    }

    public Integer getnSedereg() {
        return nSedereg;
    }

    public Moneda nSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
        return this;
    }

    public void setnSedereg(Integer nSedereg) {
        this.nSedereg = nSedereg;
    }

    public Integer getnUsuaupd() {
        return nUsuaupd;
    }

    public Moneda nUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
        return this;
    }

    public void setnUsuaupd(Integer nUsuaupd) {
        this.nUsuaupd = nUsuaupd;
    }

    public Instant gettFecupd() {
        return tFecupd;
    }

    public Moneda tFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
        return this;
    }

    public void settFecupd(Instant tFecupd) {
        this.tFecupd = tFecupd;
    }

    public Integer getnSedeupd() {
        return nSedeupd;
    }

    public Moneda nSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
        return this;
    }

    public void setnSedeupd(Integer nSedeupd) {
        this.nSedeupd = nSedeupd;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Moneda moneda = (Moneda) o;
        if (moneda.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), moneda.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Moneda{" +
            "id=" + getId() +
            ", vNommoneda='" + getvNommoneda() + "'" +
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
