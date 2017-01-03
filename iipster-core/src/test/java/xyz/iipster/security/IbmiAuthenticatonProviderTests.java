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
import org.junit.Test;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * IbmiAuthenticationProvider unit tests.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
public class IbmiAuthenticatonProviderTests {

    /**
     * Should support UsernamePasswordAuthenticationToken and JaasAuthenticationToken but nothing else.
     */
    @Test
    public void supportsTest() {
        final AS400 as400 = mock(AS400.class);
        final IbmiAuthenticationProvider iap = new IbmiAuthenticationProvider(as400, null);

        assertThat(iap.supports(UsernamePasswordAuthenticationToken.class)).isTrue();

        assertThat(iap.supports(JaasAuthenticationToken.class)).isTrue();

        assertThat(iap.supports(AnonymousAuthenticationToken.class)).isFalse();
    }

    /**
     * Successful authentication should return an authenticated Authentication.
     * TODO: refactor IbmiAuthenticationProvider to be able to test this case
     */
//    @Test
    public void authenticateTestSuccess() throws IOException, AS400SecurityException {
        final AS400 as400 = mock(AS400.class);
        when(as400.authenticate("user", "password")).thenReturn(true);
        final IbmiAuthenticationProvider iap = new IbmiAuthenticationProvider(as400, null);

        final Authentication upat = iap.authenticate(new UsernamePasswordAuthenticationToken("user", "password"));

        assertThat(upat).isNotNull()
                .isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(upat.isAuthenticated()).isTrue();
    }

    @Test
    public void authenticateTestUnknown() throws Exception {
        final AS400 as400 = mock(AS400.class);
        when(as400.authenticate("nouser", "password"))
                .thenThrow(createException(AS400SecurityException.USERID_UNKNOWN));
        final IbmiAuthenticationProvider iap = new IbmiAuthenticationProvider(as400, null);

        assertThatThrownBy(() -> {
            iap.authenticate(new UsernamePasswordAuthenticationToken("nouser", "password"));
        }).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void authenticateTestIncorrectPassword() throws Exception {
        final AS400 as400 = mock(AS400.class);
        when(as400.authenticate("user", "wrongPassword"))
                .thenThrow(createException(AS400SecurityException.PASSWORD_INCORRECT));
        final IbmiAuthenticationProvider iap = new IbmiAuthenticationProvider(as400, null);

        assertThatThrownBy(() -> {
            iap.authenticate(new UsernamePasswordAuthenticationToken("user", "wrongPassword"));
        }).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    public void authenticateTestIncorrectPasswordWithDisabled() throws Exception {
        final AS400 as400 = mock(AS400.class);
        when(as400.authenticate("user", "wrongPassword"))
                .thenThrow(createException(AS400SecurityException.PASSWORD_INCORRECT_USERID_DISABLE));
        final IbmiAuthenticationProvider iap = new IbmiAuthenticationProvider(as400, null);

        assertThatThrownBy(() -> {
            iap.authenticate(new UsernamePasswordAuthenticationToken("user", "wrongPassword"));
        }).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    public void authenticateTestUserDisabled() throws Exception {
        final AS400 as400 = mock(AS400.class);
        when(as400.authenticate("user", "password"))
                .thenThrow(createException(AS400SecurityException.USERID_DISABLE));
        final IbmiAuthenticationProvider iap = new IbmiAuthenticationProvider(as400, null);

        assertThatThrownBy(() -> {
            iap.authenticate(new UsernamePasswordAuthenticationToken("user", "password"));
        }).isInstanceOf(DisabledException.class);
    }

    @Test
    public void authenticateTestPasswordExpired() throws Exception {
        final AS400 as400 = mock(AS400.class);
        when(as400.authenticate("user", "password"))
                .thenThrow(createException(AS400SecurityException.PASSWORD_EXPIRED));
        final IbmiAuthenticationProvider iap = new IbmiAuthenticationProvider(as400, null);

        assertThatThrownBy(() -> {
            iap.authenticate(new UsernamePasswordAuthenticationToken("user", "password"));
        }).isInstanceOf(CredentialsExpiredException.class);
    }

    private AS400SecurityException createException(int errorCode) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<AS400SecurityException> c = AS400SecurityException.class.getDeclaredConstructor(int.class);
        c.setAccessible(true);
        return c.newInstance(errorCode);
    }
}
