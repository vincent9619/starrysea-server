package top.starrysea.rina.core;
import lombok.extern.flogger.Flogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    public static void ExecuteNio() throws IOException {
        // 创建 ServerSocketChannel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8888));

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
                logger.info("Link Start");
                ThreadUtil.exec(httpHandler::run);
                logger.info("Link End");

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
                logger.info("Link Failed");
                channel.close();
            } else {
                logger.info("Link Succeed");
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
