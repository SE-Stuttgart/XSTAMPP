package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import xstampp.astpa.controlstructure.utilities.CSTextLabel;

public class CSRectangleContainer extends Figure implements IControlStructureFigure{
	private UUID id;

	public CSRectangleContainer(UUID id) {
		super();
		this.id= id;
		setLayoutManager(new XYLayout());
		setBorder(new LineBorder(ColorConstants.black, 1, SWT.BORDER_DASH));
	}
	
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension dim = super.getPreferredSize(wHint,hHint);
		if(getChildren().isEmpty()){
			dim = new Dimension(20, 20);
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
			Rectangle tmp;
			for(Object child:this.getChildren()){
				if(child instanceof IFigure){
//					rect =  (Rectangle) ((XYLayout)getLayoutManager()).getConstraint((IFigure) child);
					tmp = ((IFigure) child).getBounds();
					tmp.y = y;
					y += tmp.height;
					width = Math.max(width, tmp.width);
					tmp.x = 0;
					tmp = tmp.getTranslated(offset);
					((IFigure) child).setBounds(tmp);
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
		// a container can only contain other components
		
	}

	@Override
	public CSTextLabel getTextField() {
		return new CSTextLabel(this);
	}

	@Override
	public void setLayout(Rectangle rect) {
		Rectangle bounds = rect.getCopy();
		
		if(getChildren().isEmpty()){
			bounds.setSize(20,20);
		}else{
			layout();
			bounds.setSize(getPreferredSize());
		}

		setBounds(bounds);
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		if(getChildren().isEmpty()){
			FontData tmp = graphics.getFont().getFontData()[0];
			tmp.setHeight(13);
			graphics.setFont(new Font(null, tmp));
			graphics.drawString("?", getLocation().x + 6, getLocation().y -2);
		}
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

	@Override
	public void showFeedback() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableFeedback() {
		// TODO Auto-generated method stub
		
	}
	 

}
