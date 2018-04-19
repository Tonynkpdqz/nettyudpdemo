package com.nkpdqz.udpdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LogEvent logEvent) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(logEvent.getReceived());
        builder.append("[");
        builder.append(logEvent.getSource().toString());
        builder.append("] [");
        builder.append(logEvent.getLogfile());
        builder.append("] : ");
        builder.append(logEvent.getMsg());
        System.out.println(builder.toString());
    }
}
