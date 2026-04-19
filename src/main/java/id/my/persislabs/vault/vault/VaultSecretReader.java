package id.my.persislabs.vault.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-vault-sample
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 19/04/26
 * Time: 10.03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VaultSecretReader {

    private final Optional<VaultTemplate> vaultTemplate;

    @Value("${spring.cloud.vault.kv.backend:secret}")
    private String backend;

    @Value("${spring.cloud.vault.kv.default-context:spring-boot-vault-sample}")
    private String context;

    @EventListener(ApplicationReadyEvent.class)
    public void readSecretOnStartup() {
        if (vaultTemplate.isEmpty()) {
            log.info("VaultTemplate not available — skipping Vault secret read.");
            return;
        }
        String path = "%s/data/%s".formatted(backend, context);
        try {
            VaultResponse response = vaultTemplate.get().read(path);
            if (response == null || response.getData() == null) {
                log.warn("No secret found at path {}", path);
                return;
            }
            Object data = response.getData().get("data");
            if (data instanceof Map<?, ?> kv) {
                kv.forEach((k, v) -> log.info("Vault[{}] {} = {}", context, k, mask(String.valueOf(v))));
            }
        } catch (Exception ex) {
            log.warn("Unable to read secret from Vault at {}: {}", path, ex.getMessage());
        }
    }

    private String mask(String value) {
        if (value == null || value.length() <= 2) {
            return "***";
        }
        return value.charAt(0) + "***" + value.charAt(value.length() - 1);
    }
}
