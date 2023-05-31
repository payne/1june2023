package kunesti.api.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupRepository extends JpaRepository<Group, Long> {

    Page<Group> findAllById(Long id, Pageable pageable);

}
