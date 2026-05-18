# 第一阶段：构建 Jar 包
FROM maven:3.8.6-openjdk-8 AS builder

WORKDIR /app

# 先复制 pom.xml，缓存依赖
COPY pom.xml .
# 复制源码
COPY src ./src

# 执行打包命令，跳过测试
RUN mvn clean package -DskipTests

# 第二阶段：运行
FROM eclipse-temurin:8-jre

WORKDIR /app

# 从构建阶段复制打好的 Jar 包
COPY --from=builder /app/target/campus-health-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 10000

CMD ["java", "-Xmx300m", "-Xms300m", "-jar", "app.jar"]