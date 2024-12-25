package cn.edu.seu.sky.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiaotian on 2022/4/16
 */
@Data
@ConfigurationProperties(prefix = "spring.data.hbase")
public class HbaseProperties {

    /**
     * host
     */
    private String host;

    /**
     * port
     */
    private String port;

    /**
     * node
     */
    private String nodeParent;

    /**
     * rpc timeout
     */
    private String rpcTimeout;

    /**
     * operation timeout
     */
    private String operationTimeout;

    /**
     * retry num
     */
    private String retryNum;
}
