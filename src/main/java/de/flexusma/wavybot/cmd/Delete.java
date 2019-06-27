package de.flexusma.wavybot.cmd;


import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.EmbededBuilder;
import de.flexusma.wavybot.utils.Preferences;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Delete extends Command {
    Preferences pref;

    public Delete() {
        this.name = "delete";
        this.help = "deletes messages by id or count\n" +
                "usage: <prefix>delete <number|message id>";

        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        System.out.println(commandEvent.getArgs());
        JDA jda = commandEvent.getJDA();
        pref = Database.initPref(jda,commandEvent.getGuild().getId());
        long id = 0;
        try {
            id = Long.parseLong(commandEvent.getArgs());
        } catch (NumberFormatException e) {
            commandEvent.reply("Error, see usage:\n" +
                    this.help);
        }

        if (id < 99) {
            id++;
            List<Message> msg = commandEvent.getTextChannel().getHistory().retrievePast((int) id).complete();
            commandEvent.getTextChannel().deleteMessages(msg).queue();
            id--;
            commandEvent.replySuccess("Deleted last " + id + " messages!");



        } else {
            commandEvent.getTextChannel().deleteMessageById(id).queue();


            commandEvent.reply("Message deleted!");
            commandEvent.reply(EmbededBuilder.create(commandEvent.getClient().getSuccess()+"Success","This is a test", Color.cyan).build());
            Bot.autoDelete(commandEvent,pref);
        }



    }
}
