package kunesti.api.user;

import java.util.List;
import kunesti.api.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    Page<User> findAllById(Long id, Pageable pageable);

    boolean existsByUsernameIgnoreCase(String username);

    List<User> findAllByGroups(Group group);

}
