package itm.infovis;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.applet.AppletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.activity.Activity;
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 * This class realizes the applet display.
 */
public class ItmDisplay extends Display {

	/**
	 * default serial id.
	 */
	private static final long serialVersionUID = 1L;

	private static final String TREE = "tree";
	public static final String GRAPH = "graph";
	public static final String NODES = "graph.nodes";
	public static final String EDGES = "graph.edges";

	protected Table nodes = null;
	protected Table edges = null;

	protected AppletContext APPLET_CONTEXT = null;
	protected URL URL_CONTEXT = null;

	/**
	 * Constructor.
	 */
	public ItmDisplay(URL dataurl, AppletContext ac, URL uc) throws IOException, DataIOException {
		super(new Visualization());
		initData(dataurl);
		this.APPLET_CONTEXT = ac;
		this.URL_CONTEXT = uc;

		// renders the node "name" (and "thumbnail")
		LabelRenderer nodeR = new LabelRenderer("name", null);
		nodeR.setRoundedCorner(8, 8); // round the corners

		// renders the edges
		EdgeRenderer edgeR = new EdgeRenderer(Constants.EDGE_TYPE_CURVE, Constants.EDGE_ARROW_FORWARD);
		edgeR.setArrowHeadSize(8, 8); // arrowhead size

		DefaultRendererFactory drf = new DefaultRendererFactory(nodeR, edgeR);
		drf.setDefaultRenderer(nodeR);
		m_vis.setRendererFactory(drf);

		// set up the visual operators
		// first set up all the color actions
		ColorAction nStroke = new ColorAction(NODES, VisualItem.STROKECOLOR);
		nStroke.setDefaultColor(ColorLib.gray(100));
		nStroke.add("_hover", ColorLib.gray(50));

		ColorAction eStroke = new ColorAction(EDGES, VisualItem.STROKECOLOR);
		eStroke.setDefaultColor(ColorLib.gray(100));

		ColorAction eFill = new ColorAction(EDGES, VisualItem.FILLCOLOR, ColorLib.rgb(190, 190, 255));
		ColorAction nFill = new ItmNodeColorAction(NODES, VisualItem.FILLCOLOR);
		nFill.setDefaultColor(ColorLib.gray(255));
		nFill.add("_hover", ColorLib.gray(150));

		ColorAction nText = new ColorAction(NODES, VisualItem.TEXTCOLOR);
		nText.setDefaultColor(ColorLib.gray(0));
		nText.add("_hover", ColorLib.rgb(255, 0, 0));

		// bundle the color actions
		ActionList colors = new ActionList();
		colors.add(nFill);
		colors.add(eFill);
		colors.add(nStroke);
		colors.add(eStroke);
		colors.add(nText);

		// now create the main layout routine
		// NOTE: especially the force-layout can be configured increase
		// performance (see e.g. setMaxTimeStep() method)
		ActionList layout = new ActionList(Activity.INFINITY, 20);
		// ActionList layout = new ActionList( 5000, 100 );
		layout.add(colors);
		layout.add(new ItmForceDirectedLayout(GRAPH, true));
		layout.add(new RepaintAction());
		m_vis.putAction("layout", layout);

		// create the special ylvi actions
		ActionList openNode = new ActionList();
		openNode.add(new ItmNavigationAction(TREE, APPLET_CONTEXT, URL_CONTEXT));
		m_vis.putAction("openNode", openNode);
		m_vis.run("openNode");

		// set up the display
		setSize(800, 600);
		pan(250, 250);
		setHighQuality(true);
		addControlListener(new ZoomControl());
		addControlListener(new PanControl());
		addControlListener(new FocusControl(2, "openNode"));

		// set things running
		m_vis.run("layout");
		System.out.println("starting infovis...");
	}

	/**
	 * Initialize the data tables
	 */
	private void initData(URL dataurl) throws IOException, DataIOException {
		GraphMLReader gr = new GraphMLReader();
		URLConnection hu = dataurl.openConnection();
		hu.connect();
		InputStream in = hu.getInputStream();
		if (in == null)
			throw new IOException("could not get input stream from URL connection " + dataurl);
		Graph g = gr.readGraph(in);
		VisualGraph vg = m_vis.addGraph(GRAPH, g);
		m_vis.setInteractive(EDGES, null, false);
		m_vis.setValue(NODES, null, VisualItem.SHAPE, new Integer(Constants.SHAPE_ELLIPSE));

	}

}
