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
import com.lm.bljack.models.View;


@Controller
@RequestMapping( {"", "/", "/blackjack"} )
public class BlackJackController 
{
	
	final int NUMBER_OF_CARDS = 52;

	// Create Hands for Player / Dealer
	public ArrayList<String> playerHandArr = new ArrayList<String>();
	public ArrayList<String> dealerHandArr = new ArrayList<String>();
	
	// Initial $ amount in your account is set randomly in order not to hard-code it: [100; 1,000] 
	Random rnd = new Random();
	private int startWithAmount = 100 + rnd.nextInt(901);
	
	// Returned from the Player
	private int bet;
	
	// i counts cards dealt in the current play
	private int i = 0; 
	// Number of cards that have dealt in a game, excluding the current game 
	private int cards_dealt = 0;
	
	// Empty Player/Dealer Hands
    private String noInfo = " Points:         Cards:    ";
    
    // EvenMoney proposal, state
    boolean EvenMoney = false;
	
    
	// Though cards are listed by 'value' you can tell '10' from Face cards & '11' is shown as <A> in a hand
	public int[] deck = {2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,
    		     		 99,99,99,99,
    		     		 10,10,10,10,10,10,10,10,10,10,10,10,11,11,11,11};	
    
       
	
	public BlackJackController()  { 		    		
	    // Shuffle it 	
		Shuffle shfl = new Shuffle();
		shfl.shuffleArray(deck);}	    	
	
	
	Account act     = new Account(startWithAmount);
	Hand handPlayer = new Hand();
    Hand handDealer = new Hand();
    Dealer dlr      = new Dealer();
    View vw         = new View();
	
    
    
    
    
	/* 0. "START PLAY" button
	 * ----------------------
	    --> Make visible START PLAY button
	    --> Make visible BET        button
	 */
	@GetMapping("")    
	public String index(Model model)   { 
			
		// How many cards left in the deck
		vw.showCardsLeft(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt);

		// 'max_value' = upper limit of <input> used to select a bet: right now it.equals(100% of Player's a/c)
		model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", startWithAmount); 		// At this point Current Amount = Starting Amount
		model.addAttribute("win", "");
		model.addAttribute("betA", "");   		                    // Bet is not defined at this point
		
		// Select what buttons to show/hide	
		GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
		vw.renderToView(model, status.isEmptyMsg(), status.isEmptyMsg(), true, false, false, false, false);

		
		// No info on Player/Dealer Hands
		model.addAttribute("points_p", noInfo);
		model.addAttribute("points_d", noInfo);
			
		return "bljack/front";}
	
	
	
					
	/*  1. 'START PLAY' button
	 *  -------------------
        --> 'START GAME' button hides (turns into 'EXIT GAME') ??
        -->  BET <input> hides
	    --> 'HIT' & 'STAND' appear 
	    -->  Player / Dealer total points & cards
	    -->  Player account $, number of cards left 
	 */ 
	@PostMapping("gamestart")
	public String dealCardsToPlayerDealer(Model model, int bet)   
	{   
        String temp="";
		this.bet = bet;
		
		// Deal 4 cards to Player/Dealer
		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHandArr);
        handPlayer.loadOneCard(String.valueOf(deck[i + 2]), playerHandArr);
        handDealer.loadOneCard(String.valueOf(deck[i + 1]), dealerHandArr);
        handDealer.loadOneCard(String.valueOf(deck[i + 3]), dealerHandArr);
        
        // PLAYER: total points & list of cards for after the first round
        temp =" Points: " + handPlayer.getTotalPoints(playerHandArr) + "    Cards: " + handPlayer.getMyCards(playerHandArr);
        model.addAttribute("points_p", temp);
        
        // DEALER: Hide the HOLE & its points from the Player 
        temp =" Points: " + handDealer.getTotalPointsNoHole(dealerHandArr) + "    Cards: " + 
                handDealer.listOneCard(dealerHandArr) + " <HOLE>";        
        model.addAttribute("points_d", temp);

        
		// What buttons are visible
		GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
		vw.renderToView(model, status.isEmptyMsg(), status.isEmptyMsg(), false, true, false, false, false);

		// 'max_value' = upper limit of <input> used to select a bet: right now it.equals(100% of Player's a/c)
		vw.showCardsLeft(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt);
		model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", "");
		model.addAttribute("betA", this.bet);
          
	    // Even Money offer 
		if(status.isBlackJack(playerHandArr) && dealerHandArr.get(0).equals("11")) {
			model.addAttribute("isEvenMoney", true);
		}
		
