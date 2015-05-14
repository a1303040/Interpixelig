package itm.infovis;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import prefuse.Visualization;
import prefuse.action.GroupAction;
import prefuse.data.Node;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.VisualItem;

/**
 * This prefuse action navigates from an applet to a resource when the user
 * clicked on it.
 */
public class ItmNavigationAction extends GroupAction {

	protected AppletContext APPLET_CONTEXT = null;
	protected URL URL_CONTEXT = null;

	public ItmNavigationAction(String graphGroup, AppletContext ac, URL uc) {
		super(graphGroup);
		this.APPLET_CONTEXT = ac;
		this.URL_CONTEXT = uc;
	}

	public void run(double frac) {
		TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
		if (focus == null || focus.getTupleCount() == 0)
			return;

		// Graph g = (Graph) m_vis.getGroup( m_group );
		VisualItem vi = null;
		Iterator<?> tuples = focus.tuples();
		while (tuples.hasNext()) {
			vi = (VisualItem) tuples.next();
			if (vi instanceof Node) {
				Node n = (Node) vi;
				String target = n.getString("url");
				System.out.println("got target " + target);
				try {
					URL u = new URL(URL_CONTEXT, target);
					System.out.println("navigating to " + u);
					APPLET_CONTEXT.showDocument(u, "_parent");
				} catch (MalformedURLException e0) {
					System.out
							.println("cannot navigate to clicked node! invalid url! " + URL_CONTEXT + " || " + target);
					return;
				}
			}
		}
	}

}
