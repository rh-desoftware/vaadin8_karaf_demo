package com.desoft.vaadin8demo.whiteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.vaadin.osgi.resources.OsgiVaadinContributor;
import com.vaadin.osgi.resources.OsgiVaadinResource;
import com.vaadin.osgi.resources.OsgiVaadinTheme;
import com.vaadin.osgi.resources.OsgiVaadinWidgetset;

@Component(service = VaadinStaticResourceService.class)
public class VaadinStaticResourceServiceImpl implements VaadinStaticResourceService {

    private static Logger m_Log = Logger.getLogger("VaadinStaticResourceServiceImpl");

    private final Map<Pattern, Object> m_PatterToContributor = new HashMap<>();

    @Override
    public URL findResourceURL(final String filename) {

        URL ret = null;
        synchronized (m_PatterToContributor) {

            for (Map.Entry<Pattern, Object> entry : m_PatterToContributor.entrySet()) {
                if (entry.getKey().matcher(filename).matches()) {
                    ret = entry.getValue().getClass().getClassLoader().getResource(filename);
                    if (ret != null) {
                        break;
                    }
                }
            }

        }
        if (ret == null) {
            m_Log.warning(() -> "resource " + filename + " not found");
        }
        return ret;
    }


    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            service = OsgiVaadinContributor.class,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeContributor"
    )
    public void addContributor(final OsgiVaadinContributor contributor) {

        synchronized (m_PatterToContributor) {
            for (OsgiVaadinResource resource : contributor.getContributions()) {
                Pattern pattern = toPattern("", resource.getName());
                m_Log.info(() -> "Contributor " + contributor + " brings resource " + pattern.pattern());
                m_PatterToContributor.put(pattern, contributor);
            }
        }
    }

    public void removeContributor(final OsgiVaadinContributor contributor) {
        removePatterns(contributor);
    }

    private void removePatterns(final Object obj) {
        List<String> removedPatterns = new ArrayList<>();
        synchronized (m_PatterToContributor) {
            Iterator<Map.Entry<Pattern, Object>> iter = m_PatterToContributor.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Pattern, Object> entry = iter.next();
                if (entry.getValue().equals(obj)) {
                    removedPatterns.add(entry.getKey().pattern());
                    iter.remove();
                    break;
                }
            }
        }
        m_Log.info(() -> "Removed patterns for " + obj + ": " + removedPatterns);
    }


    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            service = OsgiVaadinTheme.class,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeTheme"
    )
    public void addTheme(final OsgiVaadinTheme theme) {
        Pattern pattern = toPattern("themes/", theme.getName() + "*");
        synchronized (m_PatterToContributor) {
            m_PatterToContributor.put(pattern, theme);
        }
    }

    public void removeTheme(final OsgiVaadinTheme theme) {
        removePatterns(theme);
    }

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            service = OsgiVaadinWidgetset.class,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeWidgetset"
    )
    public void addWidgetset(final OsgiVaadinWidgetset widgetset) {
        Pattern pattern = toPattern("widgetsets/", widgetset.getName() + "*");
        synchronized (m_PatterToContributor) {
            m_PatterToContributor.put(pattern, widgetset);
        }
    }

    public void removeWidgetset(final OsgiVaadinWidgetset widgetset) {
        removePatterns(widgetset);
    }

    private static Pattern toPattern(final String prefix, final String name) {
        StringBuilder buf = new StringBuilder();
        buf.append("^/VAADIN/");
        buf.append(prefix);
        buf.append(globToReqExp(name));
        buf.append("$");
        return Pattern.compile(buf.toString());
    }

    private static String globToReqExp(final String str) {
        StringBuilder ret = new StringBuilder();

        StringBuilder currentPattern = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '*') {
                if (!currentPattern.isEmpty()) {
                    ret.append(Pattern.quote(currentPattern.toString()));
                    currentPattern.setLength(0);
                }
                ret.append(".*");
            } else {
                currentPattern.append(c);
            }
        }
        if (!currentPattern.isEmpty()) {
            ret.append(Pattern.quote(currentPattern.toString()));
            currentPattern.setLength(0);
        }
        return ret.toString();
    }

}
