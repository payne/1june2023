package kunesti.api.invite;

import kunesti.api.model.SimplePage;
import kunesti.api.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class InviteService {

    private final InviteRepository inviteRepository;

    public InviteService(final InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    public SimplePage<InviteDTO> findAll(final String filter, final Pageable pageable) {
        Page<Invite> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = inviteRepository.findAllById(longFilter, pageable);
        } else {
            page = inviteRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(invite -> mapToDTO(invite, new InviteDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public InviteDTO get(final Long id) {
        return inviteRepository.findById(id)
                .map(invite -> mapToDTO(invite, new InviteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final InviteDTO inviteDTO) {
        final Invite invite = new Invite();
        mapToEntity(inviteDTO, invite);
        return inviteRepository.save(invite).getId();
    }

    public void update(final Long id, final InviteDTO inviteDTO) {
        final Invite invite = inviteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(inviteDTO, invite);
        inviteRepository.save(invite);
    }

    public void delete(final Long id) {
        inviteRepository.deleteById(id);
    }

    private InviteDTO mapToDTO(final Invite invite, final InviteDTO inviteDTO) {
        inviteDTO.setId(invite.getId());
        inviteDTO.setInviteToken(invite.getInviteToken());
        return inviteDTO;
    }

    private Invite mapToEntity(final InviteDTO inviteDTO, final Invite invite) {
        invite.setInviteToken(inviteDTO.getInviteToken());
        return invite;
    }

    public boolean inviteTokenExists(final String inviteToken) {
        return inviteRepository.existsByInviteTokenIgnoreCase(inviteToken);
    }

}
