package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Dirpernat;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Dirpernat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirpernatRepository extends JpaRepository<Dirpernat, Long> {

}
