package top.starrysea.rina.core.connection;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.core.annotation.RinaWired;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.string.StringUtil;
import top.starrysea.rina.util.thread.ThreadUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@Slf4j
@RinaObject
public class HttpNIO {
    @RinaWired
    private HttpMessageResolver httpMessageResolver;

    private static boolean isStart = false;


    public static void yame() {
        isStart = false;
    }

    public void executeNio() {
        ServerConfig serverConfig = RinaObjectFactory.getRinaObject(ServerConfig.class);
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            serverChannel.bind(new InetSocketAddress(serverConfig.getPort()));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            isStart = true;
            while (isStart) {
                if (selector.select(serverConfig.getWaitTime()) == 0) {
                    continue;
                }
                // 获取待处理的selectionKey
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey Key = it.next();
                    log.info("开始建立与客户端连接");
                    ThreadUtil.exec(() -> httpHandler(Key));
                    log.info("结束与客户端连接");
                    it.remove();
                }
            }
        } catch (ClosedChannelException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    // 处理请求

    private void httpHandler(SelectionKey key) {
        // TCP包长度
        int bufferSize = 1024;
        // 编码字符集
        String charset = "UTF-8";
        if (key.isAcceptable()) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            // 接收的客户端的连接
            try {
                SocketChannel channel = serverChannel.accept();
                if (channel == null) {
                    return;
                }
                channel.configureBlocking(false);
                // 注册到选择器
                channel.register(key.selector(), SelectionKey.OP_READ,
                        ByteBuffer.allocate(bufferSize));


            } catch (ClosedChannelException e) {
                key.cancel();
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        if (key.isReadable()) {
            try (SocketChannel channel = (SocketChannel) key.channel()) {
                // 获取Buffer并重置
                ByteBuffer buffer = (ByteBuffer) key.attachment();
                buffer.clear();
                // 从通道获取内容
                if (channel.read(buffer) == -1) {
                    channel.close();
                    log.info("连接失败");
                }
                log.info("连接成功");
                buffer.flip();
                String receiveMessage = Charset.forName(charset).newDecoder().decode(buffer).toString();
                if (StringUtil.isBlank(receiveMessage)) {
                    return;
                } else {
                    List<String> requestContent = Arrays.asList(receiveMessage.split("\r\n"));
                    requestContent.stream().forEach(log::info);
                    httpMessageResolver.handleRun(requestContent);
                    log.info(String.valueOf(httpMessageResolver.contentAndQualityAcceptLanguageList));
                    log.info(String.valueOf(httpMessageResolver.contentAndQualityAcceptEncodingList));
                    log.info(String.valueOf(httpMessageResolver.contentAndQualityAcceptList));
                    log.info(String.valueOf(httpMessageResolver.httpMethod));


                    // 返回客户端
                    StringBuilder sendMsg = new StringBuilder();
                    sendMsg.append("HTTP/1.1 200 OK\r\n");// 响应行
                    // 响应头
                    sendMsg.append("cache-control: private;\r\n")
                            .append("content-type: text/html; charset=utf-8\r\n")
                            .append("\r\n")
                            // 响应体
                            .append("<!DOCTYPE html><html lang=\"zh-cn\">")
                            .append("<head><meta charset=\"utf-8\"/><title>测试HttpServer</title></head>")
                            .append("<body><h3>服务端接收到的请求报文</h3>");
                    for (String line : requestContent) {
                        sendMsg.append(line + "</br>");
                        if (line.isEmpty()) {
                            break;
                        }
                    }
                    sendMsg.append("</body>");
                    buffer = ByteBuffer.wrap(sendMsg.toString().getBytes(charset));
                    // 发送
                    channel.write(buffer);
                    channel.close();
                }

            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
}



