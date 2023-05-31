package kunesti.api.notification;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Long id;

    @Size(max = 255)
    private String headline;

    private String body;

}
