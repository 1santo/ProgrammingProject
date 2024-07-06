package remote;

import java.io.Serializable;

public class ActionResult implements Serializable{//pra guardar o estado de objetos de forma persistente
						//converte os objs num byte stream ou seja ida e volta (leitura e escrita)
					//e permite guardar tudo ou numa DB, file ou over the network
					//dps a deserializacao e' qnd esse obj e' convertido numa copia
				//desse obj q foi serializado
			//nao tem metodos
	private boolean wasSuccessful;
	private boolean gameEnded;
	
	public ActionResult(boolean wasSuccessful, boolean gameEnded){
		this.wasSuccessful=wasSuccessful;
		this.gameEnded=gameEnded;
	}

	public boolean isGameEnded(){
		return gameEnded;
	}
	
	void setGameEnded(boolean gameEnded){
		this.gameEnded = gameEnded; //mais safe se meter escificamente
					//doq meter gameEnded=true acho eu
	}
	
	public boolean wasSuccessful(){
		return wasSuccessful;
	}

	public void setWasSuccessful(boolean wasSuccessful){
		this.wasSuccessful = wasSuccessful;
	}
	
	@Override
	public String toString(){
		return wasSuccessful + "," + gameEnded;
	}

	public static ActionResult fromString(String str) {
		String[] parts = str.split(",");
		boolean wasSuccessful = Boolean.parseBoolean(parts[0]);
		boolean gameEnded = Boolean.parseBoolean(parts[1]);
		return new ActionResult(wasSuccessful,gameEnded);
	}
	
}
