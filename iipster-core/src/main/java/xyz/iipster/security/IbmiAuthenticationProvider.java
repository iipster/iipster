/*
 * Copyright 2016 Damien Ferrand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.iipster.security;

import com.ibm.as400.access.*;
import com.ibm.as400.security.auth.UserProfilePrincipal;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import xyz.iipster.IbmiInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security Authentication Provider to perform authentication against IBM i user profiles.
 *
 * User special authorities are mapped as roles (for example *AUDIT special authority becomes
 * IIPSTER_SPECIAL_AUTHORITY_AUDIT).
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@Component
public class IbmiAuthenticationProvider implements AuthenticationProvider {
    private final AS400 as400;
    private final IbmiInformation ibmiInformation;

    public IbmiAuthenticationProvider(AS400 as400, IbmiInformation ibmiInformation) {
        this.as400 = as400;
        this.ibmiInformation = ibmiInformation;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String userName = authentication.getName();
        final String password = authentication.getCredentials().toString();
        try {
            if (as400.authenticate(userName, password)) {
                // Create an AS400 object with the authenticating user to be able to get its special authorities
                // this is needed if the user of the AS400 bean is not authorized on the authenticating user
                // profile
                AS400 currAs400 = new AS400(ibmiInformation.getAddress(), userName, password);
                User user = new User(currAs400, userName);
                List<GrantedAuthority> authorities = new ArrayList<>(user.getSpecialAuthority().length);
                for (String sa : user.getSpecialAuthority()) {
                    authorities.add(new SimpleGrantedAuthority("IIPSTER_SPECIAL_AUTHORITY_" +
                            sa.substring(1)));
                }
                return new UsernamePasswordAuthenticationToken(userName, password,
                        authorities);
            } else {
                return null;
            }
        } catch (AS400SecurityException e) {
            if (e.getReturnCode() == AS400SecurityException.USERID_UNKNOWN) {
                return null;
            }
            if (e.getReturnCode() == AS400SecurityException.PASSWORD_INCORRECT ||
                    e.getReturnCode() == AS400SecurityException.PASSWORD_INCORRECT_USERID_DISABLE) {
                throw new BadCredentialsException("Incorrect password", e);
            }
            if (e.getReturnCode() == AS400SecurityException.USERID_DISABLE) {
                throw new DisabledException("User disabled", e);
            }
            if (e.getReturnCode() == AS400SecurityException.PASSWORD_EXPIRED) {
                throw new CredentialsExpiredException("Password expired", e);
            }
            throw new AuthenticationServiceException("Could not authenticate", e);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Could not authenticate", e);
        } catch (InterruptedException e) {
            throw new AuthenticationServiceException("Interrupted while getting user profile", e);
        } catch (ObjectDoesNotExistException e) {
            throw new RuntimeException("Can't find user profile after authentication", e);
        } catch (ErrorCompletingRequestException e) {
            throw new RuntimeException("Error while getting user profile after authentications", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
