package me.jakerg.rougelike.screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * Helper interface to have multiple screen with the same functionality, used for the main app
 *
 * @author gutierr8
 */
public interface Screen {
    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);
}
