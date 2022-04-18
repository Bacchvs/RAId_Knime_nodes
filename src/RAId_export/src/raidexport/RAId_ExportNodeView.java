package raidexport;

import org.knime.core.node.NodeView;


/**
 * This is an example implementation of the node view of the
 * "RAId_Export" node.
 * 
 * As this example node does not have a view, this is just an empty stub of the 
 * NodeView class which not providing a real view pane.
 *
 * @author Arnaud S�n�caut
 */
public class RAId_ExportNodeView extends NodeView<RAId_ExportNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RAId_ExportNodeModel})
     */
    protected RAId_ExportNodeView(final RAId_ExportNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        RAId_ExportNodeModel nodeModel = 
            (RAId_ExportNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        this.setShowNODATALabel(true);
            
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
    	
        // TODO things to do when opening the view
    }

}

