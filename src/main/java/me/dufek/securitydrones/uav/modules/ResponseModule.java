package me.dufek.securitydrones.uav.modules;

import cz.cuni.amis.pogamut.base.agent.module.SensorModule;
import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.usar2004.agent.USAR2004Bot;
import cz.cuni.amis.pogamut.usar2004.agent.module.response.SuperResponse;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarinfomessages.ResponseMessage;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This is basically a queue for RES messages. User sends commands to the server
 * and this module catches ResponseMessages that contain information about
 * Server state. All masterm modules are singletons.
 *
 * @author vejmanm
 */
public class ResponseModule extends SensorModule<USAR2004Bot>
{
    Queue<SuperResponse> respQueue;
    protected ResponseMessageListener confListener;

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
    protected SuperResponse createNewSensor(ResponseMessage message)
    {
        return ModuleInstanceProvider.getResponseInstanceByType(message.getType());
    }

    /**
     * Private ctor
     *
     * @param bot USAR2004Bot variable for creating instance of each new record
     * in responseModules(due to inheritance)
     */
    public ResponseModule(USAR2004Bot bot)
    {
        super(bot);
        respQueue = new LinkedList<SuperResponse>();
        confListener = new ResponseMessageListener(worldView);
    }

    /**
     * This method offers to peek on the beginning of the queue, but the queue
     * stays intact. If queue is empty, it returns null
     *
     * @return Returns the first Response object that was captured.
     */
    public SuperResponse peek()
    {
        return this.respQueue.peek();
    }

    /**
     * Removes one message from the queue and offers this very object as an
     * output. If queue is empty, it returns null
     *
     * @return Returns the first Response object that was captured.
     */
    public SuperResponse pull()
    {
        return this.respQueue.poll();
    }

    /**
     * Returns number of messages waiting in the queue.
     *
     * @return Returns number of messages waiting in the queue.
     */
    public int size()
    {
        return this.respQueue.size();
    }

    /**
     * Adds new Response to local queue if there is still a space.
     *
     * @param message This ought to be ResponseMessage caught by listener.
     */
    private void addMessage(ResponseMessage message)
    {
        SuperResponse instance = createNewSensor(message);
        instance.updateMessage(message);
        if(!respQueue.offer(instance))
        {
            System.out.println("CAPACITY OF THE QUEUE " + this.toString() + " IS REACHED, CAN NOT ACCEPT ANY MORE RESPONSE MESSAGES");
        }
    }

    private class ResponseMessageListener implements IWorldEventListener<ResponseMessage>
    {
        @Override
        public void notify(ResponseMessage event)
        {
            addMessage(event);
        }

        public ResponseMessageListener(IWorldView worldView)
        {
            worldView.addEventListener(ResponseMessage.class, this);
        }
    }
}
