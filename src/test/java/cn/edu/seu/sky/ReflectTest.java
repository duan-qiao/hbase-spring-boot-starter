package cn.edu.seu.sky;

import cn.edu.seu.sky.common.ObjTypeEnum;
import cn.edu.seu.sky.model.HPutInfo;
import cn.edu.seu.sky.util.ReflectUtils;
import lombok.Data;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author xiaotian on 2022/4/17
 */
public class ReflectTest extends BaseTest {

    @Test
    public void test() throws Exception {
        Info instance = Info.class.newInstance();
        for (Field field : ReflectUtils.getAllClassFields(Info.class)) {
            field.setAccessible(true);
            Object object = ObjTypeEnum.typeConvert(field.getGenericType(), "1");
            if (object != null) {
                field.set(instance, object);
            }
        }
        System.out.println(instance);

        HPutInfo<Info> put = new HPutInfo<>();
        put.setValue(instance);
        for (Field field : ReflectUtils.getAllClassFields(put.getValue().getClass())) {
            field.setAccessible(true);
            Object value = field.get(put.getValue());
            byte[] bytes = Bytes.toBytes(value.toString());
            System.out.println(Bytes.toString(bytes));
        }
    }

    @Data
    public static class Info {
        private Integer int1;
        private int int2;

        private Long long1;
        private long long2;

        private Float float1;
        private float float2;

        private Double double1;
        private double double2;

        private Short short1;
        private short short2;

        private Byte byte1;
        private byte byte2;

        private Boolean bool1;
        private boolean bool2;

        private String c;
    }
}
