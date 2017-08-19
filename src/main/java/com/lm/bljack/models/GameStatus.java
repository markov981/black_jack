package com.lm.bljack.models;

public class GameStatus {

	private int pointsTotal;
	public GameStatus(int points) { pointsTotal = points; }

	
	// Is it a Bust?
    public boolean isBust() {
    	if(pointsTotal > 21) return true;
    	return false;}

    
	// Is it a BlackJack?
    public boolean isBlackJack() {
    	if(pointsTotal == 21) return true;
    	return false;}
    
    
	// BlackJack message to the Player
    public String isBlackJackMsg() 
    	{ return "You've got 21 (in case you didn't notice). "
    			+ "Please press that STAND button so that we can calculate your payoff.";}
    
}
