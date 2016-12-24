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
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;
import xyz.iipster.security.SecurityUtils;

/**
 * This is the default UI. It is responsible for displaying the Login screen if user is not authenticated or
 * the main component if authenticated. It will be used unless a bean of type IbmiUI is declared.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@SpringUI
@Theme("iipster")
@EnableI18N
@EnableSideBar
public class DefaultIbmiUI extends IbmiUI {
    private final SecurityUtils securityUtils;
    private final String pageTitle;
    private final IbmiLoginComponent ibmiLoginComponent;
    private final IbmiMainComponent ibmiMainComponent;

    public DefaultIbmiUI(SecurityUtils securityUtils, @Value("${iipster.page.title:iipster}") String pageTitle,
                         IbmiLoginComponent ibmiLoginComponent,
                         EventBus eventBus, IbmiMainComponent ibmiMainComponent) {
        this.securityUtils = securityUtils;
        this.pageTitle = pageTitle;
        this.ibmiLoginComponent = ibmiLoginComponent;
        this.ibmiMainComponent = ibmiMainComponent;

        eventBus.register(this);
    }

    @Override
    protected void init(VaadinRequest request) {
        Page.getCurrent().setTitle(pageTitle);

        if (securityUtils.isLoggedIn()) {
            showMain();
        } else {
            showLogin();
        }
    }

    @Subscribe
    public void onLogin(LoginEvent loginEvent) {
        showMain();
    }

    protected void showLogin() {
        setContent(ibmiLoginComponent);
        ibmiLoginComponent.show();
    }

    protected void showMain() {
        setContent(ibmiMainComponent);
    }
}
