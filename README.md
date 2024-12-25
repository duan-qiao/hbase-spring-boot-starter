# hbase-spring-boot-starter

### POM 引入
```xml
<dependency>
    <groupId>cn.edu.seu.sky</groupId>
    <artifactId>hbase-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### application.yml 配置
```yaml
spring:
  data:
    hbase:
      host: 110.159.121.179,110.159.121.136,110.159.121.158
      port: 2181
      node-parent: /hbase
```

### 使用
定义实体类
```java
@Data
@HTable(name = "reverse_geo_address", family = "address")
public class ReverseGeoEntity {
    /**
     * 位置信息
     * 存储时这里是坐标lon,lat
     * 查询时这里是geoHash
     */
    private String location;

    /**
     * 结构化地址
     */
    private String formattedAddress;

    /**
     * 道路id 列表
     */
    @HColumn(qualifier = "roads")
    private List<String> roadIds;

    /**
     * poi id列表
     */
    @HColumn(qualifier = "pois")
    private List<String> poiIds;

    /**
     * aoi id列表
     */
    @HColumn(qualifier = "aois")
    private List<String> aoiIds;

    /**
     * 时间戳
     */
    private Long timestamp;
}
```
dao层操作
```java
@Repository
public class ReverseGeoDao {

    @Resource
    private HbaseTemplate hbaseTemplate;

    public ReverseGeoEntity getReverseGeo(Double lon, Double lat) {
        String rowKey = GeoHashUtil.toGeoHashStr(lat, lon, Constants.GEO_HASH_PRECISION);
        return hbaseTemplate.get(rowKey, ReverseGeoEntity.class);
    }

    public List<ReverseGeoEntity> batchGetReverseGeo(List<Coordinate> coordinates) {
        List<String> rowKeys = coordinates.stream()
                .map(x -> GeoHashUtil.toGeoHashStr(x.getLat(), x.getLon(), Constants.GEO_HASH_PRECISION))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        return hbaseTemplate.batchGet(rowKeys, ReverseGeoEntity.class);
    }

    public void saveReverseGeo(ReverseGeoEntity entity) {
        String rowKey = GeoHashUtil.toGeoHashStr(entity.getLocation(), Constants.GEO_HASH_PRECISION);
        hbaseTemplate.put(rowKey, entity);
    }
}
```
更多api
```
com.qmove.hbase.template.HbaseOperations
```