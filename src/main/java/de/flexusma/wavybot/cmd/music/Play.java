package de.flexusma.wavybot.cmd.music;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.MPlayer;
import de.flexusma.wavybot.utils.Preferences;
import de.flexusma.wavybot.utils.TrackScheduler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;

public class Play extends Command {
AudioPlayer player = TrackScheduler.playerManager.createPlayer();

Preferences pref;

    public Play(){
        this.name = "play";
        this.help = "plays the given URL\n" +
                "usage: <prefix>play <url>";

        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE,Permission.VOICE_SPEAK,Permission.VOICE_CONNECT,Permission.VOICE_USE_VAD};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        pref = Database.initPref(jda, event.getGuild().getId());

        MPlayer player = Bot.player;
        if (event.getMember().getVoiceState().getChannel() != null)
            if (!event.getArgs().equals("") && !event.getArgs().equals(" ")){
                player.loadAndPlay(event.getTextChannel(), event.getArgs(), event.getMember().getVoiceState().getChannel().getId(), event);
        player.setVolume(pref.getVolume(), event.getTextChannel());
    }
            else event.replyError("Error, please specify a link to play from or type: \n"+
                    "```"+pref.getPrefix()+name+"``` - "+help);
        else event.replyError("Error, you have to be in a voice channel to do that ^^");

        Bot.autoDelete(event,pref);
    }
}
