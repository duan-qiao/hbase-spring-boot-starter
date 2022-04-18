package cn.edu.seu.sky.config;

import cn.edu.seu.sky.annotation.HConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiaotian on 2022/4/16
 */
@Data
@ConfigurationProperties(prefix = "spring.data.hbase")
public class HbaseProperties {
    /**
     * hbase.zookeeper.quorum
     */
    @HConfig("hbase.zookeeper.quorum")
    private String quorum;
    /**
     * zookeeper节点
     */
    @HConfig("zookeeper.znode.parent")
    private String zkNode;
    /**
     * zookeeper端口号
     */
    @HConfig("hbase.zookeeper.property.clientPort")
    private String clientPort;
    /**
     * 一次RPC请求的超时时间，一次scan操作可能会有多次rpc请求
     */
    @HConfig("hbase.rpc.timeout")
    private String rpcTimeout;
    /**
     * 客户端发起一次数据操作直至得到响应之间总的超时时间，包括get、append、increment、delete、put等
     */
    @HConfig("hbase.client.operation.timeout")
    private String clientOperationTimeout;
    /**
     * scan操作一次next操作的超时时间
     */
    @HConfig("hbase.client.scanner.timeout.period")
    private String scannerPeriodTimeout;
    /**
     * hbase.client.ipc.pool.type
     */
    @HConfig("hbase.client.ipc.pool.type")
    private String poolType;
    /**
     * hbase.client.ipc.pool.size
     */
    @HConfig("hbase.client.ipc.pool.size")
    private String poolSize;
    /**
     * hbase.client.write.buffer
     */
    @HConfig("hbase.client.write.buffer")
    private String writeBuffer;
    /**
     * rpc请求失败重试次数
     */
    @HConfig("hbase.client.retries.number")
    private String clientRetriesNumber;
    /**
     * 连续两次rpc重试之间的休眠时间
     */
    @HConfig("hbase.client.pause")
    private String clientPause;
    /**
     * 一次scan查询的最大条数
     */
    @HConfig("hbase.client.scanner.caching")
    private String clientScannerCaching;
}
