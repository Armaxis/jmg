package core.generators;

import jm.music.data.Score;
import core.data.Voice;

public interface IMusicGenerator {

	public void init();
	public Score getScore();
	public void addVoice(Voice v);
	public void removeVoice(Voice v);
}
