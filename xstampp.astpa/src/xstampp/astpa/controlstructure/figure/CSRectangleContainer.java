package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.utilities.CSTextLabel;

public class CSRectangleContainer extends RectangleFigure implements
		IControlStructureFigure {
	
	private UUID id;

	public CSRectangleContainer(UUID id) {
		super();
		this.id = id;
		setSize(10, 10);
		setLayoutManager(new XYLayout());
	}
	
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension dim = super.getPreferredSize(wHint,hHint);
		if(dim.width == 0){
			dim = new Dimension(10, 10);
		}
		return dim; 
	}

	/**
	 * this implementation of the funktion adds a automatic vertical </br>
	 * positioning and a resize to layout()
	 *
	 * @author Lukas Balzer
	 *
	 */
	 @Override
	protected void layout() {
		int y =0;
		int width=0;
		Point offset = ((XYLayout)getLayoutManager()).getOrigin(this);
		Rectangle rect;
		for(Object child:this.getChildren()){
			if(child instanceof IFigure){
//				rect =  (Rectangle) ((XYLayout)getLayoutManager()).getConstraint((IFigure) child);
				rect = ((IFigure) child).getBounds();
				rect.y = y;
				y += rect.height;
				width = Math.max(width, rect.width);
				rect.x = 0;
				rect = rect.getTranslated(offset);
				((IFigure) child).setBounds(rect);
			}
		}
		setPreferredSize(width, y);
	}
	 
	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public CSTextLabel getTextField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLayout(Rectangle rect) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHighlighter(Point ref) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeHighlighter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableOffset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableOffset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDeco(boolean deco) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasDeco() {
		// TODO Auto-generated method stub
		return false;
	}

}
