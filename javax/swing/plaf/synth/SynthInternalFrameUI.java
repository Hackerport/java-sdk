/*
 * @(#)SynthInternalFrameUI.java	1.23 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.swing.plaf.synth;

import java.awt.*;
import java.awt.event.*;
import java.awt.peer.LightweightPeer;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.event.*;

import java.beans.*;
import java.io.Serializable;
import sun.swing.plaf.synth.SynthUI;


/**
 * Synth's InternalFrameUI.
 *
 * @version 1.23 03/23/10
 * @author David Kloba
 * @author Joshua Outwater
 * @author Rich Schiavi
 */
class SynthInternalFrameUI extends BasicInternalFrameUI implements SynthUI,
        PropertyChangeListener {
    private SynthStyle style;
 
    private static DesktopManager sharedDesktopManager;
    private boolean componentListenerAdded = false;

    private Rectangle parentBounds;

    public static ComponentUI createUI(JComponent b) {
        return new SynthInternalFrameUI((JInternalFrame)b);
    }

    public SynthInternalFrameUI(JInternalFrame b) {
        super(b);
    }

    public void installDefaults() {
        frame.setLayout(internalFrameLayout = createLayoutManager());
        updateStyle(frame);
    }

    protected void installListeners() {
        super.installListeners();
        frame.addPropertyChangeListener(this);
    }

    protected void uninstallComponents() {
	if (frame.getComponentPopupMenu() instanceof UIResource) {
	    frame.setComponentPopupMenu(null);
	}
	super.uninstallComponents();
    }

    protected void uninstallListeners() {
        frame.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    private void updateStyle(JComponent c) {
        SynthContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = SynthLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            Icon frameIcon = frame.getFrameIcon();
            if (frameIcon == null || frameIcon instanceof UIResource) {
                frame.setFrameIcon(context.getStyle().getIcon(
                                   context, "InternalFrame.icon"));
            }
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    protected void uninstallDefaults() {
        SynthContext context = getContext(frame, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;
        if(frame.getLayout() == internalFrameLayout) {
            frame.setLayout(null);
        }

    }

    public SynthContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SynthContext getContext(JComponent c, int state) {
        return SynthContext.getContext(SynthContext.class, c,
                            SynthLookAndFeel.getRegion(c), style, state);
    }

    private Region getRegion(JComponent c) {
        return SynthLookAndFeel.getRegion(c);
    }

    public int getComponentState(JComponent c) {
        return SynthLookAndFeel.getComponentState(c);
    }

    protected JComponent createNorthPane(JInternalFrame w) {
        titlePane = new SynthInternalFrameTitlePane(w);
	titlePane.setName("InternalFrame.northPane");
        return titlePane;
    }
    
    protected ComponentListener createComponentListener() {
	if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
	    return new ComponentHandler() {
		public void componentResized(ComponentEvent e) {
		    if (frame != null && frame.isMaximum()) {
			JDesktopPane desktop = (JDesktopPane)e.getSource();
			for (Component comp : desktop.getComponents()) {
			    if (comp instanceof SynthDesktopPaneUI.TaskBar) {
				frame.setBounds(0, 0,
						desktop.getWidth(),
						desktop.getHeight() - comp.getHeight());
				frame.revalidate();
				break;
			    }
			}
		    }

		    // Update the new parent bounds for next resize, but don't
		    // let the super method touch this frame
		    JInternalFrame f = frame;
		    frame = null;
		    super.componentResized(e);
		    frame = f;
		}
	    };
	} else {
	    return super.createComponentListener();
	}
    }

    public void update(Graphics g, JComponent c) {
        SynthContext context = getContext(c);

        SynthLookAndFeel.update(context, g);
        context.getPainter().paintInternalFrameBackground(context,
                          g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SynthContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SynthContext context, Graphics g) {
    }

    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        context.getPainter().paintInternalFrameBorder(context,
                                                            g, x, y, w, h);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        SynthStyle oldStyle = style;
        JInternalFrame f = (JInternalFrame)evt.getSource();
        String prop = evt.getPropertyName();

        if (SynthLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle(f);
        }

        if (style == oldStyle &&
            (prop == JInternalFrame.IS_MAXIMUM_PROPERTY ||
             prop == JInternalFrame.IS_SELECTED_PROPERTY)) {
            // Border (and other defaults) may need to change
            SynthContext context = getContext(f, ENABLED);
            style.uninstallDefaults(context);
            style.installDefaults(context, this);
        }
    }
}
