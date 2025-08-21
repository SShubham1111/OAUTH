package com.resource.threads;

import com.authorization.data.Data;
import com.google.gson.Gson;
import com.http.server.HttpUtils;
import com.resource.Resource;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class ResourceAccessThread implements Runnable{

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
		if(httpRequest.headers()!=null && httpRequest.headers().contains("Authorization") && httpRequest.headers().get("Authorization")!=null && ! httpRequest.headers().get("Authorization").isEmpty() ) {
		String authorizationHeader = httpRequest.headers().get("Authorization");
		
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
		{
			String token = authorizationHeader.substring(7);
			if(Data.access(token))
			{
				Resource resource = new Resource();
				HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.ACCEPTED, Unpooled.copiedBuffer(resource.resourceAPI(),CharsetUtil.UTF_8)));
			}
			else
			{
				HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
			}
		
		}
		else
		{
			HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
		}
	}
		else
		{
			HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
		}
		}


}
