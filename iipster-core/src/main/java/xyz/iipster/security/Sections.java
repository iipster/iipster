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

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

/**
 * Defines the base side bar sections.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
@SideBarSections({
        @SideBarSection(id = Sections.ACTIONS, captionCode = "iipster.sections.actions", order = 1_000),
        @SideBarSection(id = Sections.UTILS, captionCode = "iipster.sections.utils", order = 1_000_000)
})
@Component
public class Sections {
    public static final String ACTIONS = "iipster.actions";
    public static final String UTILS = "iipster.utils";
}
