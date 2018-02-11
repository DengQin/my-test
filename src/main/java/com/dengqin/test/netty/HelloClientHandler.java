package com.dengqin.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dq on 2018/2/11.
 */
public class HelloClientHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(HelloClientHandler.class);

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		byte[] req = "World".getBytes();
		ByteBuf firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
		ctx.writeAndFlush(firstMessage);
		System.out.println("客户端active");
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("客户端收到服务器响应数据");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("客户端收到 : " + body);

	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
		System.out.println("客户端收到服务器响应数据处理完成");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.warn("Unexpected exception from downstream:" + cause.getMessage());
		ctx.close();
		System.out.println("客户端异常退出");
	}
}
