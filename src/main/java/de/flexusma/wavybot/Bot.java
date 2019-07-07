package de.flexusma.wavybot;


import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.src.main.java.com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import com.jagrosh.jdautilities.examples.src.main.java.com.jagrosh.jdautilities.examples.command.PingCommand;
import de.flexusma.wavybot.cmd.*;
import de.flexusma.wavybot.cmd.music.*;
import de.flexusma.wavybot.utils.*;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.entities.Game;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.FileReader;
import java.io.IOException;

public class Bot {

    public static ShardManager sm;

    Preferences pref;
    public static MPlayer player;
    public static void main(String[] args) {
        new Bot();
    }
    JSONParser parse = new JSONParser();

    private Bot() {


        
        try {
            JSONObject details = (JSONObject) parse.parse(new FileReader("config.json"));




            String token = (String) details.get("token");
            System.out.println("Token "+token);
            BotToken.token=token;

            String w2gkey = (String) details.get("w2gkey");
            System.out.println(w2gkey);
            BotToken.w2gkey=w2gkey;

            String cx = (String) details.get("cx");
            System.out.println(cx);
            BotToken.cx=cx;

            String apikey = (String) details.get("apikey");
            System.out.println(apikey);
            BotToken.apikey=apikey;

            String url = (String) details.get("url");
            System.out.println(url);
            BotToken.url=url;

            String user = (String) details.get("user");
            System.out.println(user);
            BotToken.user=user;

            String password = (String) details.get("password");
            System.out.println(password);
            BotToken.password=password;

            String ytapikey= (String) details.get(("ytapikey"));
            System.out.println(ytapikey);
            BotToken.ytapikey=ytapikey;



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        

        EventWaiter waiter = new EventWaiter();
        Database db = new Database();

        // define a command client
        CommandClientBuilder client = new CommandClientBuilder();

        // The default is "Type !!help" (or whatver prefix you set)
        client.useDefaultGame();

        // sets the owner of the bot
        client.setOwnerId("442275267545792522");

        // sets emojis used throughout the bot on successes, warnings, and failures
        client.setEmojis(/*"\uD83D\uDE03" "✔️"*/ "✅", "⚠️", "❌");

        // sets the bot prefix

        client.setPrefix("!");


        // adds commands
        client.addCommands(
                // command to show information about the bot
                /* new AboutCommand(Color.BLUE, "an example bot",
                         new String[]{"Cool commands","Nice examples","Lots of fun!"},
                         new Permission[]{Permission.ADMINISTRATOR}
                         ),*/


                // command to check bot latency
                new PingCommand(),
                new About(),
                new Delete(),
                new Prefix(),
                new Play(),
                new SkipTrack(),
                //new ClearQueue(),
                new StopPlayback(),
                new Resume(),
                new Pause(),
                new AutoDelete(),
                new DWK(),
                new Oof(),
                new BML(),
                new Watch2Gether(),
                new Queue(),
                new Volume()
        );


        db.initDB();
        TrackScheduler.registerManager();

        player = new MPlayer();
        try {
            sm = new DefaultShardManagerBuilder()//AccountType.BOT) //JDA als Bot instanzieren
                    .setToken(BotToken.token) //Bot-Token
                    .setGame(Game.playing("loading..."))
                    .addEventListeners(waiter,client.build())
                    .build();







        } catch (LoginException e) {
            e.printStackTrace();
        }
    }


    public static void autoDelete(CommandEvent event, Preferences pref){
        if(pref.isAutoDelete())
            event.getMessage().delete().queue();
    }




}
