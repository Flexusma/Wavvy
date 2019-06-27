package de.flexusma.wavybot.cmd.music;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.MPlayer;
import de.flexusma.wavybot.utils.Preferences;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;

public class ClearQueue extends Command {
    Preferences pref;
    public ClearQueue(){
        this.name = "clearqueue";
        this.help = "stops mucis playback and clear playback queue\n" +
                "usage: <prefix>clearqueue";

        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE,Permission.VOICE_SPEAK,Permission.VOICE_CONNECT,Permission.VOICE_USE_VAD};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        pref = Database.initPref(jda,event.getGuild().getId());

        MPlayer player = Bot.player;
        player.clearQueue(event.getGuild().getAudioManager(),event.getTextChannel());

        Bot.autoDelete(event,pref);
    }
}
