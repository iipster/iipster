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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * IBM i services implementation.
 */
@Component
public class IbmiServiceImpl implements IbmiService {
    private final IbmiInformation ibmiInformation;

    public IbmiServiceImpl(IbmiInformation ibmiInformation) {
        this.ibmiInformation = ibmiInformation;
    }

    @Override
    public void changePassword(String userName, String oldPassword, String newPassword) throws AS400SecurityException,
            BadCredentialsException, IOException {
        AS400 tmpAs400 = new AS400(ibmiInformation.getAddress(), userName);

        try {
            tmpAs400.changePassword(oldPassword, newPassword);
        } catch (AS400SecurityException e) {
            switch (e.getReturnCode()) {
                case AS400SecurityException.PASSWORD_OLD_NOT_VALID:
                    throw new BadCredentialsException("Old password not valid", e);
                default:
                    throw e;
            }
        }
    }
}
