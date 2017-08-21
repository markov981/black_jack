package com.lm.bljack.models;

import java.util.ArrayList;

import org.springframework.ui.Model;



public class View {
	
	// Chooses html elements to view
	public void renderToView(Model model, Object obj1, Object obj2,
			                       boolean isGameStart, boolean isPlayStart, boolean isStand, 
			                       boolean isNewPlay,   boolean isGameOver
			                       )
	{
	model.addAttribute("isGameStart", isGameStart);	
	model.addAttribute("isPlayStart", isPlayStart);
	model.addAttribute("isStand", isStand);
	model.addAttribute("isNewPlay", isNewPlay);
	model.addAttribute("isGameOver", isGameOver);
    model.addAttribute("msg", obj1);
    model.addAttribute("msg1", obj2);}
	
	
	// 'accmCards' = cards dealt in previous games, accumulated number
	public void showCardsLeft(final int num, Model model, ArrayList<String> handPl, ArrayList<String> handDl, int accmCards) {
		int temp = 52;
		temp = num - handPl.size() - handDl.size() - accmCards;
		model.addAttribute("cards_left", temp);}
	
	// this is a mess...
	public int getCardNumb(final int num, Model model, ArrayList<String> handPl, ArrayList<String> handDl, int accmCards) {
		return num - handPl.size() - handDl.size() - accmCards;}

}
