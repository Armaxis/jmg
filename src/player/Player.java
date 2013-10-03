/**
 * 
 */
package player;

import jm.music.data.Score;
import util.Log;

/**
 * @author Armaxis
 *
 */
public class Player extends Thread {

	public boolean isReady = false;
	
	public void init(){
		Log.severe("IPlayer > Method wasn't overriden!");
	} 
	
	public void playScore(Score score){
		Log.severe("IPlayer > Method wasn't overriden!");
	}
	
	@Override
	public void run()
	{
		Log.severe("IPlayer > Method wasn't overriden!");
	}
	
}
