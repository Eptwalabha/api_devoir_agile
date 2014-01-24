package tool;

/**
 * User: eptwalabha
 * Date: 24/01/14
 * Time: 01:51
 */
public class DataToFetchQuestion {

    private String token;
    private int noQuestion;
    private String groupID;

    public DataToFetchQuestion() {
        // !! IMPORTANT !! Il ne faut rien mettre dans les objets que vous créez! sinon Gson ne peut pas parser l'objet en Json.
    }

    public void setTokenQuestion(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setNoQuestion(int noQuestion) {
        this.noQuestion = noQuestion;
    }

    public int getNoQuestion() {
        return noQuestion;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupID() {
        return groupID;
    }
}
