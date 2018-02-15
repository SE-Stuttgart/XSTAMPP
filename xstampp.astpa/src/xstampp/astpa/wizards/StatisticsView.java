package xstampp.astpa.wizards;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import xstampp.util.ColorManager;

public class StatisticsView extends ViewPart {

  private Workbook workbook;
  private TabFolder folder;
  private Map<Short, Color> colorMap;
  private Listener redrawListener;

  public StatisticsView() {
    this.colorMap = new HashMap<>();
  }

  @Override
  public void createPartControl(Composite parent) {
    folder = new TabFolder(parent, SWT.BOTTOM);
    folder.setLayout(new FillLayout());
    redrawListener = (event) -> {
      if (workbook != null) {
        loadWorkbook();
      }
    };
  }

  @Override
  public void setFocus() {
  }

  Workbook getWorkbook() {
    return workbook;
  }

  void setWorkbook(Workbook workbook) {
    this.workbook = workbook;
    PlatformUI.getWorkbench().getDisplay().asyncExec(() -> redrawListener.handleEvent(null));
  }

  private void loadWorkbook() {
    folder.setVisible(false);
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
      if (drawing != null) {
        createDrawings(sheet, sheetComposite, drawing);
      } else {
        provideGrid(sheet, sheetComposite);
      }
      scrollContent.setContent(sheetComposite);
      tab.setControl(scrollContent);
    });

    folder.setVisible(true);
  }

  private Point provideGrid(Sheet sheet, Composite shell) {
    shell.setLayout(new FillLayout());
    Grid grid = new Grid(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    grid.setHeaderVisible(true);
    final Map<Row, CellRangeAddress> mergeRegionsMap = new HashMap<>();
    for (CellRangeAddress range : sheet.getMergedRegions()) {
      mergeRegionsMap.put(sheet.getRow(range.getFirstRow()), range);
    }
    int firstRowNum = sheet.getFirstRowNum();
    int lastRowNum = sheet.getLastRowNum();
    int nrOfCols = sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells();
    Row row = sheet.getRow(firstRowNum);
    for (int i = 0; i < nrOfCols; i++) {
      GridColumn column = new GridColumn(grid, SWT.NONE);
      column.setResizeable(true);
      Cell cell = row.getCell(i);
      if (cell != null) {
        column.setText(cell.getStringCellValue());
      }
      // GridCellRenderer cellRenderer = column.getCellRenderer();
      // column.setCellRenderer(new MergeCellsRenderer(cellRenderer));
    }
    Stack<CellRangeAddress> ranges = new Stack<>();
    GridItem parent = null;

    for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
      row = sheet.getRow(i);
      if (parent != null && !ranges.peek().containsRow(i)) {
        ranges.pop();
        parent = parent.getParentItem();
      }
      GridItem item = new GridItem(grid, SWT.NONE);
      if (mergeRegionsMap.containsKey(row)) {
        parent = item;
        ranges.push(mergeRegionsMap.get(row));
      }
      for (int colNr = 0; colNr < nrOfCols; colNr++) {
        Cell cell = row.getCell(colNr);

        if (cell != null) {
          item.setBackground(colNr, this.colorMap.get(cell.getCellStyle().getFillForegroundColor()));
          item.setText(colNr, cell.getStringCellValue());

        }
      }
    }
    shell.addControlListener(new ControlAdapter() {

      @Override
      public void controlResized(ControlEvent e) {
        for (GridColumn gridColumn : grid.getColumns()) {
          gridColumn.setWidth(shell.getBounds().width / nrOfCols);
        }
      }
    });

    return grid.getSize();

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

}
