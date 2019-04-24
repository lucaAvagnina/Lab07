package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	private PowerOutageDAO podao;
	private List<Blackout> blackout = new ArrayList<Blackout>();
	
	private List<Blackout> best; 
	private int customer_best;
	private double oreTot;
	
	private int anniMax;
	private int oreMax;
	
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}

	public String getMassimizzazione(Nerc nercScelto, int anniScelti, int oreScelte) {
		
		anniMax = anniScelti;
		oreMax = oreScelte;
		
		best = null;
		customer_best = 0;
		oreTot = 0;
		
		blackout = podao.getAllBlackoutNerc(nercScelto);
		
		List<Blackout> parziale = new ArrayList<Blackout>();
		
		recursive(parziale, 0, 0, 0);
		
		String result = "";
		
		result += "Tot people affected: " + customer_best + "\n" + "Tot hours of outage: " + oreTot/60 + "\n";
		
		for(Blackout b : best) {
			result += b.toString();
		}
		
		return result.trim();		
	}

	private void recursive(List<Blackout> parziale, int customer, int ore, int livello) {
		
		if(customer > customer_best){
			best = new ArrayList<Blackout>(parziale);
			customer_best = customer;
			oreTot = ore;
		}
		
		for(Blackout b : blackout) {
			
			int oreBlackout = (int)Duration.between(b.getDateEventBegan(), b.getDateEventFinished()).toMinutes();
			
			if((ore+oreBlackout) <= oreMax*60 && this.calcolaAnno(b, parziale) == true && (parziale.size()<1 || b.getDateEventBegan().isAfter(parziale.get(parziale.size()-1).getDateEventBegan()))) {
				parziale.add(b);
				customer += b.getCustomerAffected();
				ore += oreBlackout;
				recursive(parziale, customer, ore, livello+1);
				
				//back-tracking
				parziale.remove(b);
				customer = customer-b.getCustomerAffected();
				ore = ore - oreBlackout;
			}
		}
		
	}
	
	/**
	 * 
	 * @param b l'elemento che voglio aggiungere
	 * @param parziale la mia soluzione parziale
	 * @return {@code true} se gli anni dell'elemento che voglio aggiungere e l'elemento che voglio aggiungere sono maggiori del numero di anni massimi possibili, {@code false} altrimenti.
	 */
	private boolean calcolaAnno(Blackout b, List<Blackout> parziale) {
		
		if(parziale.size() < 1)
			return true;
		else {
			if(b.getDateEventBegan().getYear()-parziale.get(parziale.size()-1).getDateEventBegan().getYear() <= anniMax)
				return true;
		}
		
		return false;
	}

}
