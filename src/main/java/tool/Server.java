package tool;

import com.google.gson.*;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * User: eptwalabha
 * Date: 21/01/14
 * Time: 14:11
 */
public class Server {
    
    private WebClient webClient;

    private String address;
    private String groupID;
    private String protocol;
    private int port;

    private TaskResponse lastTaskResponse;

    private Response currentQuestion;
    private Response lastResponseResult;

    private ServerService serviceToFetchQuestion;

    public Server(ServerService serviceToFetchQuestion) {
        this.serviceToFetchQuestion = serviceToFetchQuestion;
    }

    public void setConnection(String address, String protocol, int port, String groupID) {
        this.address = address;
        this.protocol = protocol;
        this.port = port;
        this.groupID = groupID;
    }

    public String getAddress() {
        return address;
    }

    public String getCompleteAddress() {
        return protocol + "://" + address + ":" + port + "/";
    }

    public void fetchNextQuestion() {
        DataToFetchQuestion dataToFetchQuestion = new DataToFetchQuestion();

        // TODO certain élément sont à commenter
        // dans le cas où les paramètres doivent être passé en json
        dataToFetchQuestion.setNoQuestion(0);
        dataToFetchQuestion.setTokenQuestion("123456789");
        dataToFetchQuestion.setGroupID(groupID);

        webClient = this.createWebClient();
        webClient.path(serviceToFetchQuestion.getServicePath() + "/" + dataToFetchQuestion.getNoQuestion());

        // TODO certain élément sont à commenter
        // dans le cas où les paramètres doivent être passé dans le header
        webClient.type(MediaType.APPLICATION_JSON);
        webClient.header("token", this.getNextToken());
        webClient.header("no-question", 0);
        webClient.header("group-id", groupID);

        webClient.invoke(serviceToFetchQuestion.getHttpMethod(), new Gson().toJson(dataToFetchQuestion));

        currentQuestion = webClient.getResponse();
    }

    public void sendResponseToQuestion(TaskResponse taskResponse) throws NonValidAnswer {

        ServerService serverService = taskResponse.getRelatedService();
        webClient = this.createWebClient();
        webClient.path(serverService.getServicePath());

        // le webservice donne quelques infos dans le header
        serverService.buildHeaderWebClient(webClient);
        taskResponse.buildHeaderWebClient(webClient);

        webClient.invoke(serverService.getHttpMethod(), taskResponse.getBodyWebClient());

        lastTaskResponse = taskResponse;
        lastResponseResult = webClient.getResponse();

        taskResponse.callBack(lastResponseResult);
    }

    public boolean isLastResponseValid() throws JsonParseException {

        String json = lastResponseResult.getEntity().toString();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        if (!element.isJsonObject() || element.isJsonNull())
            throw new JsonParseException("erreur parsing aux résultats de la question n°" + lastTaskResponse.getId());

        return element.getAsJsonObject().get("valide").toString().equals("ok");
    }

    public String getNextToken() throws JsonParseException {

        if (lastResponseResult == null)
            return "";

        String json = lastResponseResult.getEntity().toString();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        if (!element.isJsonObject() || element.isJsonNull())
            throw new JsonParseException("erreur parsing aux résultats de la question n°" + lastTaskResponse.getId());

        return element.getAsJsonObject().get("token").toString();
    }
    
    private WebClient createWebClient() {
        WebClient client = WebClient.create(this.getCompleteAddress());
        ClientConfiguration config = WebClient.getConfig(client);
        config.getInInterceptors().add(new LoggingInInterceptor());
        config.getOutInterceptors().add(new LoggingOutInterceptor());
        return client;
    }

    public JsonObject getDataFromQuestion() throws JsonParseException {
        String json = currentQuestion.getEntity().toString();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        if (element.isJsonNull())
            return new JsonObject();

        if (!element.isJsonObject())
            throw new JsonParseException("problème avec la question!");

        return element.getAsJsonObject();
    }

}
