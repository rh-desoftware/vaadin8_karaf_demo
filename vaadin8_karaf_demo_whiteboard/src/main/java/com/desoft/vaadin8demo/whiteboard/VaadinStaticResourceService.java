package com.desoft.vaadin8demo.whiteboard;

import java.io.IOException;
import java.net.URL;

public interface VaadinStaticResourceService {
    URL findResourceURL(String filename) throws IOException;
}
