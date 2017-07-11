package com.chencoder.rpc.core.transport.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.RpcMessage;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.core.cluster.DefaultCluster;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelDuplexHandler {

	private static final Logger logger = LoggerFactory.getLogger(DefaultCluster.class);
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				logger.debug("send ping msg");
				ctx.writeAndFlush(getHeartBeatMessage());
			} else if (e.state() == IdleState.WRITER_IDLE) {
				
			}
		}
	}

	private Object getHeartBeatMessage() {
		Header heartBeatHeader = Header.HeaderMaker.newMaker().make();
		heartBeatHeader.setExtend(EventType.HEARTBEAT.getValue());
		return new RpcRequest(heartBeatHeader, null);
	}

}
