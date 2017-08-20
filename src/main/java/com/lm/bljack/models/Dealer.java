package com.lm.bljack.models;

import java.util.ArrayList;

/* Keeps account updated with respect to 
 *    - Bets
 *    - Payoff calculations
*/ 
public class Dealer {
	
	private int pointsDealer;
		
	
	// Deal to Dealer until his hand > 17
	public int getDealerPoints(ArrayList<String> handArr, int[] deck, int i) {
		
		Hand dlrHand = new Hand();
		while (dlrHand.getTotalPoints(handArr) < 17) {
				dlrHand.loadOneCard(String.valueOf(deck[i]), handArr);} 
		
		pointsDealer = dlrHand.getTotalPoints(handArr);
		
		return pointsDealer;}

	}
