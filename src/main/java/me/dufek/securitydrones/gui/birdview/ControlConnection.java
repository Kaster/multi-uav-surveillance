package me.dufek.securitydrones.gui.birdview;

import com.google.inject.Inject;
import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base.communication.connection.impl.socket.SocketConnection;
import cz.cuni.amis.pogamut.base.component.bus.IComponentBus;
import cz.cuni.amis.pogamut.base.utils.logging.IAgentLogger;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.UT2004WorldView;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.pogamut.ut2004.server.impl.UT2004Server;

/**
 * Connects to the Unreal Tournament 2004 GameBots server with control
 * connection. We can get global information about environment. Also we can
 * control the server.
 *
 * @author Jan Dufek
 */
public class ControlConnection extends UT2004Server implements IUT2004Server {

    /**
     * Control connection.
     *
     * @param parameters agent parameters
     * @param bus component bus
     * @param agentLogger agent logger
     * @param worldView world view
     * @param act act
     */
    @Inject
    public ControlConnection(UT2004AgentParameters parameters, IAgentLogger agentLogger, IComponentBus bus, SocketConnection connection, UT2004WorldView worldView, IAct act) {
        super(parameters, agentLogger, bus, connection, worldView, act);
    }

    /**
     * Listeners initialization and file creation.
     */
    public void initialize() {
    }
}