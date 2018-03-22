package me.dufek.securitydrones.gui.birdview;

import cz.cuni.amis.pogamut.base.communication.messages.CommandMessage;

/**
 * Represents command message to the server which could be sent through control
 * connection.
 *
 * @author Jan Dufek
 */
public class MyCommandMessage extends CommandMessage {
    /**
     * The command message.
     */
    private String message;

    public MyCommandMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return toMessage();
    }

    public String toHtmlString() {
        return toString();
    }

    public String toMessage() {
        return message;
    }
}