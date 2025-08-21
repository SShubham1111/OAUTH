package com.http.server;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.*;

import com.authorization.threads.ClientAuthorizationThread;
import com.authorization.threads.ClientRegisterationThread;
import com.authorization.threads.TokenGenerationThread;
import com.resource.threads.ResourceAccessThread;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpRequestHandlerThread implements Runnable {
	private ChannelHandlerContext ctx;
	private FullHttpRequest httpRequest;


	@Override
	public void run() {
		
		
		    Thread.currentThread().setName("HttpRequestHandlerThread");
		    boolean isValidRequest = false;
		    try {
		        isValidRequest = this.validateHttpRequest();
		    } catch (Exception e) {
		        // Handle the exception, print stack trace, log, or take appropriate action.
		    }

		    if (isValidRequest) {
		        String applicationTypeHeader = this.httpRequest.headers().get("applicationType");
		        if (applicationTypeHeader != null && (applicationTypeHeader.equalsIgnoreCase("AuthorizationServer") || applicationTypeHeader.equalsIgnoreCase("ResourceServer"))) {
		            try {
		                handleHttpRequest();
		            } catch (Exception e) {
		                HttpUtils.sendHttpResponse(ctx, this.httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
		            }
		        } else {
		            HttpUtils.sendHttpResponse(ctx, this.httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
		        }
		    }
		}
	

	private void handleHttpRequest()
	{
		try {
			@SuppressWarnings("deprecation")
			URI uri = new URI(this.httpRequest.getUri());
			byte[] bytes = new byte[this.httpRequest.content().readableBytes()];
			this.httpRequest.content().getBytes(this.httpRequest.content().readerIndex(), bytes);
			String content = new String(bytes);
			switch(uri.getPath())
			{
			
			case HTTPContextInterface.REGISTER:
				ClientRegisterationThread clientRegisterationThread = new ClientRegisterationThread();
				clientRegisterationThread.setHttpRequest(httpRequest);
				clientRegisterationThread.setContent(content);
				clientRegisterationThread.setCtx(ctx);
				new Thread(clientRegisterationThread).start();;
				break;
			
			case HTTPContextInterface.AUTH:
				ClientAuthorizationThread clientAuthorizationThread = new ClientAuthorizationThread();
				clientAuthorizationThread.setHttpRequest(httpRequest);
				clientAuthorizationThread.setContent(content);
				clientAuthorizationThread.setCtx(ctx);
				new Thread(clientAuthorizationThread).start();;
				break;
				
			case HTTPContextInterface.TOKEN:
				TokenGenerationThread tokenGenerationThread = new TokenGenerationThread();
				tokenGenerationThread.setHttpRequest(httpRequest);
				tokenGenerationThread.setContent(content);
				tokenGenerationThread.setCtx(ctx);
				new Thread(tokenGenerationThread).start();
				break;
				
			case HTTPContextInterface.RESOURCE:
				ResourceAccessThread resourceAccessThread = new ResourceAccessThread();
				resourceAccessThread.setHttpRequest(httpRequest);
				resourceAccessThread.setContent(content);
				resourceAccessThread.setCtx(ctx);
				new Thread(resourceAccessThread).start();
				break;
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public FullHttpRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(FullHttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	@SuppressWarnings("deprecation")
	private boolean validateHttpRequest()
	{
		try {
			if(this.httpRequest == null)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("error-message", "http request is null");
				HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer(jsonObject.toString(),CharsetUtil.UTF_8)));
			return false;
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
		}
		
		try {
			if(!(this.httpRequest.getMethod() == HttpMethod.POST || this.httpRequest.getMethod() == HttpMethod.GET || this.httpRequest.getMethod() == HttpMethod.DELETE || this.httpRequest.getMethod() == HttpMethod.PUT || this.httpRequest.getMethod() == HttpMethod.OPTIONS))
			{
				JSONObject jsonObject = new  JSONObject();
				jsonObject.put("error-message", "Received Invalid HTTP Request Method");
				HttpUtils.sendHttpResponse(ctx, this.httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer(jsonObject.toString(),CharsetUtil.UTF_8)));
				return false;
			}
			if(this.httpRequest.getMethod() == HttpMethod.OPTIONS)
			{
				HttpUtils.sendHttpResponse(ctx, this.httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
			}
		}
		catch(Exception e)
		{
			
		}
		return true;
	}
}
