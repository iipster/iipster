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

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.SideBarUtils;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/**
 * This is the default main component displayed when user is logged in. It is used unless another bean of type
 * ViewDisplay exists.
 *
 * It will display a side bar if there is at least one side bar item to display.
 *
 * It will always display the selected View.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@UIScope
@Component
@SpringViewDisplay
public class DefaultIbmiMainComponent extends IbmiMainComponent implements ViewDisplay {
    private final ValoSideBar valoSideBar;
    private final SideBarUtils sideBarUtils;

    private final HorizontalLayout rootLayout = new HorizontalLayout();
    private final Panel springViewDisplay = new Panel();

    public DefaultIbmiMainComponent(ValoSideBar valoSideBar,
                                    SideBarUtils sideBarUtils) {
        this.valoSideBar = valoSideBar;
        this.sideBarUtils = sideBarUtils;

        setCompositionRoot(rootLayout);
    }

    @Override
    public void attach() {
        super.attach();

        rootLayout.setSizeFull();
        setSizeFull();

        // Add the sideBar only if we have at least one item
        if (sideBarUtils.getSideBarSections(UI.getCurrent().getClass()).stream().anyMatch(i -> {
            return !sideBarUtils.getSideBarItems(i).isEmpty();
        })) {
            rootLayout.addComponent(valoSideBar);
        }
        springViewDisplay.setSizeFull();
        rootLayout.addComponent(springViewDisplay);
        rootLayout.setExpandRatio(springViewDisplay, 1F);
    }

    @Override
    public void showView(View view) {
        springViewDisplay.setContent((com.vaadin.ui.Component) view);
    }
}
