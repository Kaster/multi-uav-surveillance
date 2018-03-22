package me.dufek.securitydrones.uav.modules;

import cz.cuni.amis.pogamut.base.agent.module.SensomotoricModule;
import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.usar2004.agent.USAR2004Bot;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.GeometryContainer;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.GeometryType;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.MessageDescriptor;
import cz.cuni.amis.pogamut.usar2004.agent.module.geometry.SuperGeometry;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarcommands.GetGeo;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarinfomessages.GeometryMessage;
import java.util.HashMap;
import java.util.List;

/**
 * GeometryMasterModule provides methods for querying messages to server and
 * methods for reading answers to those queries. Please note, that You have to
 * produce suitable query before reading any data. Query methods: starts with
 * query* Module methods: starts with get* Data at each module updates every
 * time You call query method again.
 *
 * @author vejmanm
 */
public class GeometryMasterModule extends SensomotoricModule<USAR2004Bot>
{
    //Quest: Some GeoTypes such as "Robot" are theoretically useless as long as we wont happen to INIT more than one bot within one USAR2004Bot. So..Do we need List for one Item then?
    protected GeometryMessageListener geoListener;
    //DataStructure representing the list of all availible SensorTypes, each is listed by name.
    protected GeometryContainer geometryModules;

    /**
     * Private ctor
     *
     * @param bot USAR2004Bot variable for creating instance of each new record
     * in geometryModules(due to inheritance)
     */
    public GeometryMasterModule(USAR2004Bot bot)
    {
        super(bot);
        geometryModules = new GeometryContainer();
        geoListener = new GeometryMessageListener(worldView);
    }

    /**
     * Collection of sensor data check.
     *
     * @return Returns false if either sensor collection is empty or null;
     */
    public Boolean isReady()
    {
        return (geometryModules != null && !geometryModules.isEmpty());
    }

    /**
     * Adds every object that can be casted to initial class to the output list.
     * Note that if You feed this method with SuperClass it will return all
     * available submodules.
     *
     * @param c Class representing the type of which the return list should be
     * @return Returns a list of eligible objects, that can be casted to Class c
     */
    public List<SuperGeometry> getGeometriesByClass(Class clazz)
    {
        return geometryModules.getGeometriesByClass(clazz);
    }

    /**
     *
     * @param type String representing the type of Geometry to return
     * @return Returns List of specified type of Geometry module.
     */
    public List<SuperGeometry> getGeometriesByType(String type)
    {
        if(type == null)
        {
            return null;
        }
        return geometryModules.getGeometriesByType(type.toLowerCase());
    }

    /**
     * Note, that if <B>type</B> = UNKNOWN it returns all unknown Geometries.
     *
     * @param type GeometryType representing the type of Geometry to return
     * @return Returns List of all Geometries that suit input GeometryType.
     */
    public List<SuperGeometry> getGeometriesByGeometryType(GeometryType type)
    {
        return geometryModules.getGeometriesByGeometryType(type);
    }

    /**
     * Gets geometry message representatives from local hashmap specified by
     * type and by name. Returns null if none matches or this hash map is empty.
     *
     * @param type String representing the type of geometry to return.
     * @param name String representing the name of geometry to return.
     * @return Returns List of specified type of Geometry representative.
     */
    public SuperGeometry getGeometryByTypeName(String type, String name)
    {
        if(type == null || name == null)
        {
            return null;
        }
        return geometryModules.getGeometryByTypeName(type.toLowerCase(), name.toLowerCase());//name can be null, but we cant put null to lowerCase!
    }

    /**
     * For each type of Geometry it adds all individuals to the returnee List as
     * a couple (Type, Name)
     *
     * @return returns Map of couples (Type/Name) of non empty Geometries
     */
    public List<MessageDescriptor> getNonEmptyDescription()
    {
        return geometryModules.getNonEmptyDescription();
    }

    /**
     * Sends GETGEO message with specified type. If the type is genuine, this
     * module will acquire geometry data of all sensors/effecters specified by
     * <B>type</B>.
     *
     * @param type type of geometry data we want to know about. For example :
     * Sonar, MisPkg, Robot, Effecter, etc..
     */
    public void queryGeometryByType(String type)
    {
        queryGeometryByTypeName(type, null);
    }

    /**
     * Sends GETGEO message with specified type and name. This module then ought
     * to acquire data from server matching requirements.
     *
     * @param type type of geometry data we want to know about. For example :
     * Sonar, MisPkg, Robot, Effecter, etc..
     * @param name of sensor/effecter. Can be omitted.
     */
    public void queryGeometryByTypeName(String type, String name)
    {
        this.act.act(new GetGeo(type, name));
    }

    /**
     * Asks GeometryType enum if it knows GeometryType represented by string
     * <B>type</B>. If it does, it also contains Class reference. This reference
     * is then instantiated and returned. If it does not, it returns instance of
     * base class SuperGeometry which is represented by
     * GeometryType.SENSOR_EFFECTER.
     *
     * @param type String representing possible valid GeometryType.
     * @return Returns Class instance relevant to input String.
     */
    protected SuperGeometry createNewSensor(GeometryMessage message)
    {
        return ModuleInstanceProvider.getGeometryInstanceByType(message.getType());
    }

    /**
     * Returns a flag that indicates if sensorUpdate was successful.
     *
     * @param message
     * @return Return false if this message type with this name does not exist
     * yet.
     */
    protected boolean updateSensorCollection(GeometryMessage message)
    {
        if(!geometryModules.containsKey(message.getType().toLowerCase()))
        {
            return false;
        }
        if(geometryModules.get(message.getType().toLowerCase()).isEmpty())
        {
            return false;
        }
        if(!geometryModules.get(message.getType().toLowerCase()).containsKey(message.getName().toLowerCase()))
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
    protected void fileMessage(GeometryMessage message)
    {
        if(updateSensorCollection(message))
        {
            return;
        }
        if(!geometryModules.containsKey(message.getType().toLowerCase()))
        {
            geometryModules.put(message.getType().toLowerCase(), new HashMap<String, SuperGeometry>());
        }
        if(geometryModules.get(message.getType().toLowerCase()).isEmpty() || !geometryModules.get(message.getType().toLowerCase()).containsKey(message.getName().toLowerCase()))
        {
            SuperGeometry newSensor = createNewSensor(message);
            if(newSensor == null)
            {
                System.out.println("This geometry message is not supported! " + message.getName());
                return;
            }
            String type = message.getType().toLowerCase();
            String name = message.getName().toLowerCase();
            newSensor.updateMessage(message);//fill the object
            geometryModules.get(type).put(name, newSensor);
        }
    }

    @Override
    protected void cleanUp()
    {
        super.cleanUp();
        geoListener = null;
        geometryModules = null;
    }

    private class GeometryMessageListener implements IWorldEventListener<GeometryMessage>
    {
        @Override
        public void notify(GeometryMessage event)
        {
            fileMessage(event);
        }

        public GeometryMessageListener(IWorldView worldView)
        {
            worldView.addEventListener(GeometryMessage.class, this);
        }
    }
}
