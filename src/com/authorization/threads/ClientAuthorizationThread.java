package com.authorization.threads;

import com.authorization.data.Data;
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

public class ClientAuthorizationThread implements Runnable {
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
		if (httpRequest.headers() != null && httpRequest.headers().contains("name")
				&& httpRequest.headers().contains("password") && httpRequest.headers().get("name") != null
				&& httpRequest.headers().get("password") != null) {
			String name = httpRequest.headers().get("name");
			String password = httpRequest.headers().get("password");

			if (name != null && !name.isEmpty() && password != null && !password.isEmpty()) {
				Credentials credentials = Data.authenticate(name, password);
				if (credentials != null) {
					HttpUtils.sendHttpResponse(ctx, httpRequest,
							new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CREATED,
									Unpooled.copiedBuffer(gson.toJson(credentials).toString(), CharsetUtil.UTF_8)));
				} else {
					HttpUtils.sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
							HttpResponseStatus.INTERNAL_SERVER_ERROR));
				}
			} else {
				HttpUtils.sendHttpResponse(ctx, httpRequest,
						new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			}
		} else {
			HttpUtils.sendHttpResponse(ctx, httpRequest,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
		}
	}

}
