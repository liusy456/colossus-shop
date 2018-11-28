## colossus-shop

基于spring-cloud的分布式商城项目，现在只实现了框架的搭建，具体的
业务逻辑还未开始，本人上班又比较忙，希望大家可以一起来共同完成这个项目，也是学习微服务的
好机会。。。
谢谢大家

### 涉及技术
* Spring Cloud
* Spring Boot Admin
* Spring Boot
* RabbitMQ
* Swagger2
* Druid
* MyBatis
* MySQL
* Redis
* ElasticSearch
* Docker
* FastDFS
* Freemarker
* Beetl
* ...

### 端口规划
| Service服务名称|端口|
|:-:|:-:|
| Admin-Service      	|8050 |
| SSO-Service        	|8051 |
| Cart-Service       	|8052 |
| Item-Service       	|8053 |
| Notify-Service     	|8054 |
| Order-Service      	|8055 |
| Portal-Service     	|8056 |
| Redis-Service      	|8057 |
| Search-Service     	|8058 |
| Pay-Service     		|8059 |

|Cloud服务名称|端口|
|:-:|:-:|
| Eureka      	 	|8501 |
| Config     		|8502 |
| Admin	     	 	|8503 |
| Zipkin     	 	|8504 |
| Zuul     	 	 	|8505 |




