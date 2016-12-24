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

package xyz.iipster.ui;

/**
 * Event fired when the user password is successfully changed.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
public class PasswordChangedEvent {
    private final String newPassword;

    public PasswordChangedEvent(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the new user password
     */
    public String getNewPassword() {
        return newPassword;
    }
}
