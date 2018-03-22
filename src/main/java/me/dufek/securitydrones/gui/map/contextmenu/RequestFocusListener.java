package me.dufek.securitydrones.gui.map.contextmenu;
import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

class RequestFocusListener implements AncestorListener
{
    private boolean removeListener;

    /*
     *  Convenience constructor. The listener is only used once and then it is
     *  removed from the component.
     */
    public RequestFocusListener()
    {
        this(true);
    }

    /*
     *  Constructor that controls whether this listen can be used once or
     *  multiple times.
     *
     *  @param removeListener when true this listener is only invoked once
     *                        otherwise it can be invoked multiple times.
     */
    public RequestFocusListener(boolean removeListener)
    {
        this.removeListener = removeListener;
    }

    @Override
    public void ancestorAdded(AncestorEvent e)
    {
        JComponent component = e.getComponent();
        component.requestFocusInWindow();

        if (removeListener)
            component.removeAncestorListener( this );
    }

    @Override
    public void ancestorMoved(AncestorEvent e) {}

    @Override
    public void ancestorRemoved(AncestorEvent e) {}
}