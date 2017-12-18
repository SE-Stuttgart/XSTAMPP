package xstampp.astpa.wizards;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

import xstampp.util.ColorManager;

public class StatisticsView extends ViewPart {

  private Workbook workbook;
  private TabFolder folder;
  private Map<Short, Color> colorMap;
  private CellRangeAddress activeRegion;

  public StatisticsView() {
    this.colorMap = new HashMap<>();
  }

  @Override
  public void createPartControl(Composite parent) {
    folder = new TabFolder(parent, SWT.BOTTOM);
    folder.setLayout(new FillLayout());

  }

  @Override
  public void setFocus() {
  }

  Workbook getWorkbook() {
    return workbook;
  }

  void setWorkbook(Workbook workbook) {
    for (TabItem tabItem : folder.getItems()) {
      tabItem.dispose();
    }
    this.colorMap.put(IndexedColors.WHITE.getIndex(), ColorManager.COLOR_WHITE);
    this.colorMap.put(IndexedColors.GREY_25_PERCENT.getIndex(), ColorManager.registerColor(new RGB(0f, 0f, 0.8f)));
    this.colorMap.put(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex(),
        ColorManager.registerColor(new RGB(240f, 0.5f, 0.8f)));
    workbook.sheetIterator().forEachRemaining((sheet) -> {
      TabItem tab = new TabItem(folder, SWT.None);
      tab.setText(sheet.getSheetName());
      ScrolledComposite scrollContent = new ScrolledComposite(folder, SWT.H_SCROLL | SWT.V_SCROLL);
      final Composite sheetComposite = new Composite(scrollContent, 0);
      scrollContent.setExpandVertical(true);
      scrollContent.setExpandHorizontal(true);
      Drawing<?> drawing = sheet.getDrawingPatriarch();
      Point size;
      if (drawing != null) {
        size = createDrawings(sheet, sheetComposite, drawing);
      } else {
        size = createTable(sheet, sheetComposite);
      }
      scrollContent.setContent(sheetComposite);
      scrollContent.layout();
      scrollContent.layout(new Control[] { sheetComposite });
      scrollContent.setMinHeight(size.y);
      scrollContent.setMinWidth(size.x);
      tab.setControl(scrollContent);
    });
  }

  private Point createDrawings(Sheet sheet, final Composite sheetComposite, Drawing<?> drawing) {
    Rectangle rect = new Rectangle(0, 0, 0, 0);
    drawing.forEach((obj) -> {
      XSSFPicture pict = (XSSFPicture) obj;
      byte[] data = pict.getPictureData().getData();
      ImageLoader imageLoader = new ImageLoader();
      ByteArrayInputStream stream = new ByteArrayInputStream(data);
      XSSFClientAnchor clientAnchor = pict.getClientAnchor();
      int x = 0;
      for (int i = 0; i < clientAnchor.getCol1(); i++) {
        x += sheet.getColumnWidthInPixels(i);
      }
      int y = 0;
      for (int i = 0; i < clientAnchor.getRow1(); i++) {
        if (!sheet.getRowSumsBelow()) {
          x += sheet.getRow(i).getHeight();
        }
      }
      final Point pos = new Point(x, y);
      ImageData imgData = imageLoader.load(stream)[0];
      Image img = new Image(null, imgData);
      sheetComposite.addPaintListener(e -> {
        e.gc.drawImage(img, pos.x, pos.y);

      });
      rect.add(new Rectangle(pos.x, pos.y, img.getBounds().width, img.getBounds().height));
    });
    return new Point(rect.width, rect.height);
  }

  private Point createTable(Sheet sheet, final Composite sheetComposite) {
    final Map<CellAddress, CellRangeAddress> mergeRegionsMap = new HashMap<>();
    for (CellRangeAddress range : sheet.getMergedRegions()) {
      mergeRegionsMap.put(new CellAddress(range.getFirstRow(), range.getFirstColumn()), range);
    }
    int firstRowNum = sheet.getFirstRowNum();
    int lastRowNum = sheet.getLastRowNum();
    GridLayout layout = new GridLayout(sheet.getRow(firstRowNum).getPhysicalNumberOfCells(), false);
    layout.horizontalSpacing = 0;
    layout.verticalSpacing = 1;
    sheetComposite.setLayout(layout);
    activeRegion = null;
    final Point size = new Point(0, 0);
    for (int i = firstRowNum; i <= lastRowNum; i++) {
      final Row row = sheet.getRow(i);
      size.y += row.getHeight();
      row.cellIterator().forEachRemaining((cell) -> {
        boolean isMergeCell = mergeRegionsMap.containsKey(cell.getAddress());
        boolean inRange = sheet.getMergedRegions().stream().anyMatch((range) -> {
          return range.isInRange(cell);
        });
        if (isMergeCell || !inRange) {
          Label cellComp = new Label(sheetComposite, SWT.BORDER | SWT.WRAP);
          GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
          layoutData.minimumWidth = 50;
          layoutData.widthHint = sheet.getColumnWidth(cell.getAddress().getColumn());

          if (isMergeCell) {
            activeRegion = mergeRegionsMap.get(cell.getAddress());
            layoutData.horizontalSpan = (activeRegion.getLastColumn() - activeRegion.getFirstColumn()) + 1;
            layoutData.verticalSpan = (activeRegion.getLastRow() - activeRegion.getFirstRow()) + 1;
          }
          cellComp.setLayoutData(layoutData);
          cellComp.setText(cell.getStringCellValue());
          cellComp.setBackground(this.colorMap.get(cell.getCellStyle().getFillForegroundColor()));
        }
      });
    }
    Point point = sheetComposite.computeSize(size.x, SWT.DEFAULT);
    size.y = point.y;
    sheetComposite.layout(true, true);
    return size;
  }

}
