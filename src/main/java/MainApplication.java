import actor.QuizActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainApplication {
    public static void main(String[] args) throws Exception {
        ActorSystem rootActor = ActorSystem.create("ParliamentSimulator");

        List<String> questions = generateQuestions();
        ActorRef quizActor = rootActor.actorOf(new Props(QuizActor.class));

        for(String question : questions){
            quizActor.tell(question, quizActor);
            Thread.sleep(1000L);
        }

        rootActor.shutdown();
    }

    private static List<String> generateQuestions() throws Exception {
        return new MainApplication().parseJsonFile("data.json");
    }

    public List<String> parseJsonFile(String filename) throws IOException, ParseException, URISyntaxException {
        JSONParser parser = new JSONParser();
        List<String> urlList = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource(filename).toURI());
        File f = path.toFile();

        JSONArray jsonArray = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(f)));

        for (Object o : jsonArray) {
            JSONObject currentUrl = (JSONObject) o;
            urlList.add(currentUrl.get("question").toString());
        }
        System.out.println(urlList.size() + " intrebari incarcate din fisier: " + filename);
        return urlList;
    }

}
