package com.desoft.vaadin8demo.whiteboard;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Push(transport = Transport.WEBSOCKET_XHR)
public class MyUI extends UI {


    private ScheduledExecutorService m_Exe;

    private Label m_TimeLabel;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        m_TimeLabel = new Label();
        layout.addComponent(m_TimeLabel);

        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener(e -> layout.addComponent(new Label("Thanks " + name.getValue()
                + ", it works!")));

        layout.addComponents(name, button);

        setContent(layout);
    }

    @Override
    public void attach() {
        super.attach();
        ScheduledExecutorService oldExe = m_Exe;
        if (oldExe != null) {
            oldExe.shutdown();
        }
        m_Exe = Executors.newSingleThreadScheduledExecutor();
        m_Exe.scheduleWithFixedDelay(this::tick, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    private void tick() {

        access(() -> {
            m_TimeLabel.setValue(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        });
    }

    @Override
    public void detach() {
        super.detach();
    }
}
