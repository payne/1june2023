package kunesti.api.group;

import jakarta.transaction.Transactional;
import kunesti.api.model.SimplePage;
import kunesti.api.user.UserRepository;
import kunesti.api.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(final GroupRepository groupRepository,
            final UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public SimplePage<GroupDTO> findAll(final String filter, final Pageable pageable) {
        Page<Group> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = groupRepository.findAllById(longFilter, pageable);
        } else {
            page = groupRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(group -> mapToDTO(group, new GroupDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public GroupDTO get(final Long id) {
        return groupRepository.findById(id)
                .map(group -> mapToDTO(group, new GroupDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final GroupDTO groupDTO) {
        final Group group = new Group();
        mapToEntity(groupDTO, group);
        return groupRepository.save(group).getId();
    }

    public void update(final Long id, final GroupDTO groupDTO) {
        final Group group = groupRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(groupDTO, group);
        groupRepository.save(group);
    }

    public void delete(final Long id) {
        final Group group = groupRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByGroups(group)
                .forEach(user -> user.getGroups().remove(group));
        groupRepository.delete(group);
    }

    private GroupDTO mapToDTO(final Group group, final GroupDTO groupDTO) {
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        return groupDTO;
    }

    private Group mapToEntity(final GroupDTO groupDTO, final Group group) {
        group.setName(groupDTO.getName());
        return group;
    }

}
