package it.polito.tdp.poweroutages;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class PowerOutagesController {
		
		private Model model;
	
	    @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private TextArea txtResult;

	    @FXML
	    private ChoiceBox<Nerc> bxNerc;

	    @FXML
	    private TextField txtAnni;

	    @FXML
	    private TextField txtOre;

	    @FXML
	    private Button btnWorstCase;

	    @FXML
	    void doWorstCase(ActionEvent event) {
	    	
	    	txtResult.clear();
	    	
	    	Nerc nercScelto = bxNerc.getValue();
	    	int anniScelti = 0;
	    	int oreScelte = 0;
	    	
	    	try {
	    		anniScelti =  Integer.parseInt(txtAnni.getText());
	    		oreScelte = (Integer.parseInt(txtOre.getText()));
	    	}catch(NumberFormatException e) {
	    		txtResult.setText("Inserisci numeri");
	    		return;
	    	}
	    	
	    	txtResult.setText(model.getMassimizzazione(nercScelto, anniScelti, oreScelte));
	    	
	    }

	    @FXML
	    void initialize() {
	        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert bxNerc != null : "fx:id=\"bxNerc\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert txtAnni != null : "fx:id=\"txtAnni\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert txtOre != null : "fx:id=\"txtOre\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert btnWorstCase != null : "fx:id=\"btnWorstCase\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        

	    }

		public void setModel(Model model) {
			this.model = model; 
	        bxNerc.getItems().addAll(model.getNercList());
		}
	}
