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

import com.google.common.eventbus.EventBus;
import com.ibm.as400.access.AS400SecurityException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.vaadin.spring.i18n.I18N;
import xyz.iipster.IbmiService;
import xyz.iipster.NewPasswordInvalidException;
import xyz.iipster.security.SecurityUtils;

import java.io.IOException;

/**
 * Change password window.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@Component
@UIScope
public class ChangePasswordComponent extends Window {
    private final PasswordField oldPasswordField = new PasswordField();
    private final PasswordField newPasswordField1 = new PasswordField();
    private final PasswordField newPasswordField2 = new PasswordField();
    private final Button okButton = new Button();
    private final Button cancelButton = new Button();

    private final I18N i18N;
    private final IbmiService ibmiService;
    private final EventBus eventBus;
    private final SecurityUtils securityUtils;

    private String currentPassword;
    private String userName;

    @Autowired
    public ChangePasswordComponent(I18N i18N, IbmiService ibmiService, EventBus eventBus, SecurityUtils securityUtils) {
        this.i18N = i18N;
        this.ibmiService = ibmiService;
        this.eventBus = eventBus;
        this.securityUtils = securityUtils;
    }

    @Override
    public void attach() {
        super.attach();

        setModal(true);
        setClosable(false);
        setResizable(false);

        setCaption(i18N.get("iipster.changePassword.title"));

        oldPasswordField.setCaption(i18N.get("iipster.changePassword.oldPassword.label"));
        newPasswordField1.setCaption(i18N.get("iipster.changePassword.newPassword1.label"));
        newPasswordField2.setCaption(i18N.get("iipster.changePassword.newPassword2.label"));

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeUndefined();
        vl.setMargin(true);

        if (currentPassword != null) {
            // currentPassowrd is not null, that means we are on login screen and the password is expired
            Label expiredLabel = new Label(i18N.get("iipster.changePassword.expired.text"));
            vl.addComponent(expiredLabel);
        }

        FormLayout fl = new FormLayout();
        fl.setSizeUndefined();
        fl.addComponents(oldPasswordField, newPasswordField1, newPasswordField2);
        vl.addComponent(fl);
        vl.setExpandRatio(fl, 1F);

        okButton.setCaption(i18N.get("iipster.ok"));
        okButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        okButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        okButton.addClickListener(e -> {
            try {
                ibmiService.changePassword(securityUtils.getAuthentication() == null ? userName :
                                securityUtils.getAuthentication().getPrincipal().toString(),
                        oldPasswordField.getValue(), newPasswordField1.getValue());
                close();
                eventBus.post(new PasswordChangedEvent(newPasswordField1.getValue()));
            } catch (BadCredentialsException e1) {
                Notification.show(i18N.get("iipster.changePassword.oldPassword.error"), Notification.Type.WARNING_MESSAGE);
                oldPasswordField.focus();
            } catch (IOException e1) {
                Notification.show("Error while changing password", Notification.Type.ERROR_MESSAGE);
            } catch (NewPasswordInvalidException e1) {
                Notification.show(i18N.get(e1.getMessageId()), Notification.Type.WARNING_MESSAGE);
                newPasswordField1.focus();
            }
        });


        cancelButton.setCaption(i18N.get("iipster.cancel"));
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.F12);
        cancelButton.addClickListener(e -> {
            close();
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSizeUndefined();
        hl.setSpacing(true);
        hl.addComponents(okButton, cancelButton);

        vl.addComponent(hl);
        vl.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);

        setContent(vl);

        if (currentPassword == null) {
            // currentPassword is null so we are not forced to change password during login
            oldPasswordField.setValue("");
            oldPasswordField.setEnabled(true);

            oldPasswordField.focus();
        } else {
            oldPasswordField.setValue(currentPassword);
            oldPasswordField.setEnabled(false);

            newPasswordField1.focus();
        }

        newPasswordField1.setValue("");
        newPasswordField2.setValue("");
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
