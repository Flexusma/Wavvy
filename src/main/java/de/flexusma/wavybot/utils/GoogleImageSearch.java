package de.flexusma.wavybot.utils;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class GoogleImageSearch {



    public static String search(String title) {
        title = title.replaceAll(" ", "+");
        try {
            URL url = new URL("https://www.googleapis.com/customsearch/v1?key=" + BotToken.apikey + "&cx=" + BotToken.cx + "&q=" + title + "&searchType=image&fileType=png,jpg&alt=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            GResults results = new Gson().fromJson(br, GResults.class);
            conn.disconnect();

            return results.getLink(0);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

//api
   //cx
}

