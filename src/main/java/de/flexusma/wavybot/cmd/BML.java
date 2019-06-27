package de.flexusma.wavybot.cmd;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.MPlayer;
import de.flexusma.wavybot.utils.Preferences;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;

public class BML extends Command {
    Preferences pref;
    public BML(){
        this.name = "bml";
        this.help = "plays Bruder muss los in your current channel\n" +
                "usage: <prefix>bml";

        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE,Permission.VOICE_SPEAK,Permission.VOICE_CONNECT,Permission.VOICE_USE_VAD};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        pref = Database.initPref(jda,event.getGuild().getId());

        MPlayer player = Bot.player;
        if(event.getMember().getVoiceState().getChannel()!=null)

            player.loadAndPlay(event.getTextChannel(),"https://www.youtube.com/watch?v=1MLdOBh9jOk", event.getMember().getVoiceState().getChannel().getId(),event);

        else event.replyError("Error, you have to be in a voice channel to do that ^^");

        Bot.autoDelete(event,pref);
    }
}
