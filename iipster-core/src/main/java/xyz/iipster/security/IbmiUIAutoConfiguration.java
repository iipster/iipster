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

import com.google.common.eventbus.EventBus;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;

/**
 * Auto configuration for the UI.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@Configuration
@Import({Sections.class, LogoutAction.class, ChangePasswordComponent.class, IbmiServiceImpl.class,
        ChangePasswordAction.class})
public class IbmiUIAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(EventBus.class)
    @UIScope
    public EventBus eventBus() {
        return new EventBus();
    }

    @ConditionalOnMissingBean(xyz.iipster.security.IbmiUI.class)
    @Import(DefaultIbmiUI.class)
    protected static class IbmiUI {

    }

    @ConditionalOnMissingBean(xyz.iipster.security.IbmiLoginComponent.class)
    @Import(DefaultIbmiLoginComponent.class)
    protected static class IbmiLoginComponent {

    }

    @ConditionalOnMissingBean(xyz.iipster.security.IbmiMainComponent.class)
    @Import(DefaultIbmiMainComponent.class)
    protected static class IbmiMainComponent {

    }

    @ConditionalOnMissingBean(xyz.iipster.security.HomeView.class)
    @Import(DefaultHomeView.class)
    protected static class HomeView {

    }

    @Bean
    MessageProvider messageProvider() {
        return new ResourceBundleMessageProvider("xyz.iipster.security.messages");
    }
}
