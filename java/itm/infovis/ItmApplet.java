package itm.infovis;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.net.URL;

import javax.swing.JLabel;

import prefuse.util.ui.JPrefuseApplet;

/*
 */
public class ItmApplet extends JPrefuseApplet {

	/**
	 * default serial id
	 */
	private static final long serialVersionUID = 1L;

	public void init() {
		try {
			// get ylvis display
			String _dataurl = "http://localhost:8080/itm/graph.jsp";
			String _urlcontext = "http://localhost:8080/itm/";

			if (_dataurl != null) {
				URL dataurl = new URL(_dataurl);
				URL urlcontext = new URL(_urlcontext);
				ItmDisplay yd = new ItmDisplay(dataurl, this.getAppletContext(), urlcontext);
				this.getContentPane().add(yd);
			} else {
				this.getContentPane().add(new JLabel(" Error: Cannot init applet - no dataurl passed!"));
			}
		} catch (Exception e0) {
			System.out.println("error: " + e0);
			this.getContentPane().add(new JLabel(" Error: " + e0));
		}
	}

}
