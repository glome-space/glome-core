package space.glome.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import space.glome.master.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
