package de.flexusma.wavybot.utils;

import net.dv8tion.jda.core.JDA;

import java.io.Serializable;

public class Preferences implements Serializable {

    String Prefix = "~";
    boolean autoDelete = false;
    int volume = 100;


    public Preferences() {

    }


    //Setter and Getter


    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}

