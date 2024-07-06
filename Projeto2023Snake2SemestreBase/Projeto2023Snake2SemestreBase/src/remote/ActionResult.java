package remote;

public class ActionResult {
	private boolean wasSuccessful;
	private boolean gameEnded;
	
	public ActionResult() {
		
	}

	boolean getGamedEnded(){
		return gameEnded;
	}
	
	void setGamedEnded(){
		gameEnded=true;
	}
}
