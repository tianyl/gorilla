package fi.utu.tech.distributed.gorilla.logic;

/**
 * TODO: make compatible with network play
 */
public final class ChatMessage {
    public final String sender;
    public final String recipient;
    public final String contents;

    public ChatMessage(String sender, String recipient, String contents) {
        this.sender = sender;
        this.recipient = recipient;
        this.contents = contents;
    }
}
