package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Dirperjuri;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Dirperjuri entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirperjuriRepository extends JpaRepository<Dirperjuri, Long> {

}
