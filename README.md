# mybatis-extension
mybatis扩展，基于mybatis实现，提供CRUD通用操作，分页，内外连接查询，支持jpa注解，乐观锁，数据填充等功能，可以与其他orm框架一起使用

## 开始使用
- Maven:
```xml
<dependency>
    <groupId>io.github.dengchen2020</groupId>
    <artifactId>entity-extension</artifactId>
    <version>1.0.3</version>
</dependency>
```
默认识别数据库列为蛇形命名，需配置map-underscore-to-camel-case=true，可以配合表字段常量一起使用（可选）
- Maven:
```xml
<dependency>
    <groupId>io.github.dengchen2020</groupId>
    <artifactId>entity-processor</artifactId>
    <version>1.0.3</version>
</dependency>
```
下面版本使用旧版注解
- Maven:
```xml
<dependency>
    <groupId>io.github.dengchen2020</groupId>
    <artifactId>entity-extension-jx</artifactId>
    <version>1.0.3</version>
</dependency>
```
