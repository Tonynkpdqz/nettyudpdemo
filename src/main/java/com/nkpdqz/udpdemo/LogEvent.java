package com.nkpdqz.udpdemo;

import java.net.InetSocketAddress;

public final class LogEvent {
    public static final byte SEPARATOR = (byte)':';
    private final InetSocketAddress source;
    private final String logfile;
    private final String msg;
    private final long received;

    public LogEvent(InetSocketAddress source, String logfile, String msg, long received) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }
    public LogEvent(String logfile,String msg){
        this(null,logfile,msg,-1);
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceived() {
        return received;
    }
}
