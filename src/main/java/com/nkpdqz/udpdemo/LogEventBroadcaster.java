package com.nkpdqz.udpdemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address,File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws InterruptedException, IOException {
        Channel channel = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        for (;;){
            long len = file.length();
            if (len<pointer){
                pointer = len;
            }else if (len>pointer){
                RandomAccessFile raf = new RandomAccessFile(file,"r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine())!=null){
                    channel.writeAndFlush(new LogEvent(null,file.getAbsolutePath(),line,-1));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            Thread.sleep(1000);
        }
    }

    public void stop(){
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        if (args.length!=2){
            throw new IllegalArgumentException();
        }

        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress(
                "255.255.255.255",Integer.parseInt(args[0])),new File(args[1]));
        try {
            broadcaster.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            broadcaster.stop();
        }
    }

}
