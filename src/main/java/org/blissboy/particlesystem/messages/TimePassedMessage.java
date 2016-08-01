package org.blissboy.particlesystem.messages;

import java.io.Serializable;

/**
 * Created by patwheaton on 7/30/16.
 */
public class TimePassedMessage implements Serializable {

    public final long newTime;

    public TimePassedMessage(long newTime) {
        this.newTime = newTime;
    }

}
