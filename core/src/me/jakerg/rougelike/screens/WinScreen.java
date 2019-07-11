package me.jakerg.rougelike.screens;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;


import asciiPanel.AsciiPanel;
import edu.southwestern.tasks.gvgai.zelda.dungeon.Dungeon;
import me.jakerg.rougelike.TitleUtil;

public class WinScreen implements Screen {
	
	Dungeon d;
	
	public WinScreen() {
		d = Dungeon.loadFromJson("data/rouge/tmp/dungeon.json");
		try {
			FileUtils.deleteDirectory(new File("data/rouge/tmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 public void displayOutput(AsciiPanel terminal) {
        try {
        	int y = 10;
			List<String> title = TitleUtil.loadTitleFromFile("data/rouge/titles/win.txt");
			for(String line : title) {
				System.out.println(line);
				terminal.write(line, 27, y++);
			}
			terminal.writeCenter("You got the triforce!", y + 1);
			terminal.writeCenter("Press [enter] to start again", y + 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new DungeonScreen(d) : this;
    }

}
