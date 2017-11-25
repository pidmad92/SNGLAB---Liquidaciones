package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Tipinteres;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tipinteres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipinteresRepository extends JpaRepository<Tipinteres, Long> {

}
