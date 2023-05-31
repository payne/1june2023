package kunesti.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("kunesti.api")
@EnableJpaRepositories("kunesti.api")
@EnableTransactionManagement
public class DomainConfig {
}
