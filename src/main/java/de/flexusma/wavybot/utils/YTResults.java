package de.flexusma.wavybot.utils;

import java.util.List;

public class YTResults {

  Data data;
}
class Data{
    List<Item> items;
}

class Item{
    List<Snippet> s;
}

class Snippet{
    String channelId;
    String title;
    List<Thumbnails> th;
}
class Thumbnails{
    List<Default> defaults;
}
class Default{
    String url;
}
