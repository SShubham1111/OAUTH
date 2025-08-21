package com.authorization.threads;

import com.authorization.data.Data;
import com.authorization.pojos.Token;
import com.google.gson.Gson;
import com.http.server.HttpUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class TokenGenerationThread implements Runnable {
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
		if (httpRequest.headers() != null && httpRequest.headers().contains("clientid")
				&& httpRequest.headers().contains("clientsecret") && httpRequest.headers().get("clientid") != null
				&& httpRequest.headers().get("clientsecret") != null) {
			String clientid = httpRequest.headers().get("clientid");
			String clientsecret = httpRequest.headers().get("clientsecret");

			if (clientid != null && !clientid.isEmpty() && clientsecret != null && !clientsecret.isEmpty()) {
				Token token = Data.token(clientid, clientsecret);
				if (token != null)
					HttpUtils.sendHttpResponse(ctx, httpRequest,
							new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.ACCEPTED,
									Unpooled.copiedBuffer(gson.toJson(token).toString(), CharsetUtil.UTF_8)));
				else
					HttpUtils.sendHttpResponse(ctx, httpRequest,
							new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
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
