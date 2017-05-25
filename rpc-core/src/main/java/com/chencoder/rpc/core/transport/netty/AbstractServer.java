package com.chencoder.rpc.core.transport.netty;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.config.ServerConfig;
import com.chencoder.rpc.core.transport.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Dempe on 2016/12/22.
 */
public abstract class AbstractServer implements Server{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServer.class);

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap bootstrap;
    private Channel channel;
    protected ServerConfig config;
    protected int port;

    protected enum ServerState {Created, Starting, Started, Shutdown}

    protected final AtomicReference<NettyServer.ServerState> serverStateRef;

    public AbstractServer(ServerConfig config, int port) throws InterruptedException {
        this.port = port;
        this.config = config;
        serverStateRef = new AtomicReference<>(AbstractServer.ServerState.Created);
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();

        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, config.getSoBacklog())
                .option(ChannelOption.SO_KEEPALIVE, config.getSoKeepAlive())
                .option(ChannelOption.TCP_NODELAY, config.getTcpNoDelay())
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(newChannelInitializer());
    }

    public abstract ChannelInitializer<SocketChannel> newChannelInitializer();


    public void start() {
        if (!serverStateRef.compareAndSet(AbstractServer.ServerState.Created, AbstractServer.ServerState.Starting)) {
            throw new IllegalStateException("Server already started");
        }
        ChannelFuture channelFuture = bootstrap.bind(port);
       /* LOGGER.info("Server bind port:{}, soBacklog:{}, soKeepLive:{}, tcpNodDelay:{}", port,
                config.getSoBacklog(), config.getSoKeepAlive(), config.getTcpNoDelay() );*/

        serverStateRef.set(NettyServer.ServerState.Started); // It will come here only if this was the thread that transitioned to Starting
        channel = channelFuture.channel();
        channel.closeFuture();
    }


    public void startAndWait() throws InterruptedException {
        start();
        try {
            waitTillShutdown();
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    public void shutdown() throws InterruptedException {
        if (!serverStateRef.compareAndSet(ServerState.Started,ServerState.Shutdown)) {
            throw new IllegalStateException("The server is already shutdown.");
        } else {
            channel.close().sync();
        }
    }

    public void waitTillShutdown() throws InterruptedException {
        NettyServer.ServerState serverState = serverStateRef.get();
        switch (serverState) {
            case Created:
            case Starting:
                throw new IllegalStateException("Server not started yet.");
            case Started:
                channel.closeFuture().await();
                break;
            case Shutdown:
                // Nothing to do as it is already shutdown.
                break;
        }
    }

    public void close() {
        if (boss != null)
            boss.shutdownGracefully().awaitUninterruptibly(15000);
        if (worker != null)
            worker.shutdownGracefully().awaitUninterruptibly(15000);
        LOGGER.info("Server stopped...");
    }

}
