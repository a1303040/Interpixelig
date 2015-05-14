package itm.infovis;

/*******************************************************************************
This file is part of the ITM course 2015
(c) University of Vienna 2009-2015
*******************************************************************************/

import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;

/**
    This class Implements the force directed layout of the itm visualization
*/
public class ItmForceDirectedLayout extends ForceDirectedLayout {

    /**
        Constructor.
    */
    public ItmForceDirectedLayout( String graph, boolean enforceBounds )
    {
        super( graph, enforceBounds );
    }

    /**
        @return the mass value for the passed visual item
    */
    public float getMassValue( VisualItem item ) 
    {
        float mass = 3f; // the default mass
        return mass;
    }
    
    protected float getSpringCoefficient( EdgeItem e ) 
    {
        return -1;
    }

}
