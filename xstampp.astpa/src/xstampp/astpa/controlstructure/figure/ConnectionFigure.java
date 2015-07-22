package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.utilities.CSTextLabel;

public class ConnectionFigure extends PolylineConnection implements IControlStructureFigure{

	public ConnectionFigure() {
		super();
		
	}
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}
	private static final int ARROW_WIDTH=6; 
	private static final int ARROW_HEIGHT=6; 
	@Override
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		Point p1 = getPoints().getPoint(getPoints().size() -1);
		Point p2 = getPoints().getPoint(getPoints().size() - 2);
		Point p3 = new Point();
		Point p4 = new Point();
		graphics.setForegroundColor(ColorConstants.black);
		graphics.setBackgroundColor(ColorConstants.black);
		if(p2.y != p1.y){
			int y_of = (p2.y -p1.y)/Math.abs(p2.y-p1.y);
			p4.x = p1.x - ARROW_WIDTH;
			p4.y = p1.y + y_of *ARROW_HEIGHT;

			p3.x = p1.x + ARROW_WIDTH;
			p3.y = p1.y + y_of *ARROW_HEIGHT;
		}else if(p2.x != p1.x){
			int x_of = (p2.x -p1.x)/Math.abs(p2.x-p1.x);
			p4.x = p1.x + x_of * ARROW_HEIGHT;
			p4.y = p1.y -ARROW_WIDTH;

			p3.x = p1.x + x_of * ARROW_HEIGHT;
			p3.y = p1.y + ARROW_WIDTH;
		}else{
			p4.x = p1.x +3;
			p4.y = p1.y -3;

			p3.x = p1.x + 3;
			p3.y = p1.y + 3;
		}
		Rectangle rect= new Rectangle(p3, p4);
		graphics.setClip(rect.getUnion(p1));
		graphics.fillPolygon(new int[]{p1.x,p1.y,p3.x,p3.y,p4.x,p4.y});
	}
	@Override
	protected void paintChildren(Graphics graphics) {
		// TODO Auto-generated method stub
		super.paintChildren(graphics);
	}
	@Override
	public UUID getId() {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	public void showFeedback() {
		setForegroundColor(ColorConstants.lightBlue);
	}
	@Override
	public void disableFeedback() {
		setForegroundColor(ColorConstants.black);
		
	}
	@Override
	protected boolean childrenContainsPoint(int x, int y) {
		Point p= new Point(x, y);
		translateFromParent(p);
		return super.childrenContainsPoint(p.x, p.y);
	}
	@Override
	public Rectangle getBounds() {
		Rectangle rct =getPoints().getBounds().getCopy();
		Rectangle bounds =super.getBounds();
		return rct.setSize(bounds.getSize());
	}
}
