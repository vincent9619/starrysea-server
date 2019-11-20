package top.starrysea.rina.core;
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

    public void executeNio() {

        ServerConfig serverConfig= new ServerConfig();
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()){
            serverChannel.bind(new InetSocketAddress(serverConfig.getPort()));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select(serverConfig.getWaitTime()) == 0) {
                    continue;
                }

            // 获取待处理的selectionKey
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                log.info("开始建立与客户端连接");
                httpHandler(key);
                log.info("结束与客户端连接");
            }
        }
        } catch (ClosedChannelException e) {
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }


    // 处理请求

    private void httpHandler(SelectionKey key){
        // TCP包长度
        int bufferSize = 1024;
        // 编码字符集
        String charset = "UTF-8";

        if (key.isAcceptable()) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            // 接收的客户端的连接
            try{
                SocketChannel channel = serverChannel.accept();
                channel.configureBlocking(false);
                // 注册到选择器
                channel.register(key.selector(), SelectionKey.OP_READ,
                        ByteBuffer.allocate(bufferSize));
            } catch (ClosedChannelException e) {
                log.error(e.getMessage(),e);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        if (key.isReadable()) {
            try(SocketChannel channel = (SocketChannel) key.channel()){
                // 获取Buffer并重置
                ByteBuffer buffer = (ByteBuffer) key.attachment();
                buffer.clear();
                // 从通道获取内容

                if (channel.read(buffer) == -1) {
                    log.info("连接失败");
                } else {
                    log.info("连接成功");
                }
            } catch (IOException ex) {
                log.error(ex.getMessage(),ex);
            }
        }
    }
}















































/*@Slf4j
public class HttpNIO {

  public void executeNio() {

        try {ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(RinaObjectFactory.getRinaObject(ServerConfig.class).getPort()));
            serverChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            ThreadUtil ex = new ThreadUtil();
                try {
                    if (selector.select(RinaObjectFactory.getRinaObject(ServerConfig.class).getWaitTime()) == 0) {
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
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
        } catch (ClosedChannelException e) {
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
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

            try(SocketChannel channel = (SocketChannel) key.channel()){
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
*/




