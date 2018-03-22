package me.dufek.securitydrones.uav.modules;

import cz.cuni.amis.pogamut.usar2004.agent.module.configuration.SuperConfiguration;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.*;
import cz.cuni.amis.pogamut.usar2004.agent.module.geometry.SuperGeometry;
import cz.cuni.amis.pogamut.usar2004.agent.module.response.SuperResponse;
import cz.cuni.amis.pogamut.usar2004.agent.module.sensor.*;
import cz.cuni.amis.pogamut.usar2004.agent.module.state.SuperState;
import java.lang.reflect.Constructor;

/**
 * Abstract class that uses static methods to offer instances of objects
 * specified by input variables.
 *
 * @author vejmanm
 */
public abstract class ModuleInstanceProvider
{
    /**
     * Class should be valid Sensor representative - SuperSensor offspring. Such
     * class gets particular ctor and creates new instance which is returned.
     *
     * @param clazz Class representing possible valid Sensor
     * @return Returns Class instance relevant to input Class
     */
    public static SuperSensor getSensorInstanceByClass(Class clazz)
    {
        try
        {
            Constructor ctor = clazz.getConstructor();
            SuperSensor instance = (SuperSensor) ctor.newInstance();
            return instance;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + " " + e.toString());
            return null;
        }
    }

    /**
     * Asks SensorType enum if it knows SensorType represented by string
     * <B>type</B>. If it does, it also contains Class reference. This reference
     * is then instantiated and returned. If it does not, it returns instance of
     * base class SuperSensor which is represented by SensorType.UNKNOWN_SENSOR.
     *
     * @param type String representing possible valid SensorType.
     * @return Returns Class instance relevant to input String.
     */
    public static SuperSensor getSensorInstanceByType(String type)
    {
        SensorType senType = SensorType.getType(type);

        try
        {
            Class clazz = senType.getModuleClass();
            Constructor ctor = clazz.getConstructor();
            SuperSensor instance = (SuperSensor) ctor.newInstance();
            return instance;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + " " + e.toString());
            return null;
        }
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
    public static SuperGeometry getGeometryInstanceByType(String type)
    {
        GeometryType geoType = GeometryType.getType(type);

        try
        {
            Class clazz = geoType.getModuleClass();
            Constructor ctor = clazz.getConstructor();
            SuperGeometry instance = (SuperGeometry) ctor.newInstance();
            return instance;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + " " + e.toString());
            return null;
        }
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
    public static SuperConfiguration getConfigInstanceByType(String type)
    {
        ConfigType confType = ConfigType.getType(type);

        try
        {
            Class clazz = confType.getModuleClass();
            Constructor ctor = clazz.getConstructor();
            SuperConfiguration instance = (SuperConfiguration) ctor.newInstance();
            return instance;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + " " + e.toString());
            return null;
        }
    }

    /**
     * Asks ResponseType enum if it knows ResponseType represented by string
     * <B>type</B>. If it does, it also contains Class reference. This reference
     * is then instantiated and returned. If it does not, it returns instance of
     * ResponseSensorEffecter which is represented by
     * ResponseType.SENSOR_EFFECTER.
     *
     * @param type String representing possible valid ResponseType.
     * @return Returns Class instance relevant to input String.
     */
    public static SuperResponse getResponseInstanceByType(String type)
    {
        ResponseType resType = ResponseType.getType(type);

        try
        {
            Class clazz = resType.getModuleClass();
            Constructor ctor = clazz.getConstructor();
            SuperResponse instance = (SuperResponse) ctor.newInstance();
            return instance;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + " " + e.toString());
            return null;
        }
    }

    /**
     * Asks VehicleType (enum) if it knows VehicleType represented by string
     * <B>type</B>. If it does, it also contains Class reference. This reference
     * is then instantiated and returned. If it does not, it returns instance of
     * SuperState which is represented by VehicleType.UNKNOW.
     *
     * @param type String representing possible valid VehicleType.
     * @return Returns Class instance relevant to input String.
     */
    public static SuperState getStateInstanceByType(String type)
    {
        VehicleType resType = VehicleType.getType(type);

        try
        {
            Class clazz = resType.getModuleClass();
            Constructor ctor = clazz.getConstructor();
            SuperState instance = (SuperState) ctor.newInstance();
            return instance;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + " " + e.toString());
            return null;
        }
    }
}
