package com.nkpdqz.udpdemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf data = datagramPacket.content();
        int idx = data.indexOf(0,data.readableBytes(),LogEvent.SEPARATOR);
        String filename = data.slice(0,idx).toString(CharsetUtil.UTF_8);
        String logMsg = data.slice(idx+1,data.readableBytes()).toString(CharsetUtil.UTF_8);
        LogEvent event = new LogEvent(datagramPacket.sender(),filename,logMsg,System.currentTimeMillis());
        list.add(event);
    }
}
