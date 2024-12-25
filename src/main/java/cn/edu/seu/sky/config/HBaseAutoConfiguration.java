package cn.edu.seu.sky.config;

import cn.edu.seu.sky.template.HbaseTemplate;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Optional;

/**
 * @author xiaotian on 2022/4/16
 */
@Configuration
@ConditionalOnClass(HbaseTemplate.class)
@EnableConfigurationProperties(HbaseProperties.class)
public class HBaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Connection hbaseConnectionFactory(HbaseProperties properties) throws IOException {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", properties.getHost());
        configuration.set("hbase.zookeeper.property.clientPort", properties.getPort());
        configuration.set("zookeeper.znode.parent", properties.getNodeParent());
        configuration.set("hbase.rpc.timeout", Optional.ofNullable(properties.getRpcTimeout()).orElse("1000"));
        configuration.set("hbase.client.operation.timeout", Optional.ofNullable(properties.getOperationTimeout()).orElse("1000"));
        configuration.set("hbase.client.retries.number", Optional.ofNullable(properties.getRetryNum()).orElse("3"));
        return ConnectionFactory.createConnection(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public HbaseTemplate hbaseTemplate(Connection connection) {
        return new HbaseTemplate(connection);
    }
}
