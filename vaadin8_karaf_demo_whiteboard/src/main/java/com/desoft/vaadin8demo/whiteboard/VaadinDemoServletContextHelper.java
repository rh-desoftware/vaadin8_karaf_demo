package com.desoft.vaadin8demo.whiteboard;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardContext;

@Component(service = ServletContextHelper.class)
@HttpWhiteboardContext(name = "Vaadin8DemoWhiteboard", path = "/vaadin8demo/whiteboard")
public class VaadinDemoServletContextHelper extends ServletContextHelper {
}
