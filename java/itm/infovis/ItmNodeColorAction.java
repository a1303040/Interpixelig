package itm.infovis;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2008-2015
 *******************************************************************************/

import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
    This prefuse action defines the node colors.
*/
public class ItmNodeColorAction extends ColorAction {
        
    protected String colorActionField = null;
            
    /**
        Constructor.
    */
    public ItmNodeColorAction( String group, String field ) 
    {
        super( group, field );
        this.colorActionField = field;
    }
    
    /**
        @return the color for the passed visual item
    */
    public int getColor( VisualItem item ) 
    {
        if ( item.canGetString( "type" ) ) {
            String ntype = item.getString( "type" );
            
            if ( colorActionField.equals( VisualItem.FILLCOLOR ) ) {
                if ( ntype.equals( "concept" ) )
                    return ColorLib.rgb( 255, 255, 0 ); // yellow
                    else
                    return ColorLib.rgb( 255, 255, 255 ); // white
                
                } else 
            if ( colorActionField.equals( VisualItem.TEXTCOLOR ) ) {

                if ( ntype.equals( "concept" ) )
                    return ColorLib.rgb( 57, 171, 255 ); // blue
                    else
                    return ColorLib.rgb( 0, 0, 0 ); // black
                } 
            }
        return ColorLib.rgb( 0, 0, 0 ); // "black"
        //return ColorLib.rgba(255,255,255,0); // "invisible"
    }
    
} 
