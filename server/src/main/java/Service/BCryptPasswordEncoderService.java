package Service;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * password encoding service
 */
@Singleton
public class BCryptPasswordEncoderService implements PasswordEncoder {

    private org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();

    /**
     * encode password
     *
     * @param rawPassword
     * @return String
     */
    @Override
    public String encode(@NonNull @NotBlank String rawPassword) {
        return delegate.encode(rawPassword);
    }

    /**
     * check whether password matches
     *
     * @param rawPassword
     * @param encodedPassword
     * @return boolean
     */
    @Override
    public boolean matches(@NonNull @NotBlank String rawPassword, @NonNull @NotBlank String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
