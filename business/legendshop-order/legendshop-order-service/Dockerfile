FROM harbor.legendshop.cn/legendshop-dev/oracle-jdk8:latest

MAINTAINER www.legendshop.cn

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
ENV LICENSE_URL="http://192.168.0.11:15151"
ENV NACOS_SETTING="-DNACOS_HOST=192.168.0.15 -DNACOS_PORT=8848 -DNACOS_NAMESPACE=dev-sr1 -DNACOS_USERNAME=nacos -DNACOS_PASSWORD=nacos"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /legendshop-order

WORKDIR /legendshop-order

EXPOSE 8085

ADD ./target/legendshop-order-service.jar ./
ADD service-license-fatjar-6.0.0.jar  ./
ADD docker-entrypoint.sh ./

#CMD sleep 120;java $JAVA_OPTS -jar legendshop-order-service.jar



CMD ["sh", "/legendshop-order/docker-entrypoint.sh"]