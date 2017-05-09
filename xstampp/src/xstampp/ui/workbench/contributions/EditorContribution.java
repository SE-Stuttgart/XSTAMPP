/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.workbench.contributions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import messages.Messages;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import xstampp.Activator;

/**
 * This toolbar Contribution provides tools for a graphical editor Editors can interact with this
 * contribution by implementing IZoomContributior
 * <p/>
 * The provided decoration switch can be used by using {@link IZoomContributor#IS_DECORATED}
 * 
 * @author Lukas Balzer
 * @since 2.0.0
 */
public class EditorContribution extends WorkbenchWindowControlContribution
    implements ZoomListener, PropertyChangeListener, IPartListener {

  private static final int STANDART_ZOOM = 100;
  private static final String[] ZOOM_LEVEL = new String[] { "25", "50", "75", "100", "150", "200",
      "250" };
  private static final Point ZOOM_SLIDER_RANGE = new Point(10, 300);
  private ComboContribiution zoomLabel;
  private ZoomManager zoomManager;
  private SliderContribution zoomSlider;
  private ButtonContribution decoButton;
  private ButtonContribution zoomInButton;
  private ButtonContribution zoomOutButton;
  private IZoomContributor contributor;
  /**
   * the value of isDecorated is used to store the content of the
   * {@link IZoomContributor#IS_DECORATED} property.
   */
  private boolean isDecorated;

  @Override
  protected Control createControl(Composite parent) {
    parent.getParent().setRedraw(true);
    this.isDecorated = false;
    this.contributor = new EmptyZoomContributor();

    this.decoButton = new ButtonContribution("decoButton", SWT.TOGGLE);
    decoButton.setToolTipText(Messages.DecorationToolTip);
    decoButton.setImage(Activator.getImage("icons/buttons/DecoButton.png")); //$NON-NLS-1$
    decoButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent arg0) {
        EditorContribution.this.contributor.fireToolPropertyChange(
            IZoomContributor.IS_DECORATED, !EditorContribution.this.isDecorated);
        setDecoSelection((boolean) contributor.getProperty(IZoomContributor.IS_DECORATED));
      }
    });
    // adding a zoomslider to the toolbar

    final SelectionAdapter adapter = new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (e.getSource() instanceof Slider) {
          int zoom = ((Slider) e.getSource()).getSelection();
          if (Math.abs(zoom - STANDART_ZOOM) < 9) {
            EditorContribution.this.zoomSlider.getSliderControl().setSelection(STANDART_ZOOM);
            zoom = STANDART_ZOOM;
          }
          EditorContribution.this.updateLabel();
          if (EditorContribution.this.zoomManager != null) {
            EditorContribution.this.zoomManager.setZoom(((double) zoom) / 100);
          }
        }
      }
    };

    this.zoomSlider = new SliderContribution("zoomSlider", SWT.HORIZONTAL, 200) {
      @Override
      protected Control createControl(Composite parent) {
        Control control = super.createControl(parent);
        getSliderControl().setMinimum(ZOOM_SLIDER_RANGE.x);
        getSliderControl().setMaximum(ZOOM_SLIDER_RANGE.y);
        getSliderControl().setIncrement(10);
        getSliderControl().setDragDetect(true);
        getSliderControl().setSelection(STANDART_ZOOM);
        getSliderControl().addSelectionListener(adapter);
        return control;
      }
    };

    this.zoomOutButton = new ButtonContribution("zoomOutButton", SWT.PUSH);
    this.zoomOutButton.setImage(Activator.getImage("/icons/buttons/minus.png")); //$NON-NLS-1$
    this.zoomOutButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent arg0) {
        int zoom = EditorContribution.this.zoomSlider.getSliderControl().getSelection()
            - EditorContribution.this.zoomSlider.getSliderControl().getIncrement();
        EditorContribution.this.zoomSlider.getSliderControl().setSelection(zoom);
        EditorContribution.this.updateLabel();
        if (EditorContribution.this.zoomManager != null) {
          EditorContribution.this.zoomManager.setZoom(((double) zoom) / 100);
        }
      }
    });

    this.zoomLabel = new ComboContribiution("zoomCombo", SWT.DROP_DOWN | SWT.NONE, 100) {
      @Override
      protected Control createControl(Composite parent) {
        super.createControl(parent);

        getComboControl().setItems(ZOOM_LEVEL);
        EditorContribution.this.updateLabel();
        getComboControl().addSelectionListener(new SelectionAdapter() {

          @Override
          public void widgetSelected(SelectionEvent arg0) {
            String selection = EditorContribution.this.zoomLabel.getComboControl().getText()
                .replace('%', ' ');
            try {
              int zoom = Integer.parseInt(selection.trim());
              EditorContribution.this.zoomSlider.getSliderControl().setSelection(zoom);
              EditorContribution.this.updateLabel();
              if (EditorContribution.this.zoomManager != null) {
                EditorContribution.this.zoomManager.setZoom(((double) zoom) / 100);
              }
            } catch (NumberFormatException e) {
            }

          }
        });
        return getComboControl();
      }
    };

    this.zoomInButton = new ButtonContribution("zoomInButton", SWT.PUSH);
    this.zoomInButton.setImage(Activator.getImage("/icons/buttons/plus.png")); //$NON-NLS-1$
    this.zoomInButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent arg0) {
        int zoom = EditorContribution.this.zoomSlider.getSliderControl().getSelection()
            + EditorContribution.this.zoomSlider.getSliderControl().getIncrement();
        EditorContribution.this.zoomSlider.getSliderControl().setSelection(zoom);
        EditorContribution.this.updateLabel();
        if (EditorContribution.this.zoomManager != null) {
          EditorContribution.this.zoomManager.setZoom(((double) zoom) / 100);
        }
      }
    });

    try {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);
    } catch (NullPointerException e) {
      // if there is a nullpointer one one cant do anything
    }

    parent.getParent().setRedraw(true);
    ToolBarManager manager = new ToolBarManager(SWT.HORIZONTAL | SWT.FLAT);
    manager.add(decoButton);
    manager.add(zoomSlider);
    manager.add(zoomOutButton);
    manager.add(zoomInButton);
    manager.add(zoomLabel);
    return manager.createControl(parent);
  }

  @Override
  public void zoomChanged(double zoom) {
    this.zoomSlider.getSliderControl().setSelection((int) (zoom * 100));
    this.updateLabel();
  }

  private void updateLabel() {
    this.zoomLabel.getComboControl()
        .setText(this.zoomSlider.getSliderControl().getSelection() + "%"); //$NON-NLS-1$
  }

  @Override
  public boolean isDynamic() {
    return true;
  }

  /**
   * enables all widgets contained in the main composite of this class
   *
   * @author Lukas Balzer
   *
   * @param enabled
   *          if components should be enabled
   */
  private void setEnabled(boolean enabled) {
    try {
      this.zoomLabel.setEnabled(enabled);
      this.zoomSlider.setEnabled(enabled);
      this.setDecoSelection((boolean) this.contributor.getProperty(IZoomContributor.IS_DECORATED));
      this.decoButton.setEnabled(enabled);
      this.zoomInButton.setEnabled(enabled);
      this.zoomOutButton.setEnabled(enabled);
    } catch (Exception e) {
      return;
    }
  }

  private void setDecoSelection(boolean enabled) {
    this.isDecorated = enabled;
    if (enabled) {
      this.decoButton
          .setImage(Activator.getImage("icons/buttons/DecoButton_Selected.png")); //$NON-NLS-1$
    } else {
      this.decoButton
          .setImage(Activator.getImage("icons/buttons/DecoButton.png")); //$NON-NLS-1$
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent arg0) {
    if (arg0.getPropertyName().equals(IZoomContributor.IS_DECORATED)) {
      this.setDecoSelection((boolean) arg0.getNewValue());
    }
  }

  @Override
  public void partActivated(IWorkbenchPart part) {
    if (part instanceof IZoomContributor) {
      this.contributor = ((IZoomContributor) part);
      this.contributor.addPropertyListener(this);
      this.zoomManager = this.contributor.getZoomManager();

      this.zoomManager.addZoomListener(EditorContribution.this);
      this.zoomSlider.getSliderControl()
          .setSelection((int) (EditorContribution.this.zoomManager.getZoom() * 100));
      this.zoomSlider.getSliderControl().notifyListeners(SWT.Selection, null);
    } else {

      if (!(this.contributor instanceof EmptyZoomContributor)) {
        this.zoomManager.removeZoomListener(EditorContribution.this);
        this.contributor.removePropertyListener(this);
        this.contributor = new EmptyZoomContributor();
      }
    }
  }

  @Override
  public void dispose() {
    try {
      if (this.contributor != null) {
        this.contributor.removePropertyListener(this);
      }
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
    } catch (NullPointerException e) {
      // if there is a nullpointer one can't delete anything
    }
    super.dispose();
  }

  @Override
  public void partOpened(IWorkbenchPart part) {
    // TODO Auto-generated method stub

  }

  @Override
  public void partDeactivated(IWorkbenchPart part) {
    if (this.contributor != null) {
      this.contributor.removePropertyListener(this);
    }
  }

  @Override
  public void partClosed(IWorkbenchPart part) {
    if (this.contributor != null) {
      this.contributor.removePropertyListener(this);
    }
  }

  @Override
  public void partBroughtToTop(IWorkbenchPart part) {
    // do nothing
  }

}
