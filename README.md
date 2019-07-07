# Wavvy (Bot)
A cute little discord bot that has some neat little features built-in, with the goal to be customiziable.

Made for Discord Hackweek.

## Installation
Please take a look at [this guide](https://github.com/flexusma/Wavvy/wiki)

If you dont want to do all this stuff, ou can simply invite the bot via https://discordapp.com/oauth2/authorize?client_id=592088339666960394&scope=bot&permissions=536071383

## Functions & Commands

Please note, that even when you change the prefix, you are allways able to use @Wavvy (@mention) as the prefix (you need to use space:  *@Wavvy help*)

* `ping`
checks the bot's latency

* `about`
gives Information about the bot
usage: <prefix>about
  
* `delete`
deletes messages by id or count
usage: <prefix>delete <number|message id>
  
* `prefix`
change the prefix of the bot
usage: <old prefix for example '!'>prefix <new prefix>
  
* `play`
plays the given URL
usage: <prefix>play <url/search>
  
* `skip`
skips to the next song in queue
usage: <prefix>skip
  
* `stop`
stops music playback
usage: <prefix>stop
  
* `resume`
resume music playback
usage: <prefix>resume
  
* `pause`
pauses music playback
usage: <prefix>pasue
  
* `autodelete`
Enables / Disables automatic command message deletion / Shows current status if no arguments given
usage: <prefix>autodelete <on/off/"">
  
* `dwk`
plays DWK in your current channel
usage: <prefix>dwk
  
* `oof`
plays oof in your current channel
usage: <prefix>oof
  
* `bml`
plays Bruder muss los in your current channel
usage: <prefix>bml
  
* `w2g`
creates a new Watch2gether room
usage: <prefix>wtg <media/video link/"">
  
* `queue`
queue commands:
queue list - shows songs in playlist
queue clear - clears the playlist
queue now - shows current song

* `volume`
changes the volume of the bot
usage: <prefix>volume <int>

## Prerequisites
- Java 8 (only jre required, the bot is already compiled in Wavvybot.jar)
- Some Api keys

## License
See [LICENSE.txt](https://github.com/Flexusma/Wavvy/blob/master/LICENSE) for more info.

For the discord testers:
If you want to, you can use my database Server, just pm me on discord: Flexusma#7419
