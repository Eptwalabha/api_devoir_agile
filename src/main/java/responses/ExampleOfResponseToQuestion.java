package responses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.cxf.jaxrs.client.WebClient;
import tool.ServerService;
import tool.TaskResponse;

import javax.ws.rs.core.Response;

/**
 * User: eptwalabha
 * Date: 24/01/14
 * Time: 01:18
 */
public class ExampleOfResponseToQuestion extends TaskResponse {

    private LangueWow dataResponse;

    public ExampleOfResponseToQuestion(ServerService serverService) {
        super(serverService);
        this.dataResponse = new LangueWow();
    }

    @Override
    public Object getBodyWebClient() {
        Gson gson = new Gson();
        return gson.toJson(this.dataResponse);
    }

    @Override
    public void buildHeaderWebClient(WebClient webClient) {
        webClient.header("hello", "world");
    }

    @Override
    public void process(JsonObject jsonObject) {
        dataResponse.setLocale("fr_FR");
    }

    @Override
    public void callBack(Response result) {
        System.out.println(result.getEntity().toString());
    }

    class LangueWow {
        private String locale;

        public void setLocale(String locale) {
            this.locale = locale;
        }
    }
}
