package me.jakerg.rougelike;

import java.util.ArrayList;

public class Log {
    private ArrayList<Message> messages;
    private int maxMessages;

    public Log(int max) {
        this.maxMessages = max;
        messages = new ArrayList<>(max);
    }

    public ArrayList<Message> messages() {
        return messages;
    }

    public int max() {
        return maxMessages;
    }

    public void addMessage(String m) {
        overflow();
        System.out.println("[LOG] : " + m);
        messages.add(0, new Message(m));
    }

    private void overflow() {
        if (messages.size() + 1 > max())
            messages.remove(messages.size() - 1);
    }

    public void clear() {
        messages().clear();
    }
}
