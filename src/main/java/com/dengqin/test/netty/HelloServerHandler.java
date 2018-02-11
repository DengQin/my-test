package com.dengqin.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by dq on 2018/2/11.
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("服务器读取到客户端请求...");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("服务器接收到:" + body);

		String curentTime = "Hello " + body;
		ByteBuf resp = Unpooled.copiedBuffer(curentTime.getBytes());
		ctx.writeAndFlush(resp);
		System.out.println("服务器做出了响应：" + curentTime);
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("服务器channelReadComplete");
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();// 捕捉异常信息
		ctx.close();// 出现异常时关闭channel
	}
}
