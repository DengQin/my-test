package com.dengqin.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by dq on 2018/2/11.
 */
public class HelloClient {
	public void connect(int port, String host) throws Exception {
		// 配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 客户端辅助启动类 对客户端配置
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel channel) throws Exception {
							channel.pipeline().addLast(new HelloClientHandler());
						}
					});
			// 异步链接服务器 同步等待链接成功
			ChannelFuture f = b.connect(host, port).sync();
			// 等待链接关闭
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
			System.out.println("客户端优雅的释放了线程资源...");
		}
	}

	public static void main(String[] args) throws Exception {
		new HelloClient().connect(8080, "127.0.0.1");
	}
}
