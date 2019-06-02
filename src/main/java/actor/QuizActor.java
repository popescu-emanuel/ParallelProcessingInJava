package actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;



public class QuizActor extends UntypedActor {

    private final Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));
    private final static int NUMBER_OF_VOTERS = 300;

    private ActorRef aggregator = this.getContext().actorOf(new Props(AggregatorActor.class));
    private List<ActorRef> voters;

    @Override
    public void preStart() {
        voters = generateVoters();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {

            String question = (String) message;
            final ArrayList<Future<Object>> answers = new ArrayList<Future<Object>>();

            for(ActorRef voter : voters){
                answers.add(ask(voter, question, t));
            }

            System.out.println("\nIntrebarea este: " + question);
            System.out.println("Numar votanti " + answers.size());

            final Future<Iterable<Object>> aggregate = Futures.sequence(answers, getContext().system().dispatcher());

            pipe(aggregate, getContext().system().dispatcher()).to(aggregator);
        }
    }

    private List<ActorRef> generateVoters(){
        List<ActorRef> voters = new ArrayList<>();
        for (int i = 0; i < QuizActor.NUMBER_OF_VOTERS; i++) {
            String name = "voter" + i;
            ActorRef actorRef =  getContext().actorOf(Props.create(VoterActor.class, name), name);
            voters.add(actorRef);
        }
        return voters;
    }

}
