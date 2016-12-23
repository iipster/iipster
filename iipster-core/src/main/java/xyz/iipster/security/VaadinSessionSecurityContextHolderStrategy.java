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

import com.vaadin.server.VaadinSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * Spring Security context holder strategy that holds the security context in the Vaadin session.
 *
 * @author df@bigbluebox.ca
 * @since 0.0.1
 */
public class VaadinSessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    /**
     * @return the current VaadinSession.
     * @throws IllegalStateException if there is no VaadinSession.
     */
    private static VaadinSession getSession() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            throw new IllegalStateException("No VaadinSession bound to current thread");
        }
        return session;
    }

    @Override
    public void clearContext() {
        getSession().setAttribute(SecurityContext.class, null);
    }

    @Override
    public SecurityContext getContext() {
        VaadinSession session = getSession();
        SecurityContext context = session.getAttribute(SecurityContext.class);
        if (context == null) {
            context = createEmptyContext();
            session.setAttribute(SecurityContext.class, context);
        }
        return context;
    }

    @Override
    public void setContext(SecurityContext context) {
        getSession().setAttribute(SecurityContext.class, context);
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
