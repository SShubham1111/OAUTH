package com.authorization.threads;

import com.authorization.data.Data;
import com.authorization.pojos.Client;
import com.authorization.pojos.Credentials;
import com.google.gson.Gson;
import com.http.server.HttpUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class ClientRegisterationThread implements Runnable{
	private ChannelHandlerContext ctx;
	private FullHttpRequest httpRequest;
	private String content;
	Gson gson = new Gson();

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


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public void run() {
		try {
		if(content!=null) {	
		Client client = gson.fromJson(content, Client.class);
		Credentials credentials = Data.addClient(client);
		if(credentials!=null)
		HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CREATED, Unpooled.copiedBuffer(gson.toJson(credentials).toString(),CharsetUtil.UTF_8)));
		else
			HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
		}
		else
		{
			HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
		}
		}
		catch(Exception e)
		{
			HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
		}
		}

}
