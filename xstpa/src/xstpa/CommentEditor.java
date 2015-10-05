package xstpa;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;


//CURRENTLY NOT USED
public class CommentEditor extends EditingSupport {

	  private final TableViewer viewer;
	  private final CellEditor editor;

	  public CommentEditor(TableViewer viewer) {
	    super(viewer);
	    this.viewer = viewer;
	    this.editor = new TextCellEditor(viewer.getTable());
	  }

	  @Override
	  protected CellEditor getCellEditor(Object element) {
	    return editor;
	  }

	  @Override
	  protected boolean canEdit(Object element) {
	    return true;
	  }

	  @Override
	  protected Object getValue(Object element) {
	    return (String.valueOf(element));
	  }

	  @Override
	  protected void setValue(Object element, Object userInputValue) {
	    element=String.valueOf(userInputValue);
	    viewer.update(element, null);
	  }
	} 