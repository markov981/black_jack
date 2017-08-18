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
}