package com.desoft.vaadin8demo.war;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(urlPatterns = {"/*", "/VAADIN/*"}, name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
public class MyUIServlet extends VaadinServlet {


}
