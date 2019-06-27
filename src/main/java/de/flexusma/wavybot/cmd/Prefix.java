package de.flexusma.wavybot.cmd;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.EmbededBuilder;
import de.flexusma.wavybot.utils.Preferences;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageEmbed;

import net.dv8tion.jda.core.hooks.ListenerAdapter;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Prefix extends Command {
    Preferences pref;

    public Prefix() {
        this.name = "prefix";
        this.help = "change the prefix of the bot\n" +
                "usage: <old prefix for example '!'>prefix <new prefix>\n";

        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        pref = Database.initPref(event.getJDA(), event.getGuild().getId());
        if (!event.getArgs().contains(" ") && !event.getArgs().equals("")&& !(event.getArgs()==null)) {
            System.out.println(event.getArgs() +" args");
            pref.setPrefix(event.getArgs());
            setGame(event,pref);
            Database.savePref(event.getJDA(), event.getGuild().getId(),pref);
            event.replySuccess("The prefix was successfully changed to:\n"+"```"+pref.getPrefix()+"```");
        }
        else {
            List<MessageEmbed.Field> l = new ArrayList<MessageEmbed.Field>();
            l.add(new MessageEmbed.Field("Not a valid prefix", "Prefixes cannot incluse \" \"  or be empty", false));

            event.reply(EmbededBuilder.create(event.getClient().getError()+"Error", "Please try again", Color.red, l).build());
        }
        Bot.autoDelete(event,pref);
    }

    public void setGame(CommandEvent event, Preferences pref){
        event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
                Game.playing("Type @"+ event.getJDA().getSelfUser().getName()+" "+event.getClient().getHelpWord()));
    }
}
