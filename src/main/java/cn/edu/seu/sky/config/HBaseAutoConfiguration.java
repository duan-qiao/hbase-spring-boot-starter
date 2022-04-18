package cn.edu.seu.sky.config;

import cn.edu.seu.sky.annotation.HConfig;
import cn.edu.seu.sky.util.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * @author xiaotian on 2022/4/16
 */
@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(HbaseProperties.class)
@ConditionalOnClass(HbaseConnectionFactory.class)
public class HBaseAutoConfiguration {

    @Resource
    private HbaseProperties properties;

    @Bean
    @ConditionalOnMissingBean(HbaseConnectionFactory.class)
    public HbaseConnectionFactory hbaseConnectionFactory() {
        Field[] fields = HbaseProperties.class.getDeclaredFields();
        Configuration configuration = HBaseConfiguration.create();
        for (Field field : fields) {
            String key = field.getAnnotation(HConfig.class).value();
            Object value = ReflectUtils.invokeGetMethod(properties, field.getName());
            if (StringUtils.isNotBlank(key) && value != null) {
                configuration.set(key, (String) value);
            }
        }
        return new HbaseConnectionFactory(configuration);
    }
}
