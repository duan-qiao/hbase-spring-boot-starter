package cn.edu.seu.sky.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author xiaotian.zhou
 * @date 2020/11/5 11:32
 */
public class HbaseConnectionFactory {

    private final Configuration configuration;

    private volatile Connection connection;

    public HbaseConnectionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Connection getConnection() throws IOException {
        if (connection != null) {
            return connection;
        }
        synchronized (this) {
            if (connection == null) {
                connection = ConnectionFactory.createConnection(configuration);
            }
        }
        return connection;
    }
}
