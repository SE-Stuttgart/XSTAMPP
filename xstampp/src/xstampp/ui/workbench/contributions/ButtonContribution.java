/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.ui.workbench.contributions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ButtonContribution extends ControlContribution {

  private int style;
  private int size;
  private Image image;
  private List<SelectionListener> selectionListeners;
  private String text;
  private String toolTip;
  private Listener listener;
  private boolean enabled;
  private boolean selection;

  /**
   * {@link ButtonContribution#ButtonContribution(String, int, int)}.
   */
  public ButtonContribution(String id) {
    this(id, SWT.PUSH);
  }

  /**
   * {@link ButtonContribution#ButtonContribution(String, int, int)}
   */
  public ButtonContribution(String id, int style) {
    this(id, style, 30);
  }

  /**
   * 
   * @param id
   *          {@link #setId(String)}
   * @param style
   *          the style constant for the button
   * @param size
   *          the width of the contribution
   * 
   * @see Button
   */
  public ButtonContribution(String id, int style, int size) {
    super(id);
    this.style = style;
    this.size = size;
    this.selectionListeners = new ArrayList<>();
    this.enabled = true;
    this.selection = false;
    this.listener = new Listener() {

      @Override
      public void handleEvent(Event event) {
      }
    };

  }

  @Override
  protected Control createControl(Composite parent) {
    final Button button = new Button(parent, this.style);
    for (SelectionListener selectionListener : selectionListeners) {
      button.addSelectionListener(selectionListener);
    }
    this.listener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        if (image != null) {
          button.setImage(image);
        }
        if (text != null) {
          button.setText(text);
        }
        if (toolTip != null) {
          button.setToolTipText(toolTip);
        }
        button.setEnabled(enabled);
        button.setSelection(selection);
      }
    };
    listener.handleEvent(null);
    return button;
  }

  @Override
  public int computeWidth(Control control) {
    return size;
  }

  public void setImage(Image image) {
    this.image = image;
    this.listener.handleEvent(null);
  }

  public void addSelectionListener(SelectionListener listener) {
    this.selectionListeners.add(listener);
  }

  public void setText(String text) {
    this.text = text;
    this.listener.handleEvent(null);
  }

  public void setToolTipText(String toolTip) {
    this.toolTip = toolTip;
    this.listener.handleEvent(null);
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    this.listener.handleEvent(null);
  }

  public void setSelection(boolean selection) {
    this.selection = selection;
  }

  public boolean getSelection() {
    return selection;
  }
}
