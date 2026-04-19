package id.my.persislabs.vault.exception;

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
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
