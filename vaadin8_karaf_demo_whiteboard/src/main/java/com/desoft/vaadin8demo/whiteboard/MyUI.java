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

    /** Updates the time every second */
    private ScheduledExecutorService m_Exe;

    /** show the current time (just to ensure that the websocket is working) */
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

    /**
     * Sets the {@link ScheduledExecutorService} for managing scheduled tasks in the UI.
     * If a previously set executor exists, it will be shut down before setting the new one.
     *
     * @param exe the {@link ScheduledExecutorService} instance to be set. Can be null to clear the executor.
     * @return the supplied {@link ScheduledExecutorService} instance.
     */
    private ScheduledExecutorService setExecutor(final ScheduledExecutorService exe) {
        ScheduledExecutorService oldExe = m_Exe;
        if (oldExe != null) {
            oldExe.shutdown();
        }
        m_Exe = exe;
        return exe;
    }

    /**
     * Updates the {@code m_TimeLabel} with the current date and time.
     * <p>
     * This method is invoked periodically by a scheduled executor to refresh
     * the time displayed in the UI. The time is formatted as "yyyy-MM-dd HH:mm:ss".
     * <p>
     * Uses {@code access()} to ensure that the update is safely executed
     * within the UI thread, maintaining thread safety for UI components.
     */
    private void tick() {
        access(() -> m_TimeLabel.setValue(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @Override
    public void attach() {
        super.attach();
        setExecutor(Executors.newSingleThreadScheduledExecutor()).scheduleWithFixedDelay(this::tick, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
    }


    @Override
    public void detach() {

        super.detach();
        setExecutor(null);
    }
}
