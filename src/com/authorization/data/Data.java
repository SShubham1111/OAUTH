package com.authorization.data;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import com.authorization.pojos.Client;
import com.authorization.pojos.Credentials;
import com.authorization.pojos.Token;

public class Data {
private static HashMap<String, ArrayList<String>> clients = new HashMap<String, ArrayList<String>>();
private static HashMap<String, String> credentials = new HashMap<String, String>();
private static HashMap<String, String> tokens = new HashMap<String, String>();

private static String generateRandomString(String characters, int length)
{
	SecureRandom secureRandom = new SecureRandom();
	StringBuilder sb = new StringBuilder();
	
	for(int i = 0; i<length; i++)
	{
		int randomIndex = secureRandom.nextInt(characters.length());
		char ranomChar = characters.charAt(randomIndex);
		sb.append(ranomChar);
	}
	return sb.toString();
	}


public static Credentials addClient(Client client)
{
	try {
	ArrayList<String> clientData = new ArrayList<String>();
	ArrayList<String> credentialsData = new ArrayList<String>();
	clientData.add(client.getPassword());
	clientData.add(client.getContact());
	clientData.add(client.getAddress());
	clientData.add(client.getEmailid());
	String clientID = generateRandomString("0123456789", 15);
	String clientSecret = generateRandomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvwxyz", 32);
	clientData.add(clientID);
	clients.put(client.getName(), clientData);
	
	credentialsData.add(clientID);
	credentialsData.add(clientSecret);
	credentials.put(clientID, clientSecret);
	
	return new Credentials(clientID, clientSecret);
	}
	catch(Exception e)
	{
	return null;
	}
}


public static Credentials authenticate(String name, String password)
{
	if(clients.containsKey(name) && clients.get(name).get(0).equals(password))
	{
		return new Credentials(clients.get(name).get(4), credentials.get(clients.get(name).get(4)));
	}
	
	return null;
	}


public static boolean authorize(String clientid, String clientsecret)
{
	if(credentials.get(clientid).equals(clientsecret))
	{
		return true;
	}
	return false;
	}

public static Token token(String clientid, String clientsecret)
{
	if(credentials.containsKey(clientid) && credentials.get(clientid).contentEquals(clientsecret))
	{String accesstoken = generateRandomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvwxyz", 255);
	tokens.put(accesstoken, clientid);
	return new Token(accesstoken,"Bearer");}
	return null;
	}

public static boolean access(String accesstoken)
{
	if(tokens.containsKey(accesstoken))
		return true;
	else
		return false;
	}
public static boolean removeClient(String name, String password)
{
	if(clients.containsKey(name) && clients.get(name).get(0).equals(password))
	{
		String clientID = clients.get(name).get(4);
		credentials.remove(clientID);
		clients.remove(name);
		return true;
	}
	
	return false;
	}
}
