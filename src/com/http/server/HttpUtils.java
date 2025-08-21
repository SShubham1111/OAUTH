package com.http.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

public class HttpUtils {
public static void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest request, FullHttpResponse response)
{
	try {
		HttpUtil.setContentLength(response, response.content().readableBytes());
		ChannelFuture channelFuture = context.channel().writeAndFlush(response);
		
		if(!HttpUtil.isKeepAlive(request))
		{
			channelFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}
	catch(Exception e)
	{
		
	}
	}
}
