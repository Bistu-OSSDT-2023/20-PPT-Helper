import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    private String text;

    public Message() {}

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}