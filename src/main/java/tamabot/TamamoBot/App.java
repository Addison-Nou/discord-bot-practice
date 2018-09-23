package tamabot.TamamoBot;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.Permission;

public class App extends ListenerAdapter
{
    public static void main( String[] args ) throws Exception
    {
    	JDA jda = new JDABuilder(AccountType.BOT).setToken(Reference.token).buildBlocking();
        jda.addEventListener(new App());
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent evt)
    {
    	//********* Objects *********\\
    	User objUser = evt.getAuthor();
    	MessageChannel objMsgCh = evt.getChannel();
    	Message objMsg = evt.getMessage();
    	boolean isAdmin = objMsg.getGuild().getMember(objUser).hasPermission(Permission.ADMINISTRATOR);
    	
    	//********* Normal Commands *********\\
    	
    	//Print list of commands
    	if(objMsg.getContentRaw().equalsIgnoreCase(Reference.prefix+"commands") || objMsg.getContentRaw().equalsIgnoreCase(Reference.prefix+"help"))
    	{
    		objMsgCh.sendMessage(objUser.getAsMention() + "```css\nNormal User Commands: \n"
    				+ "\t'>commands' - You just typed this. If you don't know what this does, then you should be worried.\n"
    				+ "\t'>help' - Help me, Eirin! Interchangeable with '>commands'.\n"
    				+ "\t'>ping' - Pings me in order to see if I'm working.\n"
    				+ "\t'>roll xdy' - where x equals the number of dice and y equals the number of sides on said dice.\n"
    				+ "\nAdmin Commands: \n"
    				+ "\t'>clear' - Clears 'x' amount of previous messages to help with channel message clutter.```").queue();
    	}
    	
    	//Ping bot to see if it works
    	if(objMsg.getContentRaw().equalsIgnoreCase(Reference.prefix+"ping"))
    	{
    		objMsgCh.sendMessage(objUser.getAsMention() + " Pong!").queue();
    	}
    	
    	//Dice roller
    	if(objMsg.getContentRaw().toLowerCase().contains(Reference.prefix + "roll "))
    	{
    		
    		//objMsgCh.sendMessage("Roll request received!").queue();
    		
    		int indexOfD = 0;
    		
    		//Iterate through the user input to see if it's in a valid format
    		boolean isValid = false;
    		
    		for (int i = 7; i < objMsg.getContentRaw().length(); i++) {
    			if (objMsg.getContentRaw().toLowerCase().charAt(i) == 'd') {
    				isValid = true;
    				indexOfD = i;
    				//objMsgCh.sendMessage("isValid is true!").queue();
    				//objMsgCh.sendMessage("indexOf'd': " + indexOfD).queue();
    				break;
    			}
    		}
    		
    		//If it is, then continue with the rest of the script
    		if (isValid) 
			{
    			
				//objMsgCh.sendMessage("Running script").queue();
    			
    			//Creating strings to hold string version of integers
        		String sNumDice = "";
        		String sNumSides = "";
        		String sNumAfter = "";
        		
        		//booleans to see if we do math after the roll
    			boolean addAfterRoll = false;
    			boolean subAfterRoll = false;
        		
        		try {
        			
            		//Iterate through the user input to see where the first number ends
        			for (int i = 6; i < objMsg.getContentRaw().length(); i++) {
        				if (isInteger(objMsg.getContentRaw().substring(i, i+1))){
        					sNumDice = sNumDice + objMsg.getContentRaw().substring(i, i+1);
        				} else break;
        			}
        			
        			int indexOfMath = 0;
        			
        			//Iterate through the user input to see where the last number ends, and if we need to add/subtract afterward
        			for (int i = indexOfD+1; i < objMsg.getContentRaw().length(); i++) {
        				
        				/*
        				//If there is a space, then check to see if there's a plus/minus sign
        				if (objMsg.getContentRaw().substring(i, i+1).contains(" "))
        				{
        					//If there is a plus sign after the space
            				if (objMsg.getContentRaw().substring(i+1, i+2).contains("+"))
            				{
            					//And there is something after the plus sign
            					if (isInteger(objMsg.getContentRaw().substring(i+2, i+3)))
            					{
            						//Set addAfterRoll to true and break out of the loop
	            					addAfterRoll = true;
	            					indexOfMath = i;
	            					break;
            					}
            				}
            				
            				//If there is a minus sign after the space
            				if (objMsg.getContentRaw().substring(i+1, i+2).contains("-"))
            				{
            					//There is something after the minus sign
            					if (isInteger(objMsg.getContentRaw().substring(i+2, i+3)))
            					{
            						//Set subAfterRoll to true and break out of the loop
	            					subAfterRoll = true;
	            					indexOfMath = i;
	            					break;
            					}
            				}
        				}*/
        				
        				//Check to see if we have to do math after
        				if (objMsg.getContentRaw().substring(i, i+1).contains("+"))
        				{
        					//If there is something after the plus sign
        					if (isInteger(objMsg.getContentRaw().substring(i+1, i+2)))
        					{
            					addAfterRoll = true;
            					indexOfMath = i;
            					break;
        					}
        				}
        				
        				if (objMsg.getContentRaw().substring(i, i+1).contains("-"))
        				{
        					//If there is something after the minus sign
        					if (isInteger(objMsg.getContentRaw().substring(i+1, i+2)))
        					{
            					subAfterRoll = true;
            					indexOfMath = i;
            					break;
        					}
        				}
        				
        				if (isInteger(objMsg.getContentRaw().substring(i, i+1)))
        					sNumSides= sNumSides + objMsg.getContentRaw().substring(i, i+1);
        				else break;
        			}
        			
        			//Gets the number after
        			if (addAfterRoll || subAfterRoll) {
        				for (int i = indexOfMath+1; i < objMsg.getContentRaw().length(); i++) {
        					if (isInteger(objMsg.getContentRaw().substring(i, i+1)))
        						sNumAfter= sNumAfter + objMsg.getContentRaw().substring(i, i+1);
        					else break;
        				}
        			}
        			
            		//If not then output error
        		} catch (Exception IndexOutOfBounds) {
        			objMsg.getTextChannel().sendMessage("Error: IndexOutOfBounds. What did you do?").queue();
        		}
        		
        		//objMsgCh.sendMessage("numDice: " + sNumDice).queue();
        		//objMsgCh.sendMessage("numSides: " + sNumSides).queue();
        		
        		if(isInteger(sNumDice) && isInteger(sNumSides)) {
        			
        			//Change the numbers in sNumDice and sNumSides from strings to integers
        			int numDice = Integer.parseInt(sNumDice);
        			int numSides = Integer.parseInt(sNumSides);
        			int numAfter = 0;
        			
        			if (addAfterRoll || subAfterRoll) {
        				numAfter = Integer.parseInt(sNumAfter);
        			}
        			
    				//objMsgCh.sendMessage("numDice: " + numDice).queue();
    				//objMsgCh.sendMessage("numDice: " + numSides).queue();
        			
        			//Sum variable to hold our roll totalumDice
        			int sum = 0;

        			int[] diceRolls = new int[numDice];
        			
    				//ArrayList of integers that holds all the rolls
    				List<Integer> rolls = new ArrayList<Integer>();

        			//Repeat dice roll for however many times there are dice
        			for (int i = 0; i < numDice; i++) {
        				int currentRoll = (int)(Math.random()*numSides) + 1;
        				
        				rolls.add(currentRoll);
        				
        				sum = sum + currentRoll;
        				
        				//objMsgCh.sendMessage("Roll: " + currentRoll).queue();
        			}	
        			
        			//Output result
        			if (addAfterRoll)
            			objMsg.getTextChannel().sendMessage("Rolling " + numDice + "d" + numSides + ": " + rolls + " = " + sum + " + " + numAfter + " = **(" + (sum + numAfter) + ")**").queue();
        			else if (subAfterRoll)
            			objMsg.getTextChannel().sendMessage("Rolling " + numDice + "d" + numSides + ": " + rolls + " = " + sum + " - " + numAfter + " = **(" + (sum - numAfter) + ")**").queue();
        			else
            			objMsg.getTextChannel().sendMessage("Rolling " + numDice + "d" + numSides + ": " + rolls + " = **(" + sum + ")**").queue();
        		}	
        		
			} else objMsgCh.sendMessage("Invalid format!").queue();

    	}
    	
    	//********* Administrator Commands *********\\
    	
    	if (isAdmin)
    	{
    		
    		//Clear multiple messages
        	if (objMsg.getContentRaw().toLowerCase().contains(Reference.prefix + "clear "))
    		{
        		String numString = "";
        		//Checks 7th number to see if it exists and is an integer
        		try {
        			String string = objMsg.getContentRaw();
            		numString = string.substring(7, Math.min(9, string.length()));
            		
        		} catch (Exception IndexOutOfBounds) {
        			objMsg.getTextChannel().sendMessage("You didn't put anything!").queue();
        		}
        		
        		//If the string contains an integer
        		if (isInteger(numString)) {
        			//Make the string into an integer to use in MessageHistory
            		int num = Integer.parseInt(numString);
        			
            		//Creates message history before the current message
            		
            		//limit(num+1) to delete the previous 'Clearing 'x' messages!' if it exists... probably doesn't work like that though, but meh
            		MessageHistory history = MessageHistory.getHistoryBefore(objMsgCh, objMsg.getId()).limit(num+1).complete();
            		
            		//Retrieve the messages in the MessageHistory
            		history.retrievePast(num+1);
            		
            		//Deletes messages using the retrieved messages
        			objMsgCh.purgeMessages(history.getRetrievedHistory());
        			
        			objMsg.getTextChannel().sendMessage("Clearing " + num + " messages!").queue();
        			
        		} /*else {
        			objMsg.getTextChannel().sendMessage("Error!").queue();
        		}*/
        	}
        	
        	//Test
        	if(objMsg.getContentRaw().equalsIgnoreCase(Reference.prefix+ "test"))
        	{
        		objMsgCh.sendMessage(objUser.getAsMention() + " Message received!").queue();
        	}
    	}
    }


public boolean isInteger(String input) {
	try {
		Integer.parseInt(input);
		return true;
	} catch (Exception e) {
		return false;
	}
}

}
