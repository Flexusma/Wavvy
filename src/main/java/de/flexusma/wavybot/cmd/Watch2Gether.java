package de.flexusma.wavybot.cmd;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Watch2Gether extends Command {
    Preferences pref;
    public Watch2Gether(){
        this.name = "w2g";
        this.aliases = new String[]{"wtg","watch2gether","watchtogether"};
        this.help = "creates a new Watch2gether room\n" +
                "usage: <prefix>wtg <media/video link/\"\">";

        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE,Permission.MESSAGE_MANAGE};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        pref = Database.initPref(jda,event.getGuild().getId());

        String key =GenW2GLink.genStreamKey(event.getArgs());
        List<MessageEmbed.Field> f = new ArrayList<>();
        f.add(new MessageEmbed.Field("Watch2gether room:","https://www.watch2gether.com/rooms/"+key,false));
        event.reply(EmbededBuilder.create("Watch2gether room created", "Your custom watch2gether room was successfully created, if you specified a media link before, the video/media should already be loaded", Color.green,f).build());

        Bot.autoDelete(event,pref);
    }
}
