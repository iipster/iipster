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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;


/**
 * Logout action.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@SideBarItem(sectionId = Sections.UTILS, captionCode = "iipster.logout.label", order = 200)
@Component
@FontAwesomeIcon(FontAwesome.SIGN_OUT)
public class LogoutAction implements Runnable {
    private final SecurityUtils securityUtils;

    @Autowired
    public LogoutAction(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public void run() {
        securityUtils.logout();
    }
}
