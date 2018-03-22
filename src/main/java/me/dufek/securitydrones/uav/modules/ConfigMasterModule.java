package me.dufek.securitydrones.uav.modules;

import cz.cuni.amis.pogamut.base.agent.module.SensomotoricModule;
import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.usar2004.agent.USAR2004Bot;
import cz.cuni.amis.pogamut.usar2004.agent.module.configuration.SuperConfiguration;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.MessageDescriptor;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.ConfigContainer;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.ConfigType;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarcommands.GetConf;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarinfomessages.ConfigurationMessage;
import java.util.HashMap;
import java.util.List;

/**
 * ConfigurationMasterModule provides methods for querying messages to server
 * and methods for reading answers to those queries. Please note, that You have
 * to produce suitable query before reading any data. Query methods: starts with
 * query* Module methods: starts with get* Data at each module updates every
 * time You call query method again.
 *
 * @author vejmanm
 */
public class ConfigMasterModule extends SensomotoricModule<USAR2004Bot>
{
    //Quest: Some ConfigTypes such as "Robot" are theoretically useless as long as we wont happen to INIT more than one bot within one USAR2004Bot. So..Do we need List for one Item then?
    //Quest: Should unite With GeoMasterModule, cause its just copy/paste thing going on here...
    protected ConfigurationMessageListener confListener;
    //DataStructure representing the list of all availible SensorTypes, each is listed by name.
    protected ConfigContainer configModules;

    /**
     * Private ctor
     *
     * @param bot USAR2004Bot variable for creating instance of each new record
     * in configModules(due to inheritance)
     */
    public ConfigMasterModule(USAR2004Bot bot)
    {
        super(bot);
        configModules = new ConfigContainer();
        confListener = new ConfigurationMessageListener(worldView);
    }

    /**
     * Collection of sensor data check.
     *
     * @return Returns false if either sensor collection is empty or null;
     */
    public Boolean isReady()
    {
        return (configModules != null && !configModules.isEmpty());
    }

    /**
     * Adds every object that can be casted to initial class to the output list.
     * Note that if You feed this method with SuperClass it will return all
     * available submodules.
     *
     * @param c Class representing the type of which the return list should be
     * @return Returns a list of eligible objects, that can be casted to Class c
     */
    public List<SuperConfiguration> getConfigurationsByClass(Class clazz)
    {
        return configModules.getConfigurationsByClass(clazz);
    }

    /**
     *
     * @param type String representing the type of Configuration to return
     * @return Returns List of specified type of Configuration module.
     */
    public List<SuperConfiguration> getConfigurationsByType(String type)
    {
        if(type == null)
        {
            return null;
        }
        return configModules.getConfigurationsByType(type.toLowerCase());
    }

    /**
     * Note, that if <B>type</B> = UNKNOWN it returns all unknown
     * Configurations.
     *
     * @param type ConfigurationType representing the type of Configuration to
     * return
     * @return Returns List of all Configurations that suit input
     * ConfigurationType.
     */
    public List<SuperConfiguration> getConfigurationsByConfigType(ConfigType type)
    {
        return configModules.getConfigurationsByConfigType(type);
    }

    /**
     * Gets configuration message representatives from local hashmap specified
     * by type and by name. Returns null if none matches or this hash map is
     * empty.
     *
     * @param type String representing the type of configuration to return.
     * @param name String representing the name of configuration to return.
     * @return Returns List of specified type of Configuration representative.
     */
    public SuperConfiguration getConfigurationByTypeName(String type, String name)
    {
        if(type == null || name == null)
        {
            return null;
        }
        return configModules.getConfigurationByTypeName(type.toLowerCase(), name.toLowerCase());//name can be null, but we cant put null to lowerCase!
    }

    /**
     * For each type of Configuration it adds all individuals to the returnee
     * List as a couple (Type, Name)
     *
     * @return returns Map of couples (Type/Name) of non empty Configurations
     */
    public List<MessageDescriptor> getNonEmptyDescription()
    {
        return configModules.getNonEmptyDescription();
    }

    /**
     * Sends GETCONF message with specified type. If the type is genuine, this
     * module will acquire Configuration data of all sensors/effecters specified
     * by <B>type</B>.
     *
     * @param type type of Configuration data we want to know about. For example
     * : Sonar, MisPkg, Robot, Effecter, etc..
     */
    public void queryConfigurationByType(String type)
    {
        queryConfigurationByTypeName(type, null);
    }

    /**
     * Sends GETCONF message with specified type and name. This module then
     * ought to acquire data from server matching requirements.
     *
     * @param type type of Configuration data we want to know about. For example
     * : Sonar, MisPkg, Robot, Effecter, etc..
     * @param name of sensor/effecter. Can be omitted.
     */
    public void queryConfigurationByTypeName(String type, String name)
    {
        this.act.act(new GetConf(type, name));
    }

    /**
     * Asks ConfigType enum if it knows ConfigType represented by string
     * <B>type</B>. If it does, it also contains Class reference. This reference
     * is then instantiated and returned. If it does not, it returns instance of
     * base class SuperConfiguration which is represented by ConfigType.SENSOR.
     *
     * @param type String representing possible valid ConfigType.
     * @return Returns Class instance relevant to input String.
     */
    protected SuperConfiguration createNewSensor(ConfigurationMessage message)
    {
        return ModuleInstanceProvider.getConfigInstanceByType(message.getType());
    }

    /**
     * Returns a flag that indicates if sensorUpdate was successful.
     *
     * @param message
     * @return Return false if this message type with this name does not exist
     * yet.
     */
    protected boolean updateSensorCollection(ConfigurationMessage message)
    {
        if(!configModules.containsKey(message.getType().toLowerCase()))
        {
            return false;
        }
        if(configModules.get(message.getType().toLowerCase()).isEmpty())
        {
            return false;
        }
        if(!configModules.get(message.getType().toLowerCase()).containsKey(message.getName().toLowerCase()))
        {
            return false;
        }
        //sensorModules.get(message.getType().toLowerCase()).get(message.getName().toLowerCase()).updateMessage(message);
        return true;
    }

    /**
     * Updates previous State on genuine Sensor or creates a new Record.
     *
     * @param message This ought to be SensorMessage caught by listener.
     */
    protected void fileMessage(ConfigurationMessage message)
    {
        if(updateSensorCollection(message))
        {
            return;
        }
        if(!configModules.containsKey(message.getType().toLowerCase()))
        {
            configModules.put(message.getType().toLowerCase(), new HashMap<String, SuperConfiguration>());
        }
        if(configModules.get(message.getType().toLowerCase()).isEmpty() || !configModules.get(message.getType().toLowerCase()).containsKey(message.getName().toLowerCase()))
        {
            SuperConfiguration newSensor = createNewSensor(message);
            if(newSensor == null)
            {
                System.out.println("This Configuration message is not supported! " + message.getName());
                return;
            }
            String type = message.getType().toLowerCase();
            String name = message.getName().toLowerCase();
            newSensor.updateMessage(message);//fill the object
            configModules.get(type).put(name, newSensor);
        }
    }

    @Override
    protected void cleanUp()
    {
        super.cleanUp();
        confListener = null;
        configModules = null;
    }

    private class ConfigurationMessageListener implements IWorldEventListener<ConfigurationMessage>
    {
        @Override
        public void notify(ConfigurationMessage event)
        {
            fileMessage(event);
        }

        public ConfigurationMessageListener(IWorldView worldView)
        {
            worldView.addEventListener(ConfigurationMessage.class, this);
        }
    }
}
