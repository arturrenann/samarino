package br.com.samarino.repository;

import br.com.samarino.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cliente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {

    @Query(value = "select distinct cliente from Cliente cliente left join fetch cliente.reservas",
        countQuery = "select count(distinct cliente) from Cliente cliente")
    Page<Cliente> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct cliente from Cliente cliente left join fetch cliente.reservas")
    List<Cliente> findAllWithEagerRelationships();

    @Query("select cliente from Cliente cliente left join fetch cliente.reservas where cliente.id =:id")
    Optional<Cliente> findOneWithEagerRelationships(@Param("id") Long id);

}
