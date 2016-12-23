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

import org.springframework.security.core.Authentication;

/**
 * Security utils interface.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
public interface SecurityUtils {

    /**
     * Authenticates the user for the current Vaadin session.
     * @param userName the user name
     * @param password the user password
     * @return the Authentication object.
     */
    Authentication login(String userName, String password);

    /**
     * @return the Authentication object for the current Vaadin session, null if there is no authentication.
     */
    Authentication getAuthentication();

    /**
     * @return true if the Vaadin session is authenticated, false otherwise.
     */
    boolean isLoggedIn();

    /**
     * Checks whether the current user has the given role or not.
     * @param role the name of the role to check.
     * @return true if the Vaadin session is authenticated and the user has the given role, false otherwise.
     */
    boolean hasRole(String role);

    /**
     * Logs the user out of the current Vaadin sesion.
     */
    void logout();
}
