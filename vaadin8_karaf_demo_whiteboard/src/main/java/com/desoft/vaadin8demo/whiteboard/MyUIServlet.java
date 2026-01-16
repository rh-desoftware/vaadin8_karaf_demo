package com.desoft.vaadin8demo.whiteboard;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@Component(service = Servlet.class)
@WebServlet(urlPatterns = {"/*", "/VAADIN/*"}, name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
public class MyUIServlet extends VaadinServlet {

    private static Logger m_Log = Logger.getLogger("MyUIServlet");

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private VaadinStaticResourceService m_ResourceService;

    @Override
    public URL findResourceURL(final String filename) throws IOException {
        URL ret = super.findResourceURL(filename);
        if (ret == null) {
            VaadinStaticResourceService resService = m_ResourceService;
            if (resService != null) {
                ret = resService.findResourceURL(filename);
            }
            if (ret == null) {
                m_Log.warning(() -> "resource " + filename + " not found");
            }
        }
        return ret;
    }


}
