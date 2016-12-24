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
import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.vaadin.spring.i18n.I18N;
import xyz.iipster.security.SecurityUtils;

/**
 * The default login form. It will be used if no IbmiLoginComponent bean is defined.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@UIScope
@Component
public class DefaultIbmiLoginComponent extends IbmiLoginComponent {
    private final EventBus eventBus;
    private final SecurityUtils securityUtils;
    private final I18N i18N;

    private final TextField userNameTF = new TextField();
    private final PasswordField passwordPF = new PasswordField();
    private final Button loginButton = new Button();
    private final VerticalLayout rootLayout = new VerticalLayout();
    private final ChangePasswordComponent changePasswordComponent;

    public DefaultIbmiLoginComponent(EventBus eventBus, SecurityUtils securityUtils, I18N i18N,
                                     ChangePasswordComponent changePasswordComponent) {
        this.eventBus = eventBus;
        this.securityUtils = securityUtils;
        this.i18N = i18N;
        this.changePasswordComponent = changePasswordComponent;

        setCompositionRoot(rootLayout);

        eventBus.register(this);
    }

    @Override
    public void attach() {
        super.attach();
        final FormLayout fl = new FormLayout();
        fl.setSizeUndefined();

        userNameTF.setCaption(i18N.get("iipster.login.username.label"));
        userNameTF.setRequired(true);
        userNameTF.addStyleName("upper-case");
        userNameTF.setMaxLength(10);
        passwordPF.setCaption(i18N.get("iipster.login.password.label"));
        passwordPF.setRequired(true);

        fl.addComponent(userNameTF);
        fl.addComponent(passwordPF);

        final VerticalLayout vl = new VerticalLayout();
        vl.setSizeUndefined();
        vl.addComponent(fl);
        vl.setExpandRatio(fl, 1F);
        vl.setComponentAlignment(fl, Alignment.MIDDLE_CENTER);

        final HorizontalLayout hl = new HorizontalLayout();
        hl.setSizeUndefined();

        loginButton.setCaption(i18N.get("iipster.login.loginButton.label"));
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener(event -> {
            if (userNameTF.isEmpty()) {
                Notification.show(i18N.get("iipster.login.username.missing"), Notification.Type.WARNING_MESSAGE);
                userNameTF.focus();
                return;
            }
            if (passwordPF.isEmpty()) {
                Notification.show(i18N.get("iipster.login.password.missing"), Notification.Type.WARNING_MESSAGE);
                passwordPF.focus();
                return;
            }

            try {
                Authentication auth = securityUtils.login(userNameTF.getValue(), passwordPF.getValue());
                eventBus.post(new LoginEvent(auth.getPrincipal().toString()));
            } catch (BadCredentialsException e) {
                Notification.show(i18N.get("iipster.login.bad.credential"), Notification.Type.WARNING_MESSAGE);
            } catch (DisabledException e) {
                Notification.show(i18N.get("iipster.login.disabled"), Notification.Type.WARNING_MESSAGE);
            } catch (CredentialsExpiredException e) {
                changePasswordComponent.setUserName(userNameTF.getValue());
                changePasswordComponent.setCurrentPassword(passwordPF.getValue());
                UI.getCurrent().addWindow(changePasswordComponent);
            } catch (Exception e) {
                Notification.show(i18N.get("iipster.login.error"), Notification.Type.WARNING_MESSAGE);
            }
        });
        hl.addComponent(loginButton);
        vl.addComponent(hl);
        vl.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
        vl.setSizeUndefined();

        vl.setMargin(true);
        final Panel panel = new Panel(i18N.get("iipster.login.panel.title"), vl);
        panel.setSizeUndefined();

        rootLayout.addComponent(panel);
        rootLayout.setSizeFull();
        rootLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setCompositionRoot(rootLayout);
        setSizeFull();
    }

    @Subscribe
    public void onPasswordChanged(PasswordChangedEvent e) {
        Authentication auth;
        try {
            auth = securityUtils.login(userNameTF.getValue(), e.getNewPassword());
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        eventBus.post(new LoginEvent(auth.getPrincipal().toString()));
    }

    @Override
    public void show() {
        userNameTF.clear();
        passwordPF.clear();
        userNameTF.focus();
    }

}
