/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager {

  public static final Color COLOR_GREEN = new Color(null, 10, 200, 10);
  public static final Color COLOR_RED = new Color(null, 200, 10, 10);
  public static final Color COLOR_YELLOW = new Color(null, 255, 200, 10);
  public static final Color COLOR_BLACK = new Color(null, 0, 0, 0);
  public static final Color COLOR_WHITE = new Color(null, 255, 255, 255);
  public static final Color COLOR_grey = new Color(null, 100, 100, 100);

  private static Map<String, Color> fColorTable = new HashMap<>(10);

  public static Color color(String titel) {
    Color color = fColorTable.get(titel);
    if (color == null) {
      return COLOR_BLACK;
    }
    return color;
  }

  public static Color registerColor(String titel, RGB rgb) {
    Color color = fColorTable.get(titel);
    if (color == null) {
      color = new Color(Display.getCurrent(), rgb);
      fColorTable.put(titel, color);
    }
    return color;
  }

  public void dispose() {
    Iterator<Color> e = fColorTable.values().iterator();
    while (e.hasNext()) {
      e.next().dispose();
    }
  }
}
