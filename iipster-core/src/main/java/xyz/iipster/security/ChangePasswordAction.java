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

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * Side bar action to change Password.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@SideBarItem(sectionId = Sections.UTILS, captionCode = "iipster.changePassword.label", order = 100)
@FontAwesomeIcon(FontAwesome.RECYCLE)
@Component
@UIScope
public class ChangePasswordAction implements Runnable {
    private final ChangePasswordComponent changePasswordComponent;

    @Autowired
    public ChangePasswordAction(ChangePasswordComponent changePasswordComponent) {
        this.changePasswordComponent = changePasswordComponent;
    }

    @Override
    public void run() {
        changePasswordComponent.setCurrentPassword(null);
        UI.getCurrent().addWindow(changePasswordComponent);
    }
}
