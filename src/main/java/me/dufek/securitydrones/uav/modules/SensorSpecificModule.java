package me.dufek.securitydrones.uav.modules;

import cz.cuni.amis.pogamut.base.agent.module.SensorModule;
import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.usar2004.agent.USAR2004Bot;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.SensorType;
import cz.cuni.amis.pogamut.usar2004.agent.module.sensor.SuperSensor;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarinfomessages.SensorMessage;

/**
 * Module that is used for particular Sensors. We can have multiple type of
 * sensor mounted on our robot. We can use either
 * SensorMasterModule/SensorMasterModuleQueued to gather all SEN messages or
 * SensorSpecificModule to catch just the type we desire.
 *
 * @author vejmanm
 */
public class SensorSpecificModule<Module extends SuperSensor> extends SensorModule<USAR2004Bot>
{
    protected SensorMessageListener sensorListener;
    protected Module sensor;
    protected boolean flagReaded = false;

    /**
     * Captures and provides data of the first sensor of respective type
     *
     * @param bot USAR2004Bot variable for creating sensor instance
     * @param c Class definition of Generic class. Ideally we would want to say
     * Module.class, but that is not possible
     */
    public SensorSpecificModule(USAR2004Bot bot, Class<Module> c)
    {
        super(bot);
        sensorListener = new SensorMessageListener(worldView);
        sensor = createContents(c);
    }

    /**
     * If we know both type and name of sensor we want to get data from - this
     * ctor is the one to use.
     *
     * @param bot USAR2004Bot variable for creating sensor instance
     * @param name Name of the module
     * @param c Class definition of Generic class. Ideally we would want to say
     * Module.class, but that is not possible
     */
    public SensorSpecificModule(USAR2004Bot bot, String name, Class<Module> c)
    {
        this(bot, c);
        if(this.sensor != null)//updateMessage ensures that isAvailible returns true.
        {
            this.sensor.updateMessage(new SensorMessage(null, 0, name, 0, 0, 0, 0, 0, 0, 0, null, null, 0, false, 0, 0, 0));
        }
    }

    /**
     * Creates a new Generic instance.
     *
     * @param clazz SuperSensor offspring
     * @return Returns SuperSensor instance to be used for storing information
     * about specific sensor.
     */
    Module createContents(Class<Module> clazz)
    {
        try
        {
            return (Module) ModuleInstanceProvider.getSensorInstanceByClass(clazz);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Collection of sensor data check.
     *
     * @return Returns false if either sensor collection is empty or null;
     */
    public Boolean isReady()
    {
        return sensor != null;
    }

    /**
     * Check method for making sure we don't use old value.
     *
     * @return Returns true if getModule() method was called sooner than a new
     * message was listened and our sensor was updated.
     */
    public boolean isReaded()
    {
        return flagReaded;
    }

    /**
     * Returns module of specified type by generics. That means we can call
     * methods on this without the need to cast.
     *
     * @return Returns info about type of sensor picked by generic.
     */
    public Module getModule()
    {
        flagReaded = true;
        return sensor;
    }

    /**
     * Updates sensor data when either the sensor name was not specified(Than it
     * monitors the first Sensor of this type) or the type and name matches.
     *
     * @param message
     */
    protected void fillModule(SensorMessage message)
    {
        if(sensor == null)
        {
            return;
        }
        if(this.sensor.getSensorType() == SensorType.getType(message.getType()))
        {
            if(!sensor.isReady() || this.sensor.getName().equalsIgnoreCase(message.getName()))
            {
                flagReaded = false;
                this.sensor.updateMessage(message);
            }
        }
    }

    @Override
    protected void cleanUp()
    {
        super.cleanUp();
        sensorListener = null;
        sensor = null;
    }

    private class SensorMessageListener implements IWorldEventListener<SensorMessage>
    {
        @Override
        public void notify(SensorMessage event)
        {
            fillModule(event);
        }

        public SensorMessageListener(IWorldView worldView)
        {
            worldView.addEventListener(SensorMessage.class, this);
        }
    }
}
