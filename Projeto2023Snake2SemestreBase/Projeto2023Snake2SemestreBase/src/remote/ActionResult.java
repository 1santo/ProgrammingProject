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
	
	public ActionResult(boolean wasSuccessful, boolean gameEnded) {
		this.wasSuccessful=wasSuccessful;
		this.gameEnded=gameEnded;
	}

	boolean getGamedEnded(){
		return gameEnded;
	}
	
	void setGamedEnded(){
		gameEnded=true;
	}
}
