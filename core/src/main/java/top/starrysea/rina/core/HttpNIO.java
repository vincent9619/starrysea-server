package top.starrysea.rina.core;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.thread.ThreadUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
@Slf4j
public class HttpNIO {

    public static void executeNio() {
        // 创建 ServerSocketChannel
        try {ServerSocketChannel serverChannel = ServerSocketChannel.open();

            serverChannel.bind(new InetSocketAddress(RinaObjectFactory.getRinaObject(ServerConfig.class).getPort()));

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
                try {
                    if (selector.select(500) == 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 获取待处理的selectionKey
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    HttpHandler httpHandler = new HttpHandler(key);
                    log.info("开始建立与客户端连接");
                    ThreadUtil.exec(httpHandler::run);
                    log.info("结束与客户端连接");

                }
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

            try(SocketChannel channel = (SocketChannel) key.channel()){;
            // 获取Buffer并重置
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            buffer.clear();
            // 从通道获取内容

            if (channel.read(buffer) == -1) {
                log.info("连接失败");
                channel.close();
            } else {
                log.info("连接成功");
                channel.close();
            }
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
