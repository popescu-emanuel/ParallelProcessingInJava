package actor;

import akka.actor.UntypedActor;

import java.util.Random;

public class VoterActor extends UntypedActor {

    private final String name;

    public VoterActor(String name) {
        this.name = name;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            try{
                Random r = new Random();
                int answerIndex = r.nextInt(AnswerType.ALL_ANSWERS.size());
                String answer = AnswerType.ALL_ANSWERS.get(answerIndex);
                getSender().tell(answer, getSelf());
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            unhandled(message);
        }
    }
}
