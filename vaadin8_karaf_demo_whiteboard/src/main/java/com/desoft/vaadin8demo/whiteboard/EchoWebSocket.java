package com.desoft.vaadin8demo.whiteboard;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.osgi.service.component.annotations.Component;

/**
 * pax-web enables the websocket support for the MyUiServlet only when in the bundle a Websocket is available.
 * See also <a href="https://github.com/ops4j/org.ops4j.pax.web/issues/2135">PAX-WEB issue #2135</a>.
 */
@ServerEndpoint(value = "/ws")
@Component(service = Object.class,
        // This property is required to enable the websocket support
        property = "org.ops4j.pax.web.http.whiteboard.websocket=true")
public class EchoWebSocket {

    private static java.util.logging.Logger LOG = Logger.getLogger(EchoWebSocket.class.getName());

    @OnOpen
    public void onWebSocketConnect(Session sess) {
        LOG.info("Socket Connected: " + sess);
    }

    @OnMessage
    public String onWebSocketText(String message) {
        LOG.info("Received TEXT message: " + message);
        return message;
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        LOG.info("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        LOG.log(Level.WARNING, "Socket Error", cause);
    }

}