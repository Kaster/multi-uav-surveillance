package me.dufek.securitydrones.uav.modules;

import cz.cuni.amis.pogamut.base.agent.module.SensorModule;
import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.usar2004.agent.USAR2004Bot;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.StateContainer;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.VehicleType;
import cz.cuni.amis.pogamut.usar2004.agent.module.state.SuperState;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarinfomessages.StateMessage;
import java.util.List;

/**
 * Master module for gathering every state message server sends. They are saved
 * respectively by their type and than name. There is a listener for STA
 * messages that updates data in StateContainer. There is also Mission package
 * module because STA and MISSTA messages are both State messages. All
 * mastermodules are singletons.
 *
 * @author vejmanm
 */
public class StateMasterModule extends SensorModule<USAR2004Bot> {

    protected StateMessageListener stateListener;
    //DataStructure representing the list of all availible StateTypes, each is listed by name.
    protected StateContainer stateModule;
    protected MissionPackageModule packageModule;

    /**
     * Private ctor
     *
     * @param bot USAR2004Bot variable for creating instance of each new record
     * in stateModules(due to inheritance)
     */
    public StateMasterModule(USAR2004Bot bot) {
        super(bot);
        stateModule = new StateContainer();
        stateListener = new StateMessageListener(worldView);
        packageModule = new MissionPackageModule(bot);
    }

    /**
     * Check method for making sure the acces to this module is possible.Returns
     * false if either state collection is empty or null;
     *
     * @return Returns false if either state collection is empty or null;
     */
    public Boolean isReady() {
        return (stateModule != null && !stateModule.isEmpty());
    }

    /**
     * Gets state message representative from local hashmap. Returns null if
     * none matches.
     *
     * @param type String representation of state type.
     * @return Returns specific state message matching input string <b>type</b>
     */
    public SuperState getStatesByType(String type) {
        if (type == null) {
            return null;
        }
        return stateModule.getStatesByType(type.toLowerCase());
    }

    /**
     * Iterates through local hashmap values and seeks match. Note that every
     * SuperState offspring has its own VehicleType property. Returns null if
     * none matches.
     *
     * @param type VehicleType representation of state type - easier to use
     * since we don't have to watch after propper spelling.
     * @return Returns specific state message matching input VehicleType
     * <b>type</b>
     */
    public SuperState getStatesByVehilceType(VehicleType type) {
        if (stateModule == null) {
            return null;
        }
        
        return stateModule.getStatesByVehicleType(type);
    }

    /**
     * Iterates through local hahmap values and takes note of every SuperState
     * object that is Instance of specified class. Returns null if none matches
     * or <b>c</b> is null.
     *
     * @param c <B>c</B> is class that the state message should extend, So when
     * SuperState is inserted as c, it should return list of all types of
     * vehicles that this holds data of.
     * @return Returns list of state objects that implements class <B>c</B>
     */
    public List<SuperState> getStatesByClass(Class c) {
        return stateModule.getStatesByClass(c);
    }

    /**
     * Returns list of availible state object names.
     *
     * @return Returns list of availible state object names.
     */
    public List<String> getAvailibleTypes() {
        return stateModule.getAvailibleTypes();
    }

    /**
     * Asks VehicleType (enum) if it knows VehicleType represented by string
     * <B>type</B>. If it does, it also contains Class reference. This reference
     * is then instantiated and returned. If it does not, it returns instance of
     * SuperState which is represented by VehicleType.UNKNOW.
     *
     * @param message StateMessage object containing valid VehicleType.
     * @return Returns Class instance relevant to input message.
     */
    protected SuperState createNewState(StateMessage message) {
        return ModuleInstanceProvider.getStateInstanceByType(message.getType());
    }

    /**
     * Returns a flag that indicates if stateUpdate was successful.
     *
     * @param message new StateMessage object.
     * @return Return false if this message type with this name does not exist
     * yet.
     */
    protected boolean updateStateCollection(StateMessage message) {
        if (!stateModule.containsKey(message.getType().toLowerCase())) {
            return false;
        }
        stateModule.get(message.getType().toLowerCase()).updateMessage(message);
        return true;
    }

    /**
     * Updates previous State or creates a new Record.
     *
     * @param message This ought to be StateMessage caught by listener.
     */
    protected void fileMessage(StateMessage message) {
        if (updateStateCollection(message)) {
            return;
        }

        SuperState newState = createNewState(message);
        if (newState == null) {
            System.out.println("This state is not supported! " + message.getType());
            return;
        }
        String type = message.getType().toLowerCase();
        newState.updateMessage(message);//fill the object
        stateModule.put(type, newState);
    }

    /**
     * Mission package module for information about misspkgs.
     *
     * @return Returns Map<String,MissionPackageState> singleton that holds
     * information abou MissPkg.
     */
    public MissionPackageModule getMissionPackageModule() {
        return packageModule;
    }

    @Override
    protected void cleanUp() {
        super.cleanUp();
        stateListener = null;
        stateModule = null;
    }

    private class StateMessageListener implements IWorldEventListener<StateMessage> {

        @Override
        public void notify(StateMessage event) {
            fileMessage(event);
        }

        public StateMessageListener(IWorldView worldView) {
            worldView.addEventListener(StateMessage.class, this);
        }
    }
}
