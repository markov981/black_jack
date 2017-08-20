package com.lm.bljack.controllers;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lm.bljack.models.Account;
import com.lm.bljack.models.Dealer;
import com.lm.bljack.models.GameStatus;
import com.lm.bljack.models.Hand;
import com.lm.bljack.models.Shuffle;


@Controller
@RequestMapping( {"", "/", "/blackjack"} )
public class BlackJackController 
{
	
	final int NUMBER_OF_CARDS = 52;
	
	// Set up for the Game.START  ?? use enumerations ??
	boolean isGameOver  = false;
	boolean isPlayOver  = false;
	boolean isPlayStart = false;
	boolean isGameStart = true;
	boolean isNewPlay   = false;

	// Create Hands(s) for Player / Dealer - there is a reason why these are <String> not <Integer>
	public ArrayList<String> playerHandArr = new ArrayList<String>();
	public ArrayList<String> dealerHandArr = new ArrayList<String>();
	
	// Initial $ amount in your account is determined randomly in order not to hard-code it: [101 
	Random rnd = new Random();
	private int startWithAmount = 100 + rnd.nextInt(901);
	
	// Declared it a reference because: the very 1st time it goes into view, 
	// it has to return null and a primitive cannot do that
	private Integer bet;
	
	// Counts cards dealt (array index)
	private int i = 0; 
	private int cards_left = 52;
	
	// Cards are listed by 'value' (the dumb way) though you can tell '10' & Face cards apart
	public int[] deck = {2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,
    		     		 99,99,99,99,
    		     		 10,10,10,10,10,10,10,10,10,10,10,10,11,11,11,11};	
    
       
	
	public BlackJackController()  { 		    		
	    // Shuffle it 	
		Shuffle shfl = new Shuffle();
		shfl.shuffleArray(deck);
		}	    	
	
	
	Account act = new Account(startWithAmount);
	Hand handPlayer = new Hand();
    Hand handDealer = new Hand();
    Dealer dlr = new Dealer();
	
    
    
	/* 0. Make visible a single button: "START PLAY"
	 * ---------------------------------------------
	    --> Make visible START GAME button
	    --> Make visible BET        button
	 */
	@GetMapping("")    
	public String index(Model model)   { 
			
		// How many cards left in the deck
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);

		// 'max_value' = upper limit of <input> used to select a bet: right now it.equals(100% of Player's a/c)
		model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		// At this point Current Amount = Starting Amount
		model.addAttribute("current_amount", startWithAmount);
		model.addAttribute("win", 0);
		// Bet is not defined at this point
		model.addAttribute("betA", 0);
		
		// Select what buttons to show/hide		
		model.addAttribute("isGameStart", isGameStart);
		model.addAttribute("isPlayStart", isPlayStart);
		model.addAttribute("isNewPlay", false);
		model.addAttribute("msg", "");
		model.addAttribute("msg1", "");
		
		// No info on Player/Dealer Hands
		String noInfo = " Points:         Cards:    ";
		model.addAttribute("points_p", noInfo);
		model.addAttribute("points_d", noInfo);
			
