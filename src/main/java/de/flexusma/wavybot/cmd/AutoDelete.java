package de.flexusma.wavybot.cmd;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.MPlayer;
import de.flexusma.wavybot.utils.Preferences;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;

public class AutoDelete extends Command {
    Preferences pref;
    public AutoDelete(){
        this.name = "autodelete";
        this.help = "Enables / Disables automatic command message deletion / Shows current status if no arguments given\n" +
                "usage: <prefix>autodelete <on/off/\"\">";

        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_READ};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        pref = Database.initPref(jda,event.getGuild().getId());

        if(event.getArgs().equals("on")){
            pref.setAutoDelete(true);
            event.replySuccess("Ok, I'm going to delete all command messages from now on! *Shred*");
            event.reply("https://media.giphy.com/media/l0IyjK57IEerH0xMc/giphy.gif");

        }else if (event.getArgs().equals("off")){
            pref.setAutoDelete(false);
            event.replySuccess("Ok, I'm NOT going to delete command messages from now on!");
            event.reply("https://media.giphy.com/media/hKfyPlnNlZxF6/giphy.gif");
        }else if(event.getArgs().equals("")|| event.getArgs().equals(null)) {
            event.replySuccess("Autodelete is currently "+"```"+pref.isAutoDelete()+"```\n"+
                    "(you can also just check if your command gets deleted :P)");
        }
        Database.savePref(jda,event.getGuild().getId(),pref);

        Bot.autoDelete(event,pref);

    }
}
