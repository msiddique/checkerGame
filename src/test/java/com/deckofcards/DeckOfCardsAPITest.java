package com.deckofcards;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import kong.unirest.GetRequest;
import kong.unirest.Unirest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
import com.fasterxml.jackson.core.JsonProcessingException;

public class DeckOfCardsAPITest {
	String baseUrl = "https://deckofcardsapi.com/api/";
	ObjectMapper om = new ObjectMapper();

	@Test
	public void test() throws JsonMappingException, JsonProcessingException {
		Deck deck = getDeck();
		System.out.println(deck.deck_id);
		shuffleDeck(deck);
		Player playerOne = om.readValue(drawCards(deck, 3), Player.class);
		System.out.println(playerOne.remaining);
		Player playerTwo = om.readValue(drawCards(deck, 3), Player.class);
		System.out.println(playerTwo.remaining);

	}

	public Deck getDeck() throws JsonMappingException, JsonProcessingException {
		String jsonBody = Unirest.get(baseUrl + "deck/new/").asString().getBody();
		return om.readValue(jsonBody, Deck.class);

	}
	
	public void shuffleDeck(Deck deck) {
	    GetRequest resp = Unirest.get(baseUrl +"deck/{deck_id}/shuffle/").routeParam("deck_id", deck.deck_id);
	    String jsonString = resp.asString().getBody();
	    deck.shuffled = true;
	}
	
	public String drawCards(Deck deck ,int numberOfCards) {	
		// https://deckofcardsapi.com/api/deck/<<deck_id>>/draw/?count=2	
		GetRequest resp = Unirest.get(baseUrl +"deck/{deck_id}/draw").routeParam("deck_id", deck.deck_id).queryString("count", numberOfCards);
	    String jsonString = resp.asString().getBody();
	    System.out.println(jsonString);
	    return jsonString;
	}
	
	public boolean checkBlackJack(ArrayList<Card> cards) {
		int total = 0;
		return true;
	}

}
