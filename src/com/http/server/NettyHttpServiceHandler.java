package com.http.server;
import java.net.SocketException;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
public class NettyHttpServiceHandler extends ChannelDuplexHandler {
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	try {
		if(msg instanceof FullHttpRequest)
		{
			HttpRequestHandlerThread requestHandlerThread = new HttpRequestHandlerThread();
			requestHandlerThread.setCtx(ctx);
			requestHandlerThread.setHttpRequest((FullHttpRequest) msg);
			Thread thread = new Thread(requestHandlerThread);
			thread.start();
		}
	}
	catch(Exception e)
	{
		System.out.println("Exception");
	}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		super.exceptionCaught(ctx, cause);
//		System.out.println("Warning Exception");
		if(cause instanceof SocketException)
		{
			System.out.println("Socket Exception");
		}
		
		ctx.fireExceptionCaught(cause);
	}
}
