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

package xyz.iipster;

import com.ibm.as400.access.AS400SecurityException;

import java.io.IOException;

/**
 * IBM i services.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
public interface IbmiService {

    /**
     * Change the given user profile password.
     *
     * @param userName    the user profile name
     * @param oldPassword the current password
     * @param newPassword the new password
     * @throws AS400SecurityException
     * @throws IOException
     */
    void changePassword(String userName, String oldPassword, String newPassword) throws IOException, NewPasswordInvalidException;
}
