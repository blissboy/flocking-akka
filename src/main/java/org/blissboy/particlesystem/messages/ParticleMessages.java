package org.blissboy.particlesystem.messages;

import akka.actor.ActorRef;
import org.blissboy.particlesystem.actors.ParticleActor;
import processing.core.PVector;

import java.io.Serializable;

/**
 * Created by patwheaton on 7/30/16.
 */
public class ParticleMessages {


    public static class ChangeLocationMessage implements Serializable {

        private static final long serialVersionUID = 1L;

        public final PVector newLocation;

        public ChangeLocationMessage(PVector newLocation) {
            this.newLocation = newLocation;
        }

        public PVector getNewLocation() {
            return newLocation;
        }
    }

    public interface Reactor {
        enum reactionType {
            COHESE,
            FOLLOW,
            AVOID,
            COLLIDE
        }

        public int getReactionBound();

        public PVector calculateReaction(ParticleActor affected, ParticleActor affector );
    }

    public static class AddReactorMessage implements Serializable {
        private static final long serialVersionUID = 1L;

        private final Reactor reactor;
        private final Reactor.reactionType type;

        public Reactor getReactor() {
            return reactor;
        }

        public Reactor.reactionType getType() {
            return type;
        }

        public AddReactorMessage(Reactor r, Reactor.reactionType type) {
            this.reactor = r;
            this.type = type;
        }
    }


    public static class ParticleMovedMessage implements Serializable {
        private static final long serialVersionUID = 1L;

        private final ParticleActor.Particle particle;

        public ParticleMovedMessage(ParticleActor.Particle particle) {
            this.particle = particle;
        }

        public ParticleActor.Particle getParticle() {
            return particle;
        }
    }

    public static class WantToJoinMessage implements Serializable {
        private static final long serialVersionUID = 1L;

        private final ActorRef whoWantsToJoin;

        public WantToJoinMessage(ActorRef joiner) {
            this.whoWantsToJoin=joiner;
        }
        public ActorRef getWhoWantsToJoin() {
            return whoWantsToJoin;
        }
    }

    public static class ReactedToParticleMessage implements Serializable {
        private static final long serialVersionUID = 1L;
        private final ActorRef whatIReactedTo;

        public ReactedToParticleMessage(ActorRef causeOfReaction) {
            this.whatIReactedTo=causeOfReaction;
        }

        public ActorRef getWhatCausedReaction() {
            return whatIReactedTo;
        }

    }
}
