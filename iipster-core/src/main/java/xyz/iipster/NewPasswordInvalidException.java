/*
 * Copyright 2017 Damien Ferrand
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
 *
 */

package xyz.iipster;

import com.ibm.as400.access.AS400SecurityException;

/**
 * Exception thrown when attempting to change a user password but the new password is rejected by the host.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
public class NewPasswordInvalidException extends Exception {
    private final AS400SecurityException sourceException;
    private final String messageId;

    public NewPasswordInvalidException(AS400SecurityException sourceException) {
        this.sourceException = sourceException;

        switch (sourceException.getReturnCode()) {
            case AS400SecurityException.PASSWORD_NEW_ADJACENT_DIGITS:
                messageId = "iipster.changePassword.error.adjacentDigits";
                break;
            case AS400SecurityException.PASSWORD_NEW_CHARACTER_NOT_VALID:
                messageId = "iipster.changePassword.error.characterNotValid";
                break;
            case AS400SecurityException.PASSWORD_NEW_CONSECUTIVE_REPEAT_CHARACTER:
                messageId = "iipster.changePassword.error.consecutiveRepeatCharacter";
                break;
            case AS400SecurityException.PASSWORD_NEW_DISALLOWED:
                messageId = "iipster.changePassword.error.disallowed";
                break;
            case AS400SecurityException.PASSWORD_NEW_NO_ALPHABETIC:
                messageId = "iipster.changePassword.error.noAlphabetic";
                break;
            case AS400SecurityException.PASSWORD_NEW_NO_NUMERIC:
                messageId = "iipster.changePassword.error.noNumeric";
                break;
            case AS400SecurityException.PASSWORD_NEW_NOT_VALID:
                messageId = "iipster.changePassword.error.notValid";
                break;
            case AS400SecurityException.PASSWORD_NEW_PREVIOUSLY_USED:
                messageId = "iipster.changePassword.error.previouslyUsed";
                break;
            case AS400SecurityException.PASSWORD_NEW_REPEAT_CHARACTER:
                messageId = "iipster.changePassword.error.repeatCharacter";
                break;
            case AS400SecurityException.PASSWORD_NEW_SAME_POSITION:
                messageId = "iipster.changePassword.error.samePosition";
                break;
            case AS400SecurityException.PASSWORD_NEW_TOO_LONG:
                messageId = "iipster.changePassword.error.tooLong";
                break;
            case AS400SecurityException.PASSWORD_NEW_TOO_SHORT:
                messageId = "iipster.changePassword.error.tooShort";
                break;
            case AS400SecurityException.PASSWORD_NEW_USERID:
                messageId = "iipster.changePassword.error.userid";
                break;
            case AS400SecurityException.PASSWORD_NEW_VALIDATION_PROGRAM:
                messageId = "iipster.changePassword.error.validationProgram";
                break;
            default:
                messageId = "iipster.changePassword.error.unknown";
        }
    }

    public AS400SecurityException getSourceException() {
        return sourceException;
    }

    public String getMessageId() {
        return messageId;
    }
}
