package org.blissboy.particlesystem;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import org.blissboy.particlesystem.actors.FlockActor;
import org.blissboy.particlesystem.actors.ParticleActor;
import org.blissboy.particlesystem.messages.ParticleMessages;
import org.blissboy.particlesystem.messages.TimePassedMessage;
import processing.core.PApplet;
import processing.core.PVector;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by patwheaton on 7/30/16.
 */
public class ParticleSystem extends PApplet {

    private int WIDTH = 1920;
    private int HEIGHT = 1080;

    static final int NUM_PARTICLES = 10;
    public static final int FOLLOW_DISTANCE = 50;
    public static final float FOLLOW_FORCE = 2f;

    public static final int COHESE_DISTANCE = 20;
    public static final float COHESE_FORCE = 2f;

    public static final float MAX_STEER_FORCE = .11f;

    ActorSystem system;
    Inbox inbox;
    ActorRef flock;


    public ParticleSystem() {
        super();
    }

    public void settings() {
        size(WIDTH,HEIGHT);
    }


    @Override
    public void setup() {
        system = ActorSystem.create("ParticleSystem");
        inbox = Inbox.create(system);

        flock = system.actorOf(Props.create(FlockActor.class), "flock");
        ActorRef particle;
        for (int pId = 0; pId < NUM_PARTICLES; pId++) {
            particle = system.actorOf(ParticleActor.props(pId, Optional.of(PVector.random2D()), Optional.of(PVector.random2D())));
            inbox.send(flock, new ParticleMessages.WantToJoinMessage(particle));
        }

    }

    @Override
    public void draw() {
        FlockActor.FlockParticleStatusMessage m =
                null;
        try {
            m = (FlockActor.FlockParticleStatusMessage)(inbox.receive(Duration.create(100, TimeUnit.MILLISECONDS)));

        m.getParticles().parallelStream().forEach(
                (ParticleActor.Particle p) -> {
                    System.out.println(p);

                });
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        TimePassedMessage timeMessage = new TimePassedMessage(System.currentTimeMillis());
        inbox.send(flock,timeMessage);

    }

    public static void main(String[] args) {


        String[] appletArgs = new String[]{ParticleSystem.class.getCanonicalName()};
        if (args != null) {
            PApplet.main(concat(appletArgs, args));
        } else {
            PApplet.main(appletArgs);
        }



//        try {
//            // Create the 'helloakka' actor system
//            final ActorSystem system = ActorSystem.create("helloakka");
//
//            Map<Integer, ActorRef> particles = new HashMap<>();
//            Inbox inbox = Inbox.create(system);
//
//            ActorRef flock = system.actorOf(Props.create(FlockActor.class), "flock");
//            ActorRef particle;
//            for (int pId = 0; pId < NUM_PARTICLES; pId++) {
//                particle = system.actorOf(ParticleActor.props(pId, Optional.of(PVector.random2D()), Optional.of(PVector.random2D())));
//                inbox.send(flock, new ParticleMessages.WantToJoinMessage(particle));
//            }

//            boolean go = true;
//            while (go) {

        TimePassedMessage timeMessage = new TimePassedMessage(System.currentTimeMillis());
//                particles.keySet().stream()
//                        .forEach( (id) -> {
//                         particles.get(id).tell(timeMessage, inbox.getRef());
//
//                });
//                inbox.send(flock,timeMessage);
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//
//                }
//
//
//            }
//
//        } catch (Exception ex) {
//            System.out.println("Got a timeout waiting for reply from an actor");
//            ex.printStackTrace();
//        }
    }


}
