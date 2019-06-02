package actor;

import java.util.Arrays;
import java.util.List;

public interface AnswerType {
    String YES = "YES";
    String NO  = "NO";
    String ABSTENTION = "ABSTENTION";
    List<String> ALL_ANSWERS = Arrays.asList(YES, YES, YES, YES,
                                             NO, NO, NO, NO,
                                             ABSTENTION, ABSTENTION);
}