        // Branching.....
        if (handPlayer.getTotalPoints(playerHandArr) == 21) 
            { vw.renderToView(model, status.is21Msg(), status.isStandMsg(), false, false, true, false, false); }
        // Bust?
        if (handPlayer.getTotalPoints(playerHandArr) > 21) 
	        { vw.renderToView(model, status.isBustMsg(), status.isStandMsg(), false, false, false, true, false); }
        // Out of cards?
        if(vw.getCardNumb(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt) == 0)
        	{vw.renderToView(model, status.isOutOfCardsMsg(), status.isEmptyMsg(), false, false, false, false, true);}
        // Out of money?
        if(act.getAccountAmount() == 0)
    	    {vw.renderToView(model, status.isOutOfMoneyMsg1(), status.isOutOfMoneyMsg2(), false, false, false, false, true);}

        
        // return "redirect:/blackjack      bljack/blackjack   bljack/deal-form-ST  redirect:/blackjack;    
		return "bljack/deal-form-ST";
	}	
	
	
	
  
	/*  2. 'HIT' button. Press until Player
	 *  --------------------------------
        -->  Will press 'STAND'
        -->  Goes Bust
	    -->  Goes BlackJack 
	    -->  Runs out of money
	    -->  The game runs out of cards 
	 */ 
	@PostMapping("/hit")
	public String dealACardToPlayer(Model model, boolean accept_em_offer) 
	{    
		String temp="";
		boolean EvenMoneySuffFunds = true;
		/* If Player accepts the Even Money offer: 
		     - His bet value is increased 50% (though with bet being an int this does not work too well)  
		     - Also check for funds availability (same as above)
		     - If the Player does not have funds to increase the bet by 50%, he gets a message: status.isEvenMoneyMsg(EvenMoney)
		 */
		if (EvenMoney) {
			if(handPlayer.getTotalPoints(playerHandArr) >= 1.5 * bet) { bet = (int)(1.5 * bet); }
			else {
				EvenMoney = false;
				EvenMoneySuffFunds = false;
			}
		}
		
		// Deal a card
		i = playerHandArr.size() + dealerHandArr.size();		
		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHandArr);				
		temp = " Points: " + handPlayer.getTotalPoints(playerHandArr) + "    Cards: " + 
		         handPlayer.getMyCards(playerHandArr);
		model.addAttribute("points_p", temp);
		
		temp = " Points: " + handDealer.getTotalPointsNoHole(dealerHandArr) + "    Cards: " + 
				handDealer.listOneCard(dealerHandArr) + " <HOLE>";        
		model.addAttribute("points_d", temp);


		// What to show
		GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));		
		vw.renderToView(model, status.isEvenMoneyMsg(EvenMoneySuffFunds), status.isEmptyMsg(), false, true, false, false, false);
		
		//Player account-related info
		vw.showCardsLeft(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", "");
		model.addAttribute("betA", bet);

		
//		System.out.println("Player points: " + handPlayer.getTotalPoints(playerHandArr));
//		System.out.println("bet: " + bet);
//		System.out.println("Account bal: " + act.getAccountAmount());
		
		
		
        // Branching.....
        if (handPlayer.getTotalPoints(playerHandArr) == 21) 
            { vw.renderToView(model, status.is21Msg(), status.isStandMsg(), false, false, true, false, false); }
        // Bust?
        if (handPlayer.getTotalPoints(playerHandArr) > 21) 
	        { vw.renderToView(model, status.isBustMsg(), status.isStandMsg(), false, false, true, false, false); }	       
        // Out of cards?
        if(vw.getCardNumb(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt) == 0)
        	{vw.renderToView(model, status.isOutOfCardsMsg(), status.isEmptyMsg(), false, false, false, false, true);}
        // Out of money?
        if(act.getAccountAmount() == 0)
    	    {vw.renderToView(model, status.isOutOfMoneyMsg1(), status.isOutOfMoneyMsg2(), false, false, false, false, true);}
      
        System.out.println("The end of HIT -------->"); 
        
		return "bljack/deal-form-ST";
		// return "redirect:/blackjack     return "bljack/hit";
	}

	
	
	
	/*  3. 'STAND' button
	 *  -----------------
        -->  Play is over (the game continues) - interaction with Player stops
        -->  Draw cards for the Sealer
        -->  Account settlement (payoffs are calculated)
	    -->  Show results, including what Dealer draw
	 */	
	@PostMapping("/stand")
	public String activePlayStops(Model model)   
	{
		String temp =""; 
		
		GameStatus statusPL = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
		GameStatus statusDL = new GameStatus(handDealer.getTotalPoints(dealerHandArr));	
		System.out.println("First Stand Message ======");
		
		
		// 1. Dealer gets cards, update his account
		i = playerHandArr.size() + dealerHandArr.size();	
		dlr.getDealerPoints(dealerHandArr, deck, i);
        temp = " Points: " + handDealer.getTotalPoints(dealerHandArr) + "    Cards: " + handDealer.getMyCards(dealerHandArr);
        model.addAttribute("points_d", temp);	
        
       
		// 2. Send Player account data, though nothing has changed
		temp = " Points: " + handPlayer.getTotalPoints(playerHandArr) + "    Cards: " + handPlayer.getMyCards(playerHandArr);
        model.addAttribute("points_p", temp);
        
        
        // 3. Final pay-off
		temp = act.setPlayOutcome(handPlayer.getTotalPoints(playerHandArr), handDealer.getTotalPoints(dealerHandArr), 
					               statusPL, statusDL, playerHandArr, dealerHandArr);
		act.setAccountAfterPayoff(temp, bet, EvenMoney);
		
		
		// 4. What to show	
		vw.renderToView(model, act.generateOutcomeMsg(temp), act.generateWinLooseMsg(temp, bet), 
				        false, false, false, true, false);
		model.addAttribute("isEvenMoney", false);
	
		
		// 5. Need to substitute with a method call
		vw.showCardsLeft(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", act.getWinLoss(startWithAmount));
		model.addAttribute("betA", bet);
		
        // Out of cards?
        if(vw.getCardNumb(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt) == 0)
        	{vw.renderToView(model, statusPL.isOutOfCardsMsg(), statusPL.isEmptyMsg(), false, false, false, false, true);}
        // Out of money?
        if(act.getAccountAmount() == 0)
    	    {vw.renderToView(model, statusPL.isOutOfMoneyMsg1(), statusPL.isOutOfMoneyMsg2(), false, false, false, false, true);}
        
        
		// return "redirect:/blackjack"; 
		return "bljack/deal-form-ST";
	}
	
	

	
	/*  4. Press 'NEW PLAY' button
	 *  ------------------------------------
	    -->  Clear: 	Total points, Bet value, both hands   
	    -->  Persist: 	Account balance, Number of cards left, Starting Amount
	 */	
	@PostMapping("/new_game")
	public String newPlay(Model model)  	
	{ 
		GameStatus sts = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
		vw.renderToView(model, sts.newPlayMsg(), sts.isEmptyMsg(), true, false, false, false, false);
		model.addAttribute("isEvenMoney", false);
	    
		// Keep account balance & number of cards left from the previous play 
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());		
		model.addAttribute("max_value", act.getAccountAmount());
				
		// At this point the Hands have not been cleared yet
		vw.showCardsLeft(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt);
		// Populate "# of cards dealt' in previous play(s)
		cards_dealt = playerHandArr.size() + dealerHandArr.size();
		
		// Re-set values 
		model.addAttribute("win", "");
		model.addAttribute("betA", "");
		playerHandArr.clear();
		dealerHandArr.clear();
		
		bet = 0;
		
		// No info on Player/Dealer Hands
		model.addAttribute("points_p", noInfo);
		model.addAttribute("points_d", noInfo);
		
        // Out of cards?
        if(vw.getCardNumb(NUMBER_OF_CARDS, model, playerHandArr, dealerHandArr, cards_dealt) == 0)
        	{vw.renderToView(model, sts.isOutOfCardsMsg(), sts.isEmptyMsg(), false, false, false, false, true);}
        // Out of money?
        if(act.getAccountAmount() == 0)
    	    {vw.renderToView(model, sts.isOutOfMoneyMsg1(), sts.isOutOfMoneyMsg2(), false, false, false, false, true);}
        
        System.out.println("the end of Play ------------>");
        
        
		return "bljack/deal-form-ST";
	}
	
	

	/*  5. Press 'END GAME' button
	    ------------------------------------
		  - No interaction, no buttons
		  - just messages & picture
	 */	
	@PostMapping("/end_game")
	public String endGame(Model model)  	
	{ 
		GameStatus sts = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
		vw.renderToView(model, sts.isEmptyMsg(), sts.isEmptyMsg(), false, false, false, false, true);
		model.addAttribute("isEvenMoney", false);

		model.addAttribute("points_p", noInfo);
		model.addAttribute("points_d", noInfo);
		model.addAttribute("msg", "");
		model.addAttribute("msg1", "");
		
		return "bljack/end";
	}
}


