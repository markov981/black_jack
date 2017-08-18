package com.lm.bljack.models;

import java.util.ArrayList;
   

/*  This could be Player or Dealer hand
 *  -----------------------------------
 *  - Keeps track of the points (START + CARDS DEALT + ACE value resolution)
 *  - Fixes the '10' vs face cards problem (if deck[i] == 99 --> count card as '10' - all '10's are entered as '99's)
 *  - Checks if your Hand = BUST or BLACKJACK
 */
public class Hand{
	

	
		
	// Load cards dealt to Player/Dealer into list(s) to be processed later
	public ArrayList<String> loadTwoCards(int card1, int card2, ArrayList<String> hand){
		hand.add("card1");
		hand.add("card2");
		return hand;}
	
	public ArrayList<String> loadOneCard(String card1, ArrayList<String> hand){
		hand.add(String.valueOf(card1));
		return hand;}
	
	
	// All face cards are loaded as '10's, i.e. by their value points
	// To distinguish a '10' from a face card '10's are loaded as '99's
	private int checkPointsOneCard(int cardPts)  { return cardPts == 99 ? 10 : cardPts; }
	
	
	// Process Player/Dealer-level List for total points
	public int getTotalPoints(ArrayList<String> hand) { 
		int points = 0;
		for(String card : hand)
			points += checkPointsOneCard(Integer.parseInt(card));
			
		return points; }
	
	
	// What is your hand: describe your cards
	public String getMyCards(ArrayList<String> hand) {
		String temp = "";
		String myCardsAre = "";
		
		for(String card : hand) {
									        temp = card;
				if      (card.equals("99")) temp = "10";
				else if (card.equals("10")) temp = "F";
				else if (card.equals("11")) temp = "A";
				myCardsAre += "<" + temp + "> ";}
		
		return myCardsAre.trim();}
	
}
