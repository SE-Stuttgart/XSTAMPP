package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;

import xstampp.astpa.controlstructure.controller.editparts.IMemberEditPart;
import xstampp.astpa.controlstructure.utilities.CSTextLabel;

public class ConnectionFigure extends PolylineConnection implements IControlStructureFigure{

	private int[] arrowHead;
	private PolylineConnection feedback;
	private UUID currentFeedbackId;
	private UUID id;
	private IPreferenceStore store;
	public ConnectionFigure(UUID id) {
		super();
		this.id = id;
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
//		updateFeedback();
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
		if(arrowHead != null){
			graphics.setBackgroundColor(xstampp.util.ColorManager.COLOR_WHITE);
			graphics.fillPolygon(arrowHead);
			graphics.setBackgroundColor(xstampp.util.ColorManager.COLOR_BLACK);
		}
		arrowHead = new int[]{p1.x,p1.y,p3.x,p3.y,p4.x,p4.y};
		graphics.fillPolygon(arrowHead);
	}
	@Override
	protected void paintChildren(Graphics graphics) {
		super.paintChildren(graphics);
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
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
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
	
	public void eraseFeedback() {
		if(this.feedback != null){
			this.feedback.setVisible(false);
		}
		
	}
	
	public IFigure getFeedback() {
		return getFeedback((IFigure)null);
	}
	
	public IFigure getFeedback(IMemberEditPart member) {
		
		if(this.feedback == null){
			this.feedback = new PolylineConnection();
			this.feedback.setAlpha(150);
			this.feedback.setAntialias(SWT.ON);
			this.feedback.setLineWidth(4);
			this.feedback.setForegroundColor(ColorConstants.darkGreen);
		}
		this.feedback.setVisible(true);
		
		if(member != null && this.currentFeedbackId != member.getId()){
			this.currentFeedbackId = member.getId();
			this.feedback.setConnectionRouter(new FeedbackRouter(member.getFigure()));
		}
		updateFeedback();
		return getFeedback(member.getFigure());
	}
	
	/**
	 * @deprecated use getFeedbach(IFigure) instead
	 *
	 * @author Lukas Balzer
	 *
	 * @param bounds
	 * @return
	 */
	@Deprecated
	public IFigure getFeedback(Rectangle bounds){
		IFigure figure= new Figure();
		if(bounds != null){
			figure.setBounds(bounds);
		}
		return getFeedback(figure);
	}
	
	public IFigure getFeedback(IFigure member) {
		if(this.feedback == null){
			this.feedback = new PolylineConnection();
			this.feedback.setAlpha(150);
			this.feedback.setAntialias(SWT.ON);
			this.feedback.setLineWidth(4);
			this.feedback.setForegroundColor(ColorConstants.darkGreen);
		}
		this.feedback.setVisible(true);
		this.feedback.setConnectionRouter(new FeedbackRouter(member));
		
		updateFeedback();
		return this.feedback;
	}
	
	private class FeedbackRouter extends AbstractRouter{
		private IFigure targetFigure;
		private ManhattanConnectionRouter router;
		
		public FeedbackRouter(IFigure member) {
			this.targetFigure=member;
			this.router= new ManhattanConnectionRouter();
		}
		public FeedbackRouter(Rectangle rect) {
			this.router= new ManhattanConnectionRouter();
			
		}
		@Override
		public void route(Connection connection) {
			connection.setSourceAnchor(getSourceAnchor());
			connection.setTargetAnchor(getTargetAnchor());
			this.router.route(connection);
			if(this.targetFigure == null){
				return;
			}
			PointList list = connection.getPoints().getCopy();
			
			Point a=list.removePoint(list.size()-1);
			
			Point b=list.getPoint(list.size()-1);
			Dimension diff = a.getDifference(b);
			Point middle = b.getTranslated(diff.width/2, diff.height/2);
			Rectangle targetBounds = this.targetFigure.getBounds().getCopy();
			this.targetFigure.translateToAbsolute(targetBounds);
			connection.translateToRelative(targetBounds);
			Point center = targetBounds.getCenter().getCopy();
			
			for(int i=0;i<10;i++){
//				this.targetFigure.translateToAbsolute(center);
				Rectangle loop_tmp = new Rectangle(center,middle);
//				this.targetFigure.translateToAbsolute(tmp);
				targetBounds = targetBounds.intersect(loop_tmp);
				center = targetBounds.getCenter().getCopy();
			}
			list.addPoint(middle);
			list.addPoint(center);
			list.addPoint(middle);
			list.addPoint(b);
			list.addPoint(a);
//			this.targetFigure.translateToAbsolute(list);
			connection.setPoints(list);
		}
		
	}

	public void updateFeedback() {
		if(this.feedback != null){
			this.feedback.getConnectionRouter().route(this.feedback);
		}
	}

	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		this.store = store;
	}
	@Override
	public void setDirty() {
		// TODO Auto-generated method stub
		
	}
}
