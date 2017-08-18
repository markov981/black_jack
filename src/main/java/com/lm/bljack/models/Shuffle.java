package com.lm.bljack.models;

import java.util.Random;
   

/* This is Fisher-Yates shuffle algorithm (I think) - AK
 * -----------------------------------------------------
     * STEP 1. 
     *   i = 51 (we start with the last card) 
     *   Generate a 'random number' = an integer between 0 and 52 (not inclusive of 52, so we are OK)
     *   Meaning, that at this step card[51] could be swapped with any other card (itself including)   
     *   Swap happens, move on to the next card --> to the one before the 1st, i.e. card[1] 
     *   
     * STEP ... Last
     *   No shuffling is needed for Card[0] and nothing is provided as i > 0. 
     */
//private static? 
public class Shuffle{
	
	public void shuffleArray(int[] deckCards) 
		{
	        Random rnd = new Random();
	        for (int i = deckCards.length - 1; i > 0; i--){
	            int index = rnd.nextInt(i + 1);
	            
	            int a = deckCards[index];
	            deckCards[index] = deckCards[i];
	            deckCards[i]     = a;
	            
	            // TBD
	            // System.out.println(" index: " + i + " value: " + deckCards[i]);	            
	        }	
	    }
}
