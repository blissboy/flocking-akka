package org.blissboy.particlesystem.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.blissboy.particlesystem.DrawingUtilities;
import org.blissboy.particlesystem.messages.ParticleMessages;
import org.blissboy.particlesystem.messages.ParticleMessages.Reactor;
import org.blissboy.particlesystem.messages.TimePassedMessage;
import processing.core.PVector;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by patwheaton on 7/30/16.
 */
public class ParticleActor extends UntypedActor  {

    private Optional<ActorRef> ourFlock = Optional.empty();
    Map<Reactor.reactionType, Reactor> reactors;
    private PVector location = DrawingUtilities.getOriginVector();
    private PVector velocity = DrawingUtilities.getOriginVector();
    final int id;



    /**
     * Create Props for an actor of this type.
     * @param id The magic number to be passed to this actor’s constructor.
     * @return a Props for creating this actor, which can then be further configured
     *         (e.g. calling `.withDispatcher()` on it)
     */
    public static Props props(final int id, final Optional<PVector> location, final Optional<PVector> veloctity) {
        return Props.create(new Creator<ParticleActor>() {
            private static final long serialVersionUID = 1L;

            @Override
            public ParticleActor create() throws Exception {
                return new ParticleActor(id, location, veloctity);
            }
        });
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        if ( message instanceof TimePassedMessage ) {
            this.step();
            getSender().tell(new ParticleMessages.ParticleMovedMessage(getThisParticle()), getSelf());
        } else if ( message instanceof ParticleMessages.AddReactorMessage) {
            addReactor(((ParticleMessages.AddReactorMessage)message).getReactor(),
                    ((ParticleMessages.AddReactorMessage)message).getType());
        } else if (message instanceof ParticleMessages.ChangeLocationMessage) {
            this.location = ((ParticleMessages.ChangeLocationMessage)message).getNewLocation();
        } else if (message instanceof FlockActor.WelcomeToFlockMessage) {
            ourFlock = Optional.of(((FlockActor.WelcomeToFlockMessage)message).getFlockActor());
        }

        System.out.println("Got message " + message.getClass().getCanonicalName());
    }


    public Map<Reactor.reactionType, Reactor> getReactors() {
        return reactors;
    }

    public PVector getLocation() {
        return location;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public int getId() {
        return id;
    }

    public Optional<ActorRef> getOurFlock() {
        return ourFlock;
    }

    private ParticleActor (int id, Optional<PVector> location, Optional<PVector> velocity) {
        this.id = id;
        this.location = location.orElse(this.location);
        this.velocity = location.orElse(this.velocity);
        this.reactors = new HashMap<>();
    }

    private Particle getThisParticle() {
        return new Particle(
                this.id,
                this.location,
                this.velocity,
                this.reactors
        );
    }

    private Reactor addReactor(Reactor r, Reactor.reactionType type) {
        return reactors.put(type,r);
    }

    private void step() {
        this.location.add(this.velocity);

    }

    public static class Particle {
        final int id;
        final PVector location;
        final PVector velocity;
        final Map<Reactor.reactionType, Reactor> reactors;
//        final int neighborhoodBound;
//
        public Particle (int id, PVector location, PVector velocity, Map<Reactor.reactionType, Reactor> reactors) {
            this.id = id;
            this.location = location;
            this.velocity = velocity;
            this.reactors = reactors;

//            // herehere find the max for the neighborhood bounds
//            for ( Reactor r: reactors.values().stream().max) {
//                neighborhoodBound = r.getReactionBound() > neighborhoodBound ? r.getReactionBound() : neighborhoodBound;
//            }
        }

    }
}
