package me.dufek.securitydrones.uav.modules;

import cz.cuni.amis.pogamut.base.agent.module.SensorModule;
import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.usar2004.agent.USAR2004Bot;
import cz.cuni.amis.pogamut.usar2004.agent.module.state.MissionPackageState;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarinfomessages.MissionPackageMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Master module for gathering every mission state message server sends. They
 * are saved respectively by their name. There is a listener for MISSTA messages
 * that updates data in local Map. All mastermodules are singletons.
 *
 * @author vejmanm
 */
public class MissionPackageModule extends SensorModule<USAR2004Bot>
{
    protected MissionPackageMessageListener packageListener;
    protected Map<String, MissionPackageState> packageModule;

    /**
     * Private ctor
     *
     * @param bot USAR2004Bot variable for creating instance of each new record
     * in stateModules(due to inheritance)
     */
    public MissionPackageModule(USAR2004Bot bot)
    {
        super(bot);
        packageModule = new HashMap<String, MissionPackageState>();
        packageListener = new MissionPackageMessageListener(worldView);
    }

    /**
     * Check method for making sure the acces to this module is possible.Returns
     * false if either local map is empty or null;
     *
     * @return Returns false if either local map is empty or null;
     */
    public Boolean isReady()
    {
        return (packageModule != null && !packageModule.isEmpty());
    }

    /**
     * Gets mission state message representative from local hashmap. Returns
     * null if none matches.
     *
     * @param name String representing the type of state to return
     * @return Returns mission package State according to the name.
     */
    public MissionPackageState getStatesByName(String name)
    {
        if(name == null)
        {
            return null;
        }
        return packageModule.get(name.toLowerCase());
    }

    /**
     * Returns list of availible mission state object types.
     *
     * @return Returns list of availible mission state object types.
     */
    public Set<String> getAvailibleTypes()
    {
        return packageModule.keySet();
    }

    /**
     * Returns a flag that indicates if udatePackage was successful.
     *
     * @param message new MissionPackageMessage object.
     * @return Return false if this message type with this name does not exist
     * yet.
     */
    protected boolean updatePackageCollection(MissionPackageMessage message)
    {
        if(!packageModule.containsKey(message.getName().toLowerCase()))
        {
            return false;
        }
        packageModule.get(message.getName().toLowerCase()).updateMessage(message);
        return true;
    }

    /**
     * Updates previous MissionPackageMessage or creates a new Record.
     *
     * @param message This ought to be MissionPackageMessage caught by listener.
     */
    protected void filePackageMessage(MissionPackageMessage message)
    {
        if(updatePackageCollection(message))
        {
            return;
        }

        MissionPackageState newPackage = new MissionPackageState();
        String name = message.getName().toLowerCase();
        newPackage.updateMessage(message);//fill the object
        packageModule.put(name, newPackage);
    }

    @Override
    protected void cleanUp()
    {
        super.cleanUp();
        packageListener = null;
        packageModule = null;
    }

    private class MissionPackageMessageListener implements IWorldEventListener<MissionPackageMessage>
    {
        @Override
        public void notify(MissionPackageMessage event)
        {
            filePackageMessage(event);
        }

        public MissionPackageMessageListener(IWorldView worldView)
        {
            worldView.addEventListener(MissionPackageMessage.class, this);
        }
    }
}
