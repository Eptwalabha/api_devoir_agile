package tool;

import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

/**
 * User: eptwalabha
 * Date: 24/01/14
 * Time: 00:43
 */
public class ServerService {

    private String servicePath;
    private String httpMethod;

    public ServerService(String servicePath, String httpMethod) {
        this.servicePath = servicePath;
        this.httpMethod = httpMethod;
    }

    public String path(String path) {
        return servicePath + path;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void buildHeaderWebClient(WebClient webClient) {
        webClient.type(MediaType.APPLICATION_JSON);
    }

    public String getHttpMethod() {
        return httpMethod;
    }
}
