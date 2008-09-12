package com.explodingpixels.widgets;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WindowUtils {

    public static WindowFocusListener createAndInstallRepaintWindowFocusListener(
            Window window) {

        // create a WindowFocusListener that repaints the window on focus
        // changes.
        WindowFocusListener windowFocusListener = new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                e.getWindow().repaint();
            }
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().repaint();
            }
        };

        window.addWindowFocusListener(windowFocusListener);

        return windowFocusListener;
    }

    public static boolean isParentWindowFocused(Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);
        return window != null && window.isFocused();
    }

    public static void installWindowFocusListener(
            WindowFocusListener focusListener, JComponent component) {
        // TODO add null argument checks.
        component.addPropertyChangeListener("Frame.active",
                createFrameFocusPropertyChangeListener(focusListener, component));
    }

    private static PropertyChangeListener createFrameFocusPropertyChangeListener(
            final WindowFocusListener focusListener, final JComponent component) {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Window window = SwingUtilities.getWindowAncestor(component);
                // use the client property that initiated this this
                // property change event, as the actual window's
                // isFocused method may not return the correct value
                // because the window is in transition.
                boolean hasFocus = (Boolean) component.getClientProperty("Frame.active");
                if (hasFocus) {
                    focusListener.windowGainedFocus(
                            new WindowEvent(window, WindowEvent.WINDOW_GAINED_FOCUS));
                } else {
                    focusListener.windowLostFocus(
                            new WindowEvent(window, WindowEvent.WINDOW_LOST_FOCUS));
                }
            }
        };
    }

}