package top.statrysea.rina.core;
import top.starrysea.rina.util.thread.ThreadUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class HttpNIO {

    public static void ExecuteNio() throws IOException {
        // 创建 ServerSocketChannel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(9999));
        // 配置为非阻塞
        serverChannel.configureBlocking(false);
        // 注册选择器
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 创建线程池
        ThreadUtil ex = new ThreadUtil();
        //Executor ex = new ThreadPoolExecutor(8, 8, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        // 创建处理器
        while (true) {
            // 每次等待0.5秒，0.5秒后返回的就绪通道为0则继续阻塞
            if (selector.select(500) == 0) {
                continue;
            }
            // 获取待处理的seletionKey
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                HttpHandler httpHandler = new HttpHandler(key);
                //ex.exec(httpHandler);
                //ThreadUtil.exec(httpHandler);
                ThreadUtil.exec(httpHandler::run);

            }
        }
    }

    // 处理请求
    private static class HttpHandler implements Runnable {
        // TCP包长度
        private int bufferSize = 1024;
        // 编码字符集
        private String charset = "UTF-8";
        // 注册的事件
        private SelectionKey key;

        //@org.jetbrains.annotations.Contract(pure = true)
        public HttpHandler(SelectionKey key) {
            this.key = key;
        }

        // 处理ServerSocketChannel接受请求
        public void handleAccept() throws Exception {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            // 接收的客户端的连接

            SocketChannel channel = serverChannel.accept();
            channel.configureBlocking(false);
            // 注册到选择器
            channel.register(key.selector(), SelectionKey.OP_READ,
                    ByteBuffer.allocate(bufferSize));
            //channel.close();
        }

        // socketChannel 读取数据
        public void handleRead() throws Exception {
            // 获取channel
            SocketChannel channel = (SocketChannel) key.channel();
            // 获取Buffer并重置
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            buffer.clear();
            // 从通道获取内容
            if (channel.read(buffer) == -1) {
                channel.close();
            } else {
                // 接受请求数据
                buffer.flip();
                String reciveMsg = Charset.forName(charset).newDecoder().decode(buffer).toString();
                List<String> requestContent = Arrays.asList(reciveMsg.split("\r\n"));


                for(int i=0; i<requestContent.size() ;i++){
                    System.out.println(requestContent.get(i));
                }



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
                    sendMsg.append(line+"</br>");
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
        }

        @Override
        public void run() {
            try {
                if (key.isAcceptable()) {
                    handleAccept();
                }
                if (key.isReadable()) {
                    handleRead();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
