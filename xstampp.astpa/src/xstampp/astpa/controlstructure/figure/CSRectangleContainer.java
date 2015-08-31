package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.TreeSearch;
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
	private boolean selected;
	private static final int TOP_OFFSET= 5;
	private static final int BOTTOM_OFFSET= 5;
	
	public CSRectangleContainer(UUID id) {
		super();
		this.selected = false;
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

	@Override
	protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {
		if(isSelected()){
			return super.findDescendantAtExcluding(x, y, search);
		}
		return null;
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
			int y =TOP_OFFSET;
			int width=0;
			Point offset = ((XYLayout)getLayoutManager()).getOrigin(this);
			Rectangle tmp;
			for(Object child:this.getChildren()){
				if(child instanceof IFigure){
//					rect =  (Rectangle) ((XYLayout)getLayoutManager()).getConstraint((IFigure) child);
					tmp = ((IFigure) child).getBounds();
					tmp.y = y;
					y += tmp.height + TOP_OFFSET;
					width = Math.max(width, tmp.width);
					tmp.x = TOP_OFFSET;
					tmp = tmp.getTranslated(offset);
					((IFigure) child).setBounds(tmp);
				}
			}
		setPreferredSize(width + 2 + TOP_OFFSET, y +5);
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
		Rectangle bounds1 = rect.getCopy();
		
		if(getChildren().isEmpty()){
			bounds1.setSize(20,20);
		}else{
			layout();
			bounds1.setSize(getPreferredSize());
		}

		setBounds(bounds1);
		this.getParent().setConstraint(this, bounds1);
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

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	 

}
