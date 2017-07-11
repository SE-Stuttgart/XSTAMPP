package xstampp.stpapriv.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import xstampp.stpapriv.Activator;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpapriv.model.controlaction.ControlAction;
import xstampp.stpapriv.model.controlaction.UnsecureControlAction;
import xstampp.stpapriv.model.results.ConstraintResult;

public class DiagramView extends ViewPart {
	public static final String ID = "xstampp.stpapriv.diagram";
	private Graph graph;
	private int layout = 1;
	ControlAction controlAction;
	List<ConstraintResult> results;
	Activator activator;
	Composite parentcomp;
	ConstraintResult ca;
	PrivacyController controller;
	List<GraphNode> correspondingNodes = new ArrayList<GraphNode>();
	List<GraphNode> unsecureNodes = new ArrayList<GraphNode>();
	List<GraphNode> relatedNodes = new ArrayList<GraphNode>();

	public static final Device device = Display.getCurrent();
	public static final Color BACKGROUND = new Color(device, 255, 102, 102);
	public static final Color BACKGROUND1 = new Color(device, 255, 255, 51);
	public static final Color BACKGROUND2 = new Color(device, 51, 153, 255);

	public void createPartControl(Composite parent) {
		this.parentcomp = parent;
		activator = Activator.getDefault();
		
		// Graph will hold all other objects
		graph = new Graph(parent, SWT.NONE);
		System.out.println(controlAction != null);
		// now a few nodes



	}

	public DiagramView() {

	}

	public DiagramView(ControlAction control) {
		super();
		this.controlAction = control;
	}

	public void setLayoutManager() {
		switch (layout) {
		case 1:
			graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			// layout++;
			break;
		case 2:
			graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			// layout = 1;
			break;

		}

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

	public void setInput(ConstraintResult ca, PrivacyController data) {
		this.controller=data;
		this.controlAction = ca.getTemp();
		this.ca = ca;
	}

	public void refresh() {
		results= controller.getAllConstraintResults();
		correspondingNodes = new ArrayList<GraphNode>();
		unsecureNodes = new ArrayList<GraphNode>();
		relatedNodes = new ArrayList<GraphNode>();
		Object[] objects = graph.getConnections().toArray();
		for (int i = 0; i < objects.length; i++) {
			GraphConnection graCon = (GraphConnection) objects[i];
			if (!graCon.isDisposed())
				graCon.dispose();
		}

		objects = graph.getNodes().toArray();
		for (int i = 0; i < objects.length; i++) {
			GraphNode graNode = (GraphNode) objects[i];
			if (!graNode.isDisposed())
				graNode.dispose();
		}
		// now a few nodes
		if (controlAction != null) {
			StringBuilder sb = new StringBuilder(controlAction.getTitle());

			int i = 0;
			while ((i = sb.indexOf(" ", i + 20)) != -1) {
				sb.replace(i, i + 1, "\n");
			}
			GraphNode node1 = new GraphNode(graph, SWT.NONE, sb.toString());
			if (!controlAction.getUnsafeControlActions().isEmpty()) {
			
				for (int k=0;k<controlAction.getUnsafeControlActions().size();k++) {
					GraphNode node;
					GraphNode node2;
					UnsecureControlAction temp2 = (UnsecureControlAction) controlAction.getUnsafeControlActions().get(k);
					if (temp2.getDescription() != null && !temp2.getDescription().equals(" ")
							&& !temp2.getDescription().equals("")) {
						sb = new StringBuilder(temp2.getDescription());

						i = 0;
						while ((i = sb.indexOf(" ", i + 20)) != -1) {
							sb.replace(i, i + 1, "\n");
						}
						 node=new GraphNode(graph, SWT.NONE, sb.toString());

						sb = new StringBuilder(temp2.getCorrespondingSafetyConstraint().getText());

						i = 0;
						while ((i = sb.indexOf(" ", i + 20)) != -1) {
							sb.replace(i, i + 1, "\n");
						}
						node2=new GraphNode(graph, SWT.NONE, sb.toString());


						for(ConstraintResult res: results){
							if((("SC1." + temp2.getNumber()).equals(res.getScId()))
								){
								GraphNode node3;
								for (String related : res.getRelatedConstraints()) {
									
										sb = new StringBuilder(related);

										i = 0;
										while ((i = sb.indexOf(" ", i + 20)) != -1) {
											sb.replace(i, i + 1, "\n");
										}
										node3=new GraphNode(graph, SWT.NONE, sb.toString());
										new GraphConnection(graph, ZestStyles.CONNECTIONS_DOT,
												node2, node3);
										if (temp2.isSafetyCritical && !temp2.isSecurityCritical) {
											node3.setBackgroundColor(BACKGROUND2);
										
										}else if (temp2.isSecurityCritical && !temp2.isSafetyCritical) {
											node3.setBackgroundColor(BACKGROUND1);
										}else if (temp2.isSecurityCritical && temp2.isSafetyCritical) {
											node3.setBackgroundColor(BACKGROUND);
										}
								
									

								}
							}
						}
						
						if (temp2.isSafetyCritical && !temp2.isSecurityCritical) {
							node.setBackgroundColor(BACKGROUND2);
							node2.setBackgroundColor(BACKGROUND2);
			

						} else if (temp2.isSecurityCritical && !temp2.isSafetyCritical) {
							node.setBackgroundColor(BACKGROUND1);
							node2.setBackgroundColor(BACKGROUND1);

						} else if (temp2.isSecurityCritical && temp2.isSafetyCritical) {
							node.setBackgroundColor(BACKGROUND);
							node2.setBackgroundColor(BACKGROUND);


						}
						new GraphConnection(graph, ZestStyles.CONNECTIONS_DOT, node1,
								node);
						new GraphConnection(graph, ZestStyles.CONNECTIONS_DOT, node,
								node2);

					}

				}
			
			}
		}

		setLayoutManager();
		graph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(e);
			}

		});
	}
}
