package xstampp.astpa.util.jobs.statistics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.util.jobs.statistics.AbstractProgressSheetCreator.STEP;

public class ImageProvider {

  private DataModelController controller;

  public ImageProvider(DataModelController controller) {
    this.controller = controller;
  }

  byte[] createTotalBar(int width, int height) {
    return getBytes(width, height, (graphics) -> {
      Rectangle rect = new Rectangle(50, 50, width - 100, height - 100);
      drawHeading(graphics, "Total Progress", width);
      drawHorizontalStepBar(graphics, rect, controller.getProjectId(), EnumSet.of(STEP.TOTAL));
    });
  }

  byte[] createHazardBars(int width, int height, int horizontalMargin) {
    return getBytes(width, height, (graphics) -> {
      drawHeading(graphics, "Hazard Progress", width);
      List<ITableModel> hazards = controller.getAllHazards();

      int rectHeight = hazards.size() == 0 ? 0 : (height - 55) / hazards.size() - horizontalMargin;
      int rectWidth = width - 150;
      int segmY = 50 + horizontalMargin;
      int fontHeight = graphics.getFontMetrics().getHeight();
      for (ITableModel model : hazards) {
        Rectangle rect = new Rectangle(50, segmY, rectWidth, rectHeight);
        int stringX = rect.x - graphics.getFontMetrics().stringWidth(model.getIdString()) - 5;
        int stringY = segmY + rectHeight / 2 + fontHeight / 2;
        graphics.drawString(model.getIdString(), stringX, stringY);
        drawHorizontalStepBar(graphics, rect, model.getId(),
            EnumSet.of(STEP.STEP_0, STEP.STEP_1_HAZARD_CENTERED, STEP.STEP_2_HAZARD_CENTERED));
        segmY += rectHeight + horizontalMargin;
      }

      graphics.setColor(Color.lightGray);

      graphics.fillRect(width - 100, 50 + 4, 15, 15);
      graphics.drawString("Step 0", width - 80, 50 + fontHeight * 1);
      graphics.setColor(Color.GRAY);
      graphics.fillRect(width - 100, 50 + fontHeight + 4, 15, 15);
      graphics.drawString("Step 1", width - 80, 50 + fontHeight * 2);
      graphics.setColor(Color.darkGray);
      graphics.fillRect(width - 100, 50 + fontHeight * 2 + 4, 15, 15);
      graphics.drawString("Step 2", width - 80, 50 + fontHeight * 3);
    });
  }

  byte[] createStepBars(int width, int height) {
    return getBytes(width, height, (graphics) -> {
      Rectangle rect = new Rectangle(50, 50, width - 100, height - 100);
      drawHeading(graphics, "per Step Progress", width);
      graphics.drawPolyline(new int[] { rect.x, rect.x, rect.x + rect.width },
          new int[] { rect.y, rect.y + rect.height, rect.y + rect.height }, 3);
      graphics.drawString("100%", 5, 50);
      graphics.drawLine(rect.x, rect.y, rect.x - 5, rect.y);
      graphics.drawString("  0%", 5, 50 + rect.height);
      graphics.drawLine(rect.x, rect.y + rect.height, rect.x - 5, rect.y + rect.height);
      Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
      graphics.setStroke(dashed);
      for (float factor = 0; factor < 1; factor += 0.25) {
        int lineY = (int) (rect.y + (rect.height * factor));
        graphics.drawLine(rect.x, lineY, rect.x + rect.width, lineY);
      }
      drawBar(graphics, rect, STEP.STEP_0, 1, 30);
      drawBar(graphics, rect, STEP.STEP_1, 2, 30);
      drawBar(graphics, rect, STEP.STEP_2, 3, 30);
    });
  }

  private byte[] getBytes(int width, int height, Consumer<Graphics2D> imageConsumer) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    graphics.setColor(Color.BLACK);
    imageConsumer.accept(graphics);
    try (ByteArrayOutputStream baps = new ByteArrayOutputStream();) {
      ImageIO.write(image, "png", baps);
      return baps.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void drawHorizontalStepBar(Graphics2D graphics, Rectangle rect, UUID entryId, EnumSet<STEP> segments) {
    Color colorBefore = graphics.getColor();
    int barHeight = (int) (rect.height * 0.8);
    int barY = rect.y + (int) (rect.height * 0.1);
    Iterator<Color> colorIter = Arrays.asList(Color.lightGray, Color.GRAY, Color.DARK_GRAY).iterator();
    float progress = 0;
    int barWidth = 0;
    for (STEP step : segments) {
      Float segmentProgress = (AbstractProgressSheetCreator.getProgress(entryId, 0, step)) / (segments.size() * 100);
      graphics.setColor(colorIter.next());
      int segmentWidth = (int) (rect.width * segmentProgress);
      graphics.fillRect(rect.x + barWidth, barY, segmentWidth, barHeight);
      barWidth += segmentWidth;
      progress += segmentProgress;
    }

    String progressString = String.format("%.3f", progress * 100) + "%";
    int stringWidth = graphics.getFontMetrics().stringWidth(progressString);
    int stringY = (int) (barY + barHeight * 0.5 + graphics.getFontMetrics().getHeight() / 2);
    int stringX = rect.x + barWidth;
    if (stringWidth < barWidth) {
      graphics.setColor(Color.WHITE);
      stringX = rect.x;
    }
    graphics.drawString(progressString, stringX, stringY);
    graphics.setColor(Color.BLACK);
    graphics.drawPolyline(new int[] { rect.x, rect.x, rect.x + rect.width },
        new int[] { rect.y, rect.y + rect.height, rect.y + rect.height }, 3);
    graphics.setColor(colorBefore);
  }

  private void drawBar(Graphics2D graphics, Rectangle rect, STEP step, int nr, int barWidth) {
    int stepBarPosition = rect.x + nr * rect.width / 4;
    float progress = (AbstractProgressSheetCreator.getProgress(controller.getProjectId(), 1, step)) / 100;
    int heightOffset = (int) (rect.height * (1 - progress));
    Color color = progress < 0.75 ? Color.yellow : Color.green;
    color = progress < 0.5 ? Color.orange : color;
    color = progress < 0.25 ? Color.red : color;
    Color colorBefore = graphics.getColor();
    graphics.setColor(color);
    Rectangle barBounds = new Rectangle(stepBarPosition - barWidth / 2, rect.y + heightOffset, barWidth,
        rect.height - heightOffset);
    graphics.fillRect(barBounds.x, barBounds.y, barBounds.width, barBounds.height);
    graphics.setColor(colorBefore);
    String progressString = String.format("%.3f", progress * 100) + "%";

    graphics.drawString(progressString, barBounds.x, barBounds.y - 5);
    int fontHeight = graphics.getFontMetrics().getHeight();
    int labelWidth = graphics.getFontMetrics().stringWidth(step.getLabel());
    graphics.drawString(step.getLabel(), stepBarPosition - labelWidth / 2,
        rect.y + rect.height + fontHeight);

  }

  private void drawHeading(Graphics2D graphics, String text, int width) {
    width -= width / 2 + graphics.getFontMetrics().stringWidth(text) / 2;
    Font font = graphics.getFont();
    graphics.setFont(new Font("Arial", Font.BOLD, 12));
    graphics.drawString(text, (int) width, 30);
    graphics.setFont(font);
  }
}
