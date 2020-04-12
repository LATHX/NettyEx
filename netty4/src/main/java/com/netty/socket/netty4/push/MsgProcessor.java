package com.netty.socket.netty4.push;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.json.simple.JSONObject;

public class MsgProcessor {
    private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    private static final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    private static final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");
    private static final AttributeKey<String> FROM = AttributeKey.valueOf("from");

    private IMDecoder decoder = new IMDecoder();
    private IMEncoder encoder = new IMEncoder();

    public String getNickname(Channel client) {
        return client.attr(NICK_NAME).get();
    }

    public String getAddress(Channel client) {
        return client.remoteAddress().toString().replaceFirst("/", "");
    }

    public JSONObject getAttrs(Channel client) {
        return client.attr(ATTRS).get();
    }

    public void logout(Channel client) {
        for (Channel channel : onlineUsers) {
            IMMessage request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), getNickname(client) + "离开");
            String content = encoder.encode(request);
            channel.writeAndFlush(new TextWebSocketFrame(content));
        }
        onlineUsers.remove(client);
    }

    public Long sysTime() {
        return System.currentTimeMillis();
    }

    /**
     * 发送消息
     *
     * @param client
     * @param msg
     */
    public void sendMsg(Channel client, IMMessage msg) {

        sendMsg(client, encoder.encode(msg));
    }

    /**
     * 获取扩展属性
     *
     * @param client
     * @return
     */
    private void setAttrs(Channel client, String key, Object value) {
        try {
            JSONObject json = client.attr(ATTRS).get();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        }
    }

    /**
     * 获取用户昵称
     *
     * @param client
     * @return
     */
    public String getNickName(Channel client) {
        return client.attr(NICK_NAME).get();
    }

    /**
     * 发送消息
     *
     * @param client
     * @param msg
     */
    public void sendMsg(Channel client, String msg) {

        IMMessage request = decoder.decode(msg);
        if (null == request) {
            return;
        }

        String addr = getAddress(client);

        if (request.getCmd().equals(IMP.LOGIN.getName())) {
            client.attr(NICK_NAME).getAndSet(request.getSender());
            client.attr(IP_ADDR).getAndSet(addr);
            client.attr(FROM).getAndSet(request.getTerminal());
//			System.out.println(client.attr(FROM).get());
            onlineUsers.add(client);

            for (Channel channel : onlineUsers) {
                boolean isself = (channel == client);
                if (!isself) {
                    request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), getNickName(client) + "加入");
                } else {
                    request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), "已与服务器建立连接！");
                }

                if ("Console".equals(channel.attr(FROM).get())) {
                    channel.writeAndFlush(request);
                    continue;
                }
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        } else if (request.getCmd().equals(IMP.CHAT.getName())) {
            for (Channel channel : onlineUsers) {
                boolean isself = (channel == client);
                if (isself) {
                    request.setSender("you");
                } else {
                    request.setSender(getNickName(client));
                }
                request.setTime(sysTime());

                if ("Console".equals(channel.attr(FROM).get()) & !isself) {
                    channel.writeAndFlush(request);
                    continue;
                }
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        } else if (request.getCmd().equals(IMP.FLOWER.getName())) {
            JSONObject attrs = getAttrs(client);
            long currTime = sysTime();
            if (null != attrs) {
                long lastTime = Long.valueOf(attrs.get("lastFlowerTime").toString());
                //60秒之内不允许重复刷鲜花
                int secends = 10;
                long sub = currTime - lastTime;
                if (sub < 1000 * secends) {
                    request.setSender("you");
                    request.setCmd(IMP.SYSTEM.getName());
                    request.setContent("您送鲜花太频繁," + (secends - Math.round(sub / 1000)) + "秒后再试");

                    String content = encoder.encode(request);
                    client.writeAndFlush(new TextWebSocketFrame(content));
                    return;
                }
            }

            //正常送花
            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    request.setSender("you");
                    request.setContent("你给大家送了一波鲜花雨");
                    setAttrs(client, "lastFlowerTime", currTime);
                } else {
                    request.setSender(getNickName(client));
                    request.setContent(getNickName(client) + "送来一波鲜花雨");
                }
                request.setTime(sysTime());

                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
    }
}
