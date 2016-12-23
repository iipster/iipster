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

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security Authentication Provider to perform authentication against IBM i user profiles.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@Component
public class IbmiAuthenticationProvider implements AuthenticationProvider {
    private final AS400 as400;

    public IbmiAuthenticationProvider(AS400 as400) {
        this.as400 = as400;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String userName = authentication.getName();
        final String password = authentication.getCredentials().toString();
        try {
            if (as400.authenticate(userName, password)) {
                List<GrantedAuthority> authorities = new ArrayList<>();
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
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
