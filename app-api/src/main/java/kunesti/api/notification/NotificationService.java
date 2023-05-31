package kunesti.api.notification;

import kunesti.api.model.SimplePage;
import kunesti.api.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(final NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public SimplePage<NotificationDTO> findAll(final String filter, final Pageable pageable) {
        Page<Notification> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = notificationRepository.findAllById(longFilter, pageable);
        } else {
            page = notificationRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public NotificationDTO get(final Long id) {
        return notificationRepository.findById(id)
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final NotificationDTO notificationDTO) {
        final Notification notification = new Notification();
        mapToEntity(notificationDTO, notification);
        return notificationRepository.save(notification).getId();
    }

    public void update(final Long id, final NotificationDTO notificationDTO) {
        final Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationDTO, notification);
        notificationRepository.save(notification);
    }

    public void delete(final Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO mapToDTO(final Notification notification,
            final NotificationDTO notificationDTO) {
        notificationDTO.setId(notification.getId());
        notificationDTO.setHeadline(notification.getHeadline());
        notificationDTO.setBody(notification.getBody());
        return notificationDTO;
    }

    private Notification mapToEntity(final NotificationDTO notificationDTO,
            final Notification notification) {
        notification.setHeadline(notificationDTO.getHeadline());
        notification.setBody(notificationDTO.getBody());
        return notification;
    }

}
