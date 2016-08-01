package org.blissboy.particlesystem.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.blissboy.particlesystem.messages.ParticleMessages;
import org.blissboy.particlesystem.messages.TimePassedMessage;

import java.io.Serializable;
import java.util.*;

/**
 * Created by patwheaton on 7/31/16.
 */
public class FlockActor extends UntypedActor {


    Set<ActorRef> particleActors;
    Map<Integer,ParticleActor.Particle> particles;

    public FlockActor() {
        particleActors = new HashSet<>();
        particles = new HashMap<>();
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        if ( message instanceof TimePassedMessage) {
            getSender().tell(new FlockParticleStatusMessage(particles.values()), getSelf());
            interactFlock();
            stepFlock((TimePassedMessage)message);
        } else if ( message instanceof ParticleMessages.WantToJoinMessage) {
            addParticle(((ParticleMessages.WantToJoinMessage)message).getWhoWantsToJoin());
            ((ParticleMessages.WantToJoinMessage)message).getWhoWantsToJoin().tell(new WelcomeToFlockMessage(getSelf()), getSelf());
        } else if ( message instanceof ParticleMessages.ParticleMovedMessage ) {
            ParticleActor.Particle p = ((ParticleMessages.ParticleMovedMessage)message).getParticle();
            particles.put(p.id, p);
        }
        System.out.println("Got message " + message.getClass().getCanonicalName());

    }


    private void interactFlock() {
        Integer[] ids = new Integer[particles.keySet().size()];
        particles.keySet().toArray(ids);

        float dist;
        ParticleActor.Particle currentP;
        ParticleActor.Particle compareP;

        for (int i=0; i<ids.length; i++) {
            currentP = particles.get(ids[i]);
            for (int j=i+1; j<ids.length; j++) {
                compareP = particles.get(ids[j]);
                dist = currentP.location.dist(compareP.location);
                if (currentP. )
            }
        }
    }

    private void stepFlock(TimePassedMessage timePassed) {
        for ( ActorRef p : particleActors) {
            p.tell(timePassed, getSelf());
        }
    }

    private void addParticle(ActorRef p) {
        particleActors.add(p);
    }


    /////////////////////////////////////////////////
    //                     messages                //
    /////////////////////////////////////////////////
    public static final class WelcomeToFlockMessage implements Serializable {
        private static final long serialVersionUID = 1L;
        private final ActorRef flockActor;
        public WelcomeToFlockMessage(ActorRef flock) {
            this.flockActor = flock;
        }

        public ActorRef getFlockActor() {
            return flockActor;
        }
    }

    public static final class FlockParticleStatusMessage implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Collection<ParticleActor.Particle> particles;
        public FlockParticleStatusMessage(Collection<ParticleActor.Particle> particles) {
            this.particles = particles;
        }

        public Collection<ParticleActor.Particle> getParticles() {
            return particles;
        }
    }

}
