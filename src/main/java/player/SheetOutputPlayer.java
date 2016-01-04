package player;

import jm.gui.cpn.Notate;
import jm.music.data.Score;

public class SheetOutputPlayer extends Player {

	private Notate notateWindow;
	
	@Override
	public void init() {
		isReady = false;
		notateWindow = new Notate();
	}

	@Override
	public void playScore(Score score) {
		if(isReady)
		{
			notateWindow.appendScore(score);
		}
		else
		{
			//Wait until ready, then append
			while(!isReady)
			{
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			notateWindow.appendScore(score);
		}
	}

	@Override
	public void run() {
		while(!notateWindow.isReady)
		{
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		isReady = true;
	    
	}

}
