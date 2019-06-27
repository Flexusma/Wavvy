package de.flexusma.wavybot.utils;

import com.google.gson.Gson;
import net.dv8tion.jda.bot.sharding.DefaultShardManager;
import net.dv8tion.jda.core.entities.Guild;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GenW2GLink {

    public static String genStreamKey(String media){

        try {
            URL url = new URL("https://www.watch2gether.com/rooms/create.json");
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url.toString());

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            if(media!="")
            pairs.add(new BasicNameValuePair("share", media));
            pairs.add(new BasicNameValuePair("api_key", BotToken.w2gkey));
            request.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = client.execute(request);


            BufferedReader br = new BufferedReader(new InputStreamReader((resp.getEntity().getContent())));
            W2Res g = new Gson().fromJson(br,W2Res.class);
            System.out.println(br.readLine());
            System.out.println(g.getStreamkey());


            return g.getStreamkey();

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }

}
