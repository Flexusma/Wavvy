package de.flexusma.wavybot.utils;

import com.jagrosh.jdautilities.command.src.main.java.com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    public BlockingQueue<AudioTrack> queue=null;
    public AudioTrack now=null;


    public static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    public static void registerManager(){
        AudioSourceManagers.registerRemoteSources(playerManager);
    }
  Guild g;
    CommandEvent event;
    int q_index = 0;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        g=guild.getJDA().getGuildById(guild.getId());

    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track,Guild guild, CommandEvent event) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        g=guild;
        if (!player.startTrack(track, true)) {
            queue.offer(track);
            this.event=event;
        }
    }

    public void clearQueue() {
        while (queue.remainingCapacity() <= 2147483646) {
            try {
                queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            nextTrack();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() throws InterruptedException {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.

        if(queue.remainingCapacity()<=2147483646) {
            now = queue.take();
            player.startTrack(now, false);
        }
        else g.getJDA().getGuildById(g.getId()).getAudioManager().closeAudioConnection();

    }

    public BlockingQueue<AudioTrack> getQueue(){
        return queue;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        System.out.println(queue.remainingCapacity());
        if (endReason.mayStartNext) {
            if(queue.remainingCapacity()==2147483647)

                g.getJDA().getGuildById(g.getId()).getAudioManager().closeAudioConnection();

            else {
                try {
                    nextTrack();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


