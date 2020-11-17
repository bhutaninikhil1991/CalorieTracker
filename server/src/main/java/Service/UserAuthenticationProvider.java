package Service;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import models.User;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Optional;

/**
 * user authentication provider
 */
@Singleton
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Inject
    protected UserService userService;
    @Inject
    protected PasswordEncoder passwordEncoder;

    /**
     * authenticate user
     *
     * @param httpRequest
     * @param authenticationRequest
     * @return Publisher<AuthenticationResponse>
     */
    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            User user = fetchUser(authenticationRequest);
            Optional<AuthenticationFailed> authenticationFailed = validate(user, authenticationRequest);
            if (authenticationFailed.isPresent()) {
                emitter.onError(new AuthenticationException(authenticationFailed.get()));
                emitter.onComplete();
            } else {
                emitter.onNext(new UserDetails(String.valueOf(user.getId()), new ArrayList<>()));
            }
        }, BackpressureStrategy.ERROR);
    }

    /**
     * fetch user record
     *
     * @param authenticationRequest
     * @return User
     */
    protected User fetchUser(AuthenticationRequest authenticationRequest) {
        final String emailAddress = authenticationRequest.getIdentity().toString();
        return userService.findUserByEmailAddress(emailAddress);
    }

    /**
     * validate user
     *
     * @param user
     * @param authenticationRequest
     * @return Optional<AuthenticationFailed>
     */
    protected Optional<AuthenticationFailed> validate(User user, AuthenticationRequest authenticationRequest) {
        AuthenticationFailed authenticationFailed = null;
        if (user == null) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND);

        } else if (!passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }
        return Optional.ofNullable(authenticationFailed);
    }
}
