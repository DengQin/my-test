package com.dengqin.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by dq on 2018/2/11.
 */
public class HelloServer {
	public void bind(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// NIO服务器端的辅助启动类 降低服务器开发难度
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel channel) throws Exception {
							channel.pipeline().addLast(new HelloServerHandler());
						}
					});
			// 服务器启动后 绑定监听端口 同步等待成功
			// 调用sync() 方法会阻塞
			ChannelFuture f = serverBootstrap.bind(port).sync();
			System.out.println("Server启动");
			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出 释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			System.out.println("服务器优雅的释放了线程资源...");
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		new HelloServer().bind(port);
	}
}
