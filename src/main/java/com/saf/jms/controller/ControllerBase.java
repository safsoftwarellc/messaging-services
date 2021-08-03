package com.saf.jms.controller;

public abstract class ControllerBase {
	

	public int getRandomNumber() {
		int randomNumber = ( int )( Math.random() * 9999 );

		if( randomNumber <= 1000 ) {
		    randomNumber = randomNumber + 1000;
		}
		return randomNumber;

	}
}
