package de.flexusma.wavybot.utils;

import com.google.api.services.youtube.model.SearchListResponse;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.BaseAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class MPlayer {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MPlayer() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager,guild);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(final TextChannel channel, final String trackUrl, String channelID, CommandEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());



        SearchListResponse res = YoutubeSearch.search(trackUrl);
        System.out.println("https://www.youtube.com/watch?v="+res.getItems().get(0).getId().getVideoId());


        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage(//"Adding to queue: " + track.getInfo().title
                        EmbededBuilder.create("Song added to queue", track.getInfo().title+"\n"+
                                "```"+trackUrl+"```", Color.green, GoogleImageSearch.search(track.getInfo().title)).build()).queue();

                play(channel.getGuild(), musicManager, track, channelID, event);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();



                channel.sendMessage(//"Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")"
                         EmbededBuilder.create("Adding to queue", playlist.getName() +"\n"+
                                 "```"+trackUrl+"```",Color.green).build()).queue();

                for(AudioTrack t : playlist.getTracks())
                play(channel.getGuild(), musicManager, t,channelID, event);
            }

            @Override
            public void noMatches() {
                if(res==null)
                channel.sendMessage(EmbededBuilder.create("Sorry, I did not find anything to play",
                        "```"+trackUrl+"```", Color.yellow).build()).queue();
                else {
                    String t = "https://www.youtube.com/watch?v="+res.getItems().get(0).getId().getVideoId();
                    loadAndPlay(channel,t,channelID,event);
                }
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage(EmbededBuilder.create("Bleep. bloop. Woosh~ Error!","Hey, semms like something went wrong, please check your url: \n"+
                        "```"+trackUrl+"```", Color.red).build()).queue();


            }
        });
    }



    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, String channelID,CommandEvent event) {
        //if(channelID.equals(""))
        //connectToFirstVoiceChannel(guild.getAudioManager());
       // else
            connectToVoiceChannelById(guild.getAudioManager(),channelID);

        musicManager.scheduler.queue(track,guild,event);

    }

    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        try {
            musicManager.scheduler.nextTrack();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.sendMessage("⏩ Skipped to next track.").queue();
    }

    public void stopPlayback(AudioManager audioManager, TextChannel channel){
        audioManager.closeAudioConnection();
        channel.sendMessage("Playback was stopped").queue();
        getGuildAudioPlayer(channel.getGuild()).scheduler.clearQueue();
    }

    public void clearQueue(AudioManager manager, TextChannel channel, CommandEvent e){

        getGuildAudioPlayer(channel.getGuild()).scheduler.clearQueue();
        manager.closeAudioConnection();

        channel.sendMessage("Queue Cleared").queue();
    }

    public List<MessageEmbed.Field> listQueue(AudioManager manager, TextChannel channel){
        if(manager.isConnected()) {
            BlockingQueue<AudioTrack> tr = getGuildAudioPlayer(channel.getGuild()).scheduler.getQueue();

            List<MessageEmbed.Field> f = new ArrayList<>();
            boolean b = false;
            for (AudioTrack t : tr) {
                if (b)
                    b = !b;
                f.add(new MessageEmbed.Field(t.getInfo().title, "```" + t.getInfo().uri + "```", b));

            }
            return f;
        }

        return null;

    }

    public void nowQueue(AudioManager manager, TextChannel channel){

        AudioTrack track = null;

            if(manager.isConnected())
                track = getGuildAudioPlayer(channel.getGuild()).scheduler.now;

        if(track!=null)
        channel.sendMessage(//"Adding to queue: " + track.getInfo().title
                EmbededBuilder.create("Currently playing", track.getInfo().title+"\n"+
                        "```"+track.getInfo().uri+"```", Color.green, GoogleImageSearch.search(track.getInfo().title)).build()).queue();
    }

    public void pausePlayback(TextChannel channel){
        GuildMusicManager m = getGuildAudioPlayer(channel.getGuild());
        m.player.setPaused(true);
        channel.sendMessage("Playback paused").queue();
    }
    public void restartPlayback(TextChannel channel){
        GuildMusicManager m = getGuildAudioPlayer(channel.getGuild());
        m.player.setPaused(false);
        channel.sendMessage("Playback resumed").queue();
    }
    public void setVolume(int i, TextChannel channel){
        GuildMusicManager m = getGuildAudioPlayer(channel.getGuild());
        m.player.setVolume(i);

    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }
    private static void connectToVoiceChannelById(AudioManager audioManager,String channelID) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            VoiceChannel voiceChannel = audioManager.getGuild().getVoiceChannelById(channelID);
                audioManager.openAudioConnection(voiceChannel);


        }
    }

}
