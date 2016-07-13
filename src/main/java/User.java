import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nigel on 7/11/16.
 */
public class User {
    private String name;
    private String password;
    private List<Message> messageList = new ArrayList<>();

    public User(String name, String password) {
        this.password = password;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List getMessageList() {
        return messageList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addMessage(Message message) {
        this.messageList.add(message);
    }

    public void addMessage (String message){
        Message input = new Message(message);
        this.messageList.add(input);
    }

    public void deleteMessage (int index){
        messageList.remove(index-1);
    }

    public void editMessage (int index, Message edit){
        messageList.remove(index-1);
        messageList.add(index-1, edit);
    }
}
