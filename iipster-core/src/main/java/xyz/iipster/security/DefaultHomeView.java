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

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * This is the default home view. It will be used if no bean of type HomeView exists.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@SpringView(name = "")
@SideBarItem(sectionId = Sections.ACTIONS, captionCode = "iipster.home.label")
@FontAwesomeIcon(FontAwesome.HOME)
public class DefaultHomeView extends VerticalLayout implements HomeView {
    private final I18N i18N;

    public DefaultHomeView(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    public void attach() {
        super.attach();

        setSizeFull();
        setMargin(true);

        final Label label = new Label(i18N.get("iipster.defaultHome.text"));
        label.setSizeUndefined();
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
