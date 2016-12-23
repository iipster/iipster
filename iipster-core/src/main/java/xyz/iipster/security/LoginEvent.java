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

/**
 * Event fired when the user logs in.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
public class LoginEvent {
    private final String userName;

    public LoginEvent(String userName) {
        this.userName = userName;
    }

    /**
     * @return the name of the logged in user.
     */
    public String getUserName() {
        return userName;
    }
}
