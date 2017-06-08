package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.HelpGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "HELP - A Grandma Odyssey";
        config.addIcon("HelpIcon.png", Files.FileType.Internal);

        LwjglApplication lwjglApplication = new LwjglApplication(new HelpGame(), config);
    }
}
