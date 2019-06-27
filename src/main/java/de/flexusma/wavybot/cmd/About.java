package de.flexusma.wavybot.cmd;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import de.flexusma.wavybot.Bot;
import de.flexusma.wavybot.utils.Database;
import de.flexusma.wavybot.utils.EmbededBuilder;
import de.flexusma.wavybot.utils.Preferences;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class About extends Command {
    Preferences pref ;


    public About(){
        this.name = "about";
        this.help = "gives Information about the bot\n" +
                "usage: <prefix>about\n";

        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly = false;
    }
    @Override
    protected void execute(CommandEvent event) {
        int shardid = event.getJDA().getShardInfo().getShardId();
        System.out.println(event.getJDA().getShardInfo().getShardString());
        JDA jda = event.getJDA();
        try {
            pref = Database.initPref(jda, event.getGuild().getId());
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        List<MessageEmbed.Field> fld = new ArrayList<>();

        for(Command c : event.getClient().getCommands()){
            fld.add(new MessageEmbed.Field("```"+c.getName()+"```",c.getHelp(),false));
        }

        event.reply(EmbededBuilder.create("WavvyBot","Hello! I am Wavvy, an example bot\n" +
                "Type !help to see my commands!\n" +
                "If you want to use me on your server, go ahead here comes an invite link:\n" +
                "```" +"https://discordapp.com/oauth2/authorize?client_id="+Bot.sm.getShardById(shardid).getSelfUser().getId()+"&scope=bot&permissions=8"+"```"+
                "\n The current prefix is: "+ "```"+pref.getPrefix()+"```" , Color.CYAN, "https://flexusma.de/imag/wave.png",fld).build());

        Bot.autoDelete(event,pref);
    }
}
