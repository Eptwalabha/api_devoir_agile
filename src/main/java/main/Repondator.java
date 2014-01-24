package main;

import responses.ExampleOfResponseToQuestion;
import tool.Server;
import tool.ServerService;
import tool.TaskResponse;
import tool.NonValidAnswer;

import javax.ws.rs.HttpMethod;
import java.util.ArrayList;

/**
 * User: eptwalabha
 * Date: 23/01/14
 * Time: 18:50
 */
public class Repondator {

    public static void main(String[] arg0) {

        // TODO renseigne un des services
        ServerService serviceGetQuestion = new ServerService("/question", HttpMethod.GET);

        Server server = new Server(serviceGetQuestion);

        // TODO renseigner les données serveur et l'identifiant du groupe
        server.setConnection("eu.battle.net", "http", 80, "idGroupe");

        // TODO retourner la liste des réponses
        ArrayList<TaskResponse> listOfResponses = prepareQuestions();

        // on récupère la première question
        server.fetchNextQuestion();
        boolean allCorrect = false;

        try {

            for (TaskResponse response : listOfResponses) {

                response.process(server.getDataFromQuestion());
                server.sendResponseToQuestion(response);

                if (!server.isLastResponseValid())
                    break;

                // si la réponse est juste, on récupère la question suivante
                server.fetchNextQuestion();
            }

            allCorrect = true;

        } catch (NonValidAnswer nonValidAnswer) {
            nonValidAnswer.printStackTrace();
        }

        if (allCorrect)
            System.out.println("tout juste pour le moment!");

    }

    private static ArrayList<TaskResponse> prepareQuestions() {

        ArrayList<TaskResponse> listOfResponses = new ArrayList<TaskResponse>();

        // TODO renseigner les services existants
        ServerService serviceWowListeRace = new ServerService("/api/wow/data/character/races", HttpMethod.GET);

        // Question 1
        TaskResponse reponse_01 = new ExampleOfResponseToQuestion(serviceWowListeRace);
        listOfResponses.add(reponse_01);

        // Question 2 ...

        return listOfResponses;
    }
}
