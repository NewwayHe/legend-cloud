如何打包并推上harbor 见142服务器：

先替换target里的jar包

执行命令：
sudo docker build -t harbor.legendshop.cn/legendshop6.0/legendshop-order-service:LS.7.0.0

sudo docker push harbor.legendshop.cn/legendshop6.0/legendshop-order-service:LS.7.0.0

docker images