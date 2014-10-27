package vamix.videoProcessing;

import javax.swing.SwingWorker;

import vamix.util.Constants;

public class PlaybackWorker extends SwingWorker<Void,Integer>{
	
	private long _skipRate;
	//constructor to allow the input from user to be use in extractworker
	public PlaybackWorker(long skipRate){
		_skipRate=skipRate;
	}


	@Override
	protected Void doInBackground() throws Exception {
		//make sure the correct process Builder is setup as it is weird
		while(true){
			if (!isCancelled()){
				//publish to progress bar
				vamix.userInterface.Main.vid.skip(_skipRate);
				Thread.sleep(Constants.SKIP_RATE_THREAD_SLEEP);
			}else{
				break;
			}
		}
		return null;
	}


}
