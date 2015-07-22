package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.utilities.CSTextLabel;

public class CSRectangleContainer extends CSFigure {
	

	public CSRectangleContainer(UUID id) {
		super(id, true);
		setBorder(ColorConstants.black);
		setText("?");
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
	public void setDeco(boolean deco) {
		// TODO Auto-generated method stub
		
	}
	 

}
