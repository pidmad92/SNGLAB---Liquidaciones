package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Docpresate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Docpresate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocpresateRepository extends JpaRepository<Docpresate, Long> {

}
