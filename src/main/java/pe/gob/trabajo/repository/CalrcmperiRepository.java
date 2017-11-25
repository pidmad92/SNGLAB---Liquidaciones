package pe.gob.trabajo.repository;

import pe.gob.trabajo.domain.Calrcmperi;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Calrcmperi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalrcmperiRepository extends JpaRepository<Calrcmperi, Long> {

}
