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

public class Queue extends Command {
    Preferences pref;

    public Queue(){
        this.name = "queue";
        this.help = "queue commands:\n" +
                    "queue list - shows songs in playlist\n"+
                    "queue clear - clears the playlist\n"+
                    "queue now - shows current song";


        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE,Permission.VOICE_SPEAK,Permission.VOICE_CONNECT,Permission.VOICE_USE_VAD};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        pref = Database.initPref(jda,event.getGuild().getId());
        MPlayer player = Bot.player;
        if(event.getArgs().equals("clear")){

            player.clearQueue(event.getGuild().getAudioManager(),event.getTextChannel(),event);
        }else if(event.getArgs().equals("list")){
            List<MessageEmbed.Field> f = player.listQueue(event.getGuild().getAudioManager(),event.getTextChannel());
            event.reply(EmbededBuilder.create("Current playlist", "I've listed all current songs in the playlist", Color.green,f).build());

        }else if(event.getArgs().equals("now")){
            player.nowQueue(event.getGuild().getAudioManager(),event.getTextChannel());
        }

        Bot.autoDelete(event,pref);
    }
}
