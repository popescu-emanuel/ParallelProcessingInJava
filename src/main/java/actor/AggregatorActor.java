package actor;

import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregatorActor extends UntypedActor {

    public void onReceive(Object message) {
        if (message instanceof List) {
            List<String> answers = (List<String>) message;
            Map<String, Integer> numberOfAnswersPerType = new HashMap<>();

            int numberOfVotes = answers.size();

            for(String answerType : AnswerType.ALL_ANSWERS){
                numberOfAnswersPerType.put(answerType, 0);
            }

            answers.forEach(answer -> {
                Integer currentValue = numberOfAnswersPerType.get(answer);
                numberOfAnswersPerType.put(answer, currentValue + 1);
            });


            int abstentions  = numberOfAnswersPerType.get(AnswerType.ABSTENTION);
            int votesFor     = numberOfAnswersPerType.get(AnswerType.YES);
            int votesAgainst = numberOfAnswersPerType.get(AnswerType.NO);

            String displayVotes = String.format("Pentru: %d | Contra: %d | Abtineri: %d", votesFor, votesAgainst, abstentions);
            System.out.println(displayVotes);

            if(numberOfAnswersPerType.get(AnswerType.ABSTENTION) >= numberOfVotes/2){
                System.out.println("[ANULAT] Numarul de abtineri a fost prea ridicat!");
                return;
            }

            if(votesFor > votesAgainst){
                System.out.println("[ACCEPTAT]");
            } else {
                System.out.println("[REFUZAT]");
            }
        } else {
            unhandled(message);
        }
    }

}
