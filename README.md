# Java Music Generator

By Artem Chubaryan.

Java Music Generator (or JMG) is based on [jMusic library](http://www.explodingart.com/jmusic/) and allows you to create music in real time, play it and save to MIDI or MP3.
Started as a master's degree final project it continued to grow, and I hope the quality of the generated music will be perfected with time.

## Before Starting

* I advise you to read [introduction article](http://habrahabr.ru/post/185154/) on Habrahabr.
* More deeply the insides of the project are described in my [master's paper](paper.docx).
* Also you can view a [quick presentation](presentation.pdf).
_All information is currently available only on Russian._

## Getting Started

* Install [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
* Download the project.
* Add `gervill.jar` and `jave-1.0.2.jar` from _/libs_ to build path.
* Install additional soundfonts (see below).
 
## Installing Soundfonts

You can use custom soundbanks to make generator sound better. All you need is to put .sf2 file in _/soundfonts_ directory and use it in RealtimePlayer.java.
I suggest you to download free soundbank Fluid R3, which includes all the General Midi instruments.

1. Download [Fluid R3 (141 Mb)](http://www.musescore.org/download/fluid-soundfont.tar.gz).
2. Extract it to _/soundfonts_.
3. Rename the file to _fluid.sf2_ or change constant `SOUNDFONT_FILENAME` in `/src/player/RealtimePlayer.java`
4. Enjoy the new amazing sound of generator :)

## License

JMG is distributed under the [GNU GPL v2](http://www.gnu.org/licenses/gpl-2.0.html).

