package de.flexusma.wavybot.cmd.music;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.EmbededBuilder;
import de.flexusma.wavybot.utils.MPlayer;
import de.flexusma.wavybot.utils.Preferences;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.List;

public class Volume extends Command {

    Preferences pref;

    public Volume(){
        this.name = "volume";
        this.help = "changes the volume of the bot\n"+
        "usage: <prefix>volume <int>";


        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE,Permission.VOICE_SPEAK,Permission.VOICE_CONNECT,Permission.VOICE_USE_VAD};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        pref = Database.initPref(jda,event.getGuild().getId());
        MPlayer player = Bot.player;
        int i = pref.getVolume();
        try {
            i=Integer.parseInt(event.getArgs());
            player.setVolume(i,event.getTextChannel());
            pref.setVolume(i);
            Database.savePref(jda,event.getGuild().getId(),pref);
            event.replySuccess("Successfully changed the volume to: "+"```"+i+"```");
        }catch (NumberFormatException e){
            event.replyError("Please type a number without comma as an argument");
        }

        Bot.autoDelete(event,pref);
    }
}
