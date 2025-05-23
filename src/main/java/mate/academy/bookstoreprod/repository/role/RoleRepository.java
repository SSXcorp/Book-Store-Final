package mate.academy.bookstoreprod.repository.role;

import java.util.Optional;
import mate.academy.bookstoreprod.model.Role;
import mate.academy.bookstoreprod.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