		return "bljack/front";}
	
	
	
					
	/*  1. Press 'START PLAY' button
	 *  --------------------------------------
        --> 'START GAME' button hides (turns into 'EXIT GAME') ??
        -->  BET <input> hides
	    --> 'HIT' & 'STAND' appear 
	    -->  Player / Dealer total points & cards
	    -->  Player account $, number of cards left 
	 */ 
	@PostMapping("gamestart")
	public String dealCardsToPlayerDealer(Model model, int bet)   
	{   
		this.bet = bet;
		
		// Define what buttons are visible
		model.addAttribute("isGameStart", false);
		model.addAttribute("isPlayStart", true);
		model.addAttribute("isNewPlay", false);
		model.addAttribute("msg", "");
		model.addAttribute("msg1", "");

		// 'max_value' = upper limit of <input> used to select a bet: right now it.equals(100% of Player's a/c)
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);
		model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", act.getWinLoss(startWithAmount));
		model.addAttribute("betA", this.bet);
			
		
		// Deal 4 cards to Player/Dealer
		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHandArr);
        handPlayer.loadOneCard(String.valueOf(deck[i + 2]), playerHandArr);
        handDealer.loadOneCard(String.valueOf(deck[i + 1]), dealerHandArr);
        handDealer.loadOneCard(String.valueOf(deck[i + 3]), dealerHandArr);
        
        // PLAYER: total points & list of cards for after the first round
        String temp="";
        temp = String.valueOf(" Points: " + handPlayer.getTotalPoints(playerHandArr)) + "    Cards: " +
          	   String.valueOf(handPlayer.getMyCards(playerHandArr));
        model.addAttribute("points_p", temp);
        
        // DEALER: Hide the HOLE & its points from the Player 
        temp = String.valueOf(" Points: " + handPlayer.getTotalPointsNoHole(dealerHandArr, 1)) + "    Cards: " +
         	   dealerHandArr.get(0) + " <HOLE>";        
        model.addAttribute("points_d", temp);

                
        
        /*  Player goes Blackjack (he cannot go Bust at this point)?
             - Remove the HIT button from the form
             - Only the STAND button is left 
        	 - We can proceed without interacting with the PLAYER, but need to stop at View with STAND to message him on what happened
        */
        GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
        if (status.is21Total(playerHandArr)) { status.isStopHitting(model, status, playerHandArr); }
		

        // return "redirect:/blackjack      bljack/blackjack   bljack/deal-form-ST  redirect:/blackjack;    
		return "bljack/deal-form-ST";
	}	
	
	
	
  
	/*  2. Press 'HIT' button until Player
	 *  ------------------------------------
        -->  Feels like pressing 'STAND'
        -->  Goes Bust
	    -->  Goes BlackJack 
	    -->  Runs out of money
	    -->  The game runs out of cards 
	 */ 
	@PostMapping("/hit")
	public String dealACardToPlayer(Model model) 
	{		
		model.addAttribute("isGameStart", false);
		model.addAttribute("isPlayStart", true);
		model.addAttribute("isNewPlay", false);
		model.addAttribute("msg", "");
		model.addAttribute("msg1", "");
		
		//Player account-related info
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", act.getWinLoss(startWithAmount));
		model.addAttribute("betA", bet);
		
        
        // Deal a card
		i = playerHandArr.size() + dealerHandArr.size();		
		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHandArr);
        
		String temp="";
        temp = String.valueOf(" Points: " + handPlayer.getTotalPoints(playerHandArr)) + "    Cards: " +
        	   String.valueOf(handPlayer.getMyCards(playerHandArr));
        model.addAttribute("points_p", temp);
    
        temp = String.valueOf(" Points: " + handPlayer.getTotalPointsNoHole(dealerHandArr, 1)) + "    Cards: " +
         	   dealerHandArr.get(0) + " <HOLE>";        
        model.addAttribute("points_d", temp);
		

        
		// Check game status
        GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
			// Is 21?
        if (handPlayer.getTotalPoints(playerHandArr) == 21) { status.isStopHitting(model, status, playerHandArr); }
        
        System.out.println("Is it 21 points:  " + handPlayer.getTotalPoints(playerHandArr));
			// Bust
        if (handPlayer.getTotalPoints(playerHandArr) > 21) {
	        model.addAttribute("isPlayStart", false);
	        model.addAttribute("isNewPlay", false);
	        model.addAttribute("isStand", true);
	        model.addAttribute("msg", status.isBustMsg());
            model.addAttribute("msg1", status.isStandMsg());}
       
		return "bljack/deal-form-ST";
		// return "redirect:/blackjack 
        // return "bljack/hit";
	}

	
	
	/*  3. Press 'STAND' button
	 *  ------------------------------------
        -->  Play is over (the game continues) - interaction with Player stops
        -->  Draw cards for the Sealer
        -->  Account settlement (payoffs are calculated)
	    -->  Show results, including what Dealer draw
	 */	
	@PostMapping("/stand")
	public String activePlayStops(Model model)   
	{  
        model.addAttribute("isPlayStart", false);
        model.addAttribute("isStand", true);
        model.addAttribute("isNewPlay", true);

        
        
		// 1. Dealer gets cards
		i = playerHandArr.size() + dealerHandArr.size();	
		dlr.getDealerPoints(dealerHandArr, deck, i);
		
		System.out.println("Dealer's card index: " + i);
		
		// Send info on Player account, though nothing has changed here
        String temp = String.valueOf(" Points: " + handPlayer.getTotalPoints(playerHandArr)) + "    Cards: " +
        	   String.valueOf(handPlayer.getMyCards(playerHandArr));
        model.addAttribute("points_p", temp);
		
		
		// 2. Payoff calculated, Player account adjusted
		GameStatus statusPL = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
		GameStatus statusDL = new GameStatus(handPlayer.getTotalPoints(dealerHandArr));
		
		temp = act.setPlayOutcome(handPlayer.getTotalPoints(playerHandArr), handDealer.getTotalPoints(dealerHandArr), 
					               statusPL, statusDL, playerHandArr, dealerHandArr);
		act.setAccountAfterPayoff(temp, this.bet);
		
        model.addAttribute("msg",  act.generateOutcomeMsg(temp));
        model.addAttribute("msg1", act.generateWinLooseMsg(temp, bet));
		
		
		// 3. Play-level objects cleared, Game-level - updated
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", act.getWinLoss(startWithAmount));
		model.addAttribute("betA", bet);
		
		
		// 4. The final Play-level View: show updated dealer result, state game outcome and show updated Player account
        temp = String.valueOf(" Points: " + handDealer.getTotalPoints(dealerHandArr)) + "    Cards: " +
        	   String.valueOf(handDealer.getMyCards(dealerHandArr));
        model.addAttribute("points_d", temp);
        
		// return "redirect:/blackjack"; 
		return "bljack/deal-form-ST";
	}
	
	

	
	/*  4. Press 'NEW PLAY' button
	 *  ------------------------------------
	    -->  Clear: 	Total points, Bet value  
	    -->  Persist: 	Account balance, Number of cards left, Starting Amount
	 */	
	@PostMapping("/new_game")
	public String newPlay(Model model)   
	{  
		startWithAmount = 100 + rnd.nextInt(901);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		
		model.addAttribute("win", 0);
		model.addAttribute("betA", 0);
		playerHandArr.clear();
		dealerHandArr.clear();
		bet = 0;
				
		return "bljack/deal-form-ST";
	}
}


