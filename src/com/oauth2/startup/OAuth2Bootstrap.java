package com.oauth2.startup;

import com.http.server.NettyHttpServer;

public class OAuth2Bootstrap {
	
public static void main(String[] args) {
	try {
//		schema();
		NettyHttpServer.initializeSystem();
	} catch (InterruptedException e) {
		System.out.println("Interrupted Exception");
	}
}
}
