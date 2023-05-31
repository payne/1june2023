package kunesti.api.user;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kunesti.api.group.Group;
import kunesti.api.group.GroupRepository;
import kunesti.api.invite.Invite;
import kunesti.api.invite.InviteRepository;
import kunesti.api.model.SimplePage;
import kunesti.api.notification.Notification;
import kunesti.api.notification.NotificationRepository;
import kunesti.api.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final InviteRepository inviteRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final GroupRepository groupRepository,
            final InviteRepository inviteRepository,
            final NotificationRepository notificationRepository,
            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.inviteRepository = inviteRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SimplePage<UserDTO> findAll(final String filter, final Pageable pageable) {
        Page<User> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = userRepository.findAllById(longFilter, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setGroups(user.getGroups().stream()
                .map(group -> group.getId())
                .toList());
        userDTO.setInvite(user.getInvite() == null ? null : user.getInvite().getId());
        userDTO.setNotification(user.getNotification() == null ? null : user.getNotification().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setHash(passwordEncoder.encode(userDTO.getHash()));
        final List<Group> groups = groupRepository.findAllById(
                userDTO.getGroups() == null ? Collections.emptyList() : userDTO.getGroups());
        if (groups.size() != (userDTO.getGroups() == null ? 0 : userDTO.getGroups().size())) {
            throw new NotFoundException("one of groups not found");
        }
        user.setGroups(groups.stream().collect(Collectors.toSet()));
        final Invite invite = userDTO.getInvite() == null ? null : inviteRepository.findById(userDTO.getInvite())
                .orElseThrow(() -> new NotFoundException("invite not found"));
        user.setInvite(invite);
        final Notification notification = userDTO.getNotification() == null ? null : notificationRepository.findById(userDTO.getNotification())
                .orElseThrow(() -> new NotFoundException("notification not found"));
        user.setNotification(notification);
        return user;
    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

}
