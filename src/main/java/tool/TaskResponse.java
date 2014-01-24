package tool;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.Response;

/**
 * User: eptwalabha
 * Date: 23/01/14
 * Time: 12:14
 */
public abstract class TaskResponse {


    private final ServerService serverService;
    private int noQuestion;
    private static int id = 0;

    public TaskResponse(ServerService serverService) {
        noQuestion = id++;
        this.serverService = serverService;
    }

    public int getId() {
        return noQuestion;
    }

    public ServerService getRelatedService() {
        return this.serverService;
    }

    public abstract Object getBodyWebClient();
    public abstract void buildHeaderWebClient(WebClient webClient);
    public abstract void process(JsonObject jsonObject);
    public abstract void callBack(Response result);
}
