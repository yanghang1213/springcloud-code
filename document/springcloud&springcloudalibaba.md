# Spring Cloud & Spring Cloud Alibaba

## 1.简介

> 分布式微服务架构的一站式解决方案,是多种微服务架构落地技术的集合体,俗称微服务全家桶

## 2.常用的组件

+ 1.服务的注册与发现:Eureka
+ 2.服务负载与调用:Ribbon,Feign
+ 3.服务熔断和降级:Hystrix
+ 4.服务网关Zuul
+ 服务统一配置:Spring Cloud Config

## 3.搭建微服务的父工程

## 4.Eureka

### 4.1.Eureka服务端搭建

+ pom.xml

  ```xml
  <dependency>
  	<groupId>org.springframework.cloud</groupId>
  	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
  </dependency>
  ```

+ 在启动类设定自己为eurekaserver

  ```java
  @EnableEurekaServer
  ```

+ application.yml配置

  ```yml
  server:
    port: 7001
  eureka:
    instance:
      hostname: localhost #eureka服务器的实例名称
    client:
      #false表示不向注册中心注册自己
      register-with-eureka: false
      #false表示自己就是注册中心
      fetch-registry: false
      service-url:
        #eureka交互地址,查询服务和注册服务都需要用到这个地址
        defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  
  ```

+ 启动项目访问localhost:7001会出现eureka管理页面

### 4.2.eureka客户端搭建

+ pom.xml

  ```xml
  <!--eureka-->
  <dependency>
  	<groupId>org.springframework.cloud</groupId>
  	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  </dependency>
  ```

+ application.yml

  ```yml
  eureka:
    client:
      #客户端提供者需要向eureka注册
      register-with-eureka: true
      #客户端向注册中心抓取已有的注册服务
      fetch-registry: true
      service-url:
        #eureka交互地址,查询服务和注册服务都需要用到这个地址
        defaultZone: http://localhost:7001/eureka/
  ```

+ 启动类添加客户端启动注解@EnableEurekaClient

### 4.3.搭建eureka集群服务器

+ 与单机版不同的就是application.yml文件的配置

  ```yml
  server:
    port: 7001
  eureka:
    instance:
      hostname: eureka7001.com #eureka服务器的主机名称
    client:
      #false表示不向注册中心注册自己
      register-with-eureka: false
      #false表示自己就是注册中心
      fetch-registry: false
      service-url:
        #eureka交互地址,查询服务和注册服务都需要用到这个地址，如有多个以逗号分隔
        defaultZone: http://eureka7002.com:7002/eureka/
  ```


### 4.4.搭建payment支付提供者集群

+ 主要是application.yml配置文件需要注册到eureka集群里

  ```yml
  defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
  ```

### 4.5.order订单服务修改

+ 1.请求路径修改

  > public static final String PAYMENT_URL = "http://SPRINGCLOUD-PAYMENT";

+ 2.RestTemplate修改

  ```java
  @Configuration
  public class RestTemplateConfig {
  
      @Bean
      @LoadBalanced
      public RestTemplate getRestTemplate(){
      return new RestTemplate();
  }
  }
  ```

  @LoadBalanced:表示负载均衡

### 4.6.更换eureka显示的主机名以及显示ip

```yml
instance:
  instance-id: payment8002  #eureka显示的名称
  prefer-ip-address: true   #是否显示IP地址
```

## 5.springcloud整合zookeeper替换eureka

### 5.1.引入zookeeper依赖

```yml
        <!--zookeeper-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
        </dependency>
```

### 5.2.controller

```java
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/zookeeper")
    public R payment() {
        return R.ok().put("zookeeper.port",serverPort;
    }
}
```

### 5.3.启动类添加注解

```
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
```

### 5.4.启动后报错

> at org.apache.zookeeper.KeeperException.create(KeeperException.java:103) ~[zookeeper-3.5.3-beta.jar:3.5.3-beta-8ce24f9e675cbefffb8f21a47e06b42864475a60]

### 5.5.zookeeper3.5.3与自己服务器部署的版本不一致,改变jar包版本

```xml
        <!--zookeeper-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!--去除springcloud自带的zookeeper3.5.3版本换成3.4.9-->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.9</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

### 5.6.在zookeeper服务器可以查看到以下信息

```sh
[zk: localhost:2181(CONNECTED) 1] ls /services
[springcloud-payment]
```

### 5.7.consumer消费端

+ application.yml

  ```yml
  server:
    port: 80
  
  spring:
    application:
      name: springclou-order
    cloud:
      zookeeper:
        connect-string: 192.168.1.8:2181
  ```

+ 编写controller进行测试(此处省略,与eureka差不多)

## 6.springcloud整合consul

### 6.1.pom.xml引入

```xml
        <!--consul-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>
```

### 6.2.application.yml

```yml
server:
  port: 8006

spring:
  application:
    name: springcloud-payment-consul
  cloud:
    consul:
      host: 192.168.1.8  #用linux的ip地址（consul在本机就填localhost）
      port: 8500
      discovery:
        service-name: ${spring.application.name}
        prefer-ip-address: true
        heartbeat:
          enabled: true
        health-check-path: health
```

### 6.3.安装consul

+ 官网下载linux版本的consul

  > https://www.consul.io/downloads.html

+ 解压后将文件复制一份到/usr/local/bin/

  > cp consul /usr/local/bin/

+ 启动consul

  > consul agent -data-dir /tmp/node0 -node=node0 -bind=[192.168.1.8](http://192.168.1.8:8500/ui) -datacenter=dc1 -ui -client=[192.168.1.8](http://192.168.1.8:8500/ui) -server -bootstrap-expect 1

## 7.Ribbon

### 7.1.手写ribbon负载均衡算法

![image-20201221222802240](C:\Users\Administrator\Desktop\image-20201221222802240.png)

**从eureka看到有两个serviceInstance**

+ SPRINGCLOUD-ORDER (服务消费者)
+ SPRINGCLOUD-PAYMENT(服务提供者---集群,目前是8001和8002两个)

**自定义接口类MyLoadBalance返回URI**

```java
public interface MyLoadBalance {

    public URI getServiceInstance();
}
```

**实现类MyLoadBalanceImpl(主要实现)**

*分析负载均衡算法*

> 例如有三个集群实例 8001,8002,8003
>
> 以后请求依次是8001,8002,8003,8001,8002,8003......
>
> 相当于一个list有三个实例对象
>
> 每次请求的角标为: 下一次请求实例角标 = (当前角标数 + 1) % 实例总数量(3)

```java
@Component
public class MyLoadBalanceImpl implements MyLoadBalance{

    //定义一个原子操作类
    private AtomicInteger atomicInteger;
    public static final String SERVICE_ID = "SPRINGCLOUD-PAYMENT";
    @Autowired
    private DiscoveryClient discoveryClient;

    //初始化赋值
    public MyLoadBalanceImpl (){
        this.atomicInteger = new AtomicInteger(0);
    }


    @Override
    public URI getServiceInstance() {
        //1.获取所有的eureka存活的实例
        List<ServiceInstance> instances = discoveryClient.getInstances(SERVICE_ID);
        //2.获取到每一次请求的角标
        int instancesCount = instances.size();
        int nextValue = getAndSetNextValue(instancesCount);
        //3.获取当前角标的实例对象
        ServiceInstance serviceInstance = instances.get(nextValue);

        return serviceInstance.getUri();
    }

    //此方法是获取到下一个index,通过原子操作类+1 % 存活实例总数量(集群总数量) = 当前请求角标
    private int getAndSetNextValue(int instancesCount) {
        int current = this.atomicInteger.get();
        int nextValue;
        do{
            nextValue = (current + 1) % instancesCount;
        }while (!this.atomicInteger.compareAndSet(current,nextValue));
            return nextValue;
    }
}
```

**消费者OrderController**

```java
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    public static final String INFO_URL = "/payment/info/";
    
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MyLoadBalance myLoadBalance;
    
    @GetMapping("/my/loadbalance/{id}")
    public R loadbalance(@PathVariable Long id){
        URI uri = myLoadBalance.getServiceInstance();

        return restTemplate.getForObject(uri + INFO_URL + id,R.class);
    }

}
```

**postman测试**

![image-20201221223947176](C:\Users\Administrator\Desktop\image-20201221223947176.png)

![image-20201221224011522](C:\Users\Administrator\Desktop\image-20201221224011522.png)

*发现确实是依次调用8001和8002两个提供者实例*

## 8.OpenFeign(服务调用)

### 8.1.pom.xml

```xml
 <!--openfeign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```

### 8.2.application.yml

```yml
server:
  port: 80

spring:
  application:
    name: springcloud-order-feign

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka

ribbon:
  ReadTimeout: 5000
  ConnectionTimeout: 5000

#配置日志打印信息
logging:
  level:
    #设定监控哪个service
    com.hangyang.service.PaymentFeignService: debug

```

### 8.3.PaymentFeignService

```java
@Component
@FeignClient(value = "SPRINGCLOUD-PAYMENT")
public interface PaymentFeignService {

    @GetMapping("/payment/info/{id}")
    public R info(@PathVariable("id") Long id);
}
```

### 8.4.OrderController

```java
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @RequestMapping("/feign/payment/info/{id}")
    public R info(@PathVariable Long id){
        R r = paymentFeignService.info(id);

        return r;
    }
}
```

### 8.5.启动类

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
public class SpringcloudOrderFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudOrderFeignApplication.class, args);
    }

}
```

## 9.Hystrix(服务熔断与降级)

### 9.1.服务提供者springcloud-payment-hystrix-8005（服务降级,一般写在服务消费者一端）

+ pom.xml

```xml
        <!--hystrinx-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```

+ application.yml

```yml
server:
  port: 8005

spring:
  application:
    name: springcloud-payment-hystrix

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```

+ PaymentServiceImpl

```java
@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${server.port}")
    private String serverPort;

    @Override
    public R infoOk(Long id) {
        return R.ok().put("线程池",Thread.currentThread().getName()).put("ServerPort",serverPort);
    }

    @Override
    @HystrixCommand(fallbackMethod = "infoErrorHandler",commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")})
//    execution.isolation.thread.timeoutInMilliseconds设置方法执行超时时间，让超时时间小于线程时间测试服务降级方法会不会执行
    public R infoError(Long id) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int a = 10 / 0;
        return R.ok().put("线程池",Thread.currentThread().getName()).put("ServerPort",serverPort);
    }

    public R infoErrorHandler(Long id){
        return R.error().put("info","服务繁忙，请稍后重试");
    }
}
```

+ 启动类

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableHystrix
```

+ 这样需要每一个service方法对应一个降级方法处理,可以设置统一的全局降级处理

```
@Service
@DefaultProperties(defaultFallback = "paymentDefaultFallback")
public class PaymentServiceImpl implements PaymentService {

    @Value("${server.port}")
    private String serverPort;

    @Override
    public R infoOk(Long id) {
        return R.ok().put("线程池",Thread.currentThread().getName()).put("ServerPort",serverPort);
    }

    @Override
    @HystrixCommand
    public R infoError(Long id) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.ok().put("线程池",Thread.currentThread().getName()).put("ServerPort",serverPort);
    }

    public R paymentDefaultFallback(){
        return R.error().put("info","global全局异常处理");
    }
}
```

### 9.2.服务消费者springcloud-order-hystrix（服务降级,代码和服务提供者差不多）

### 9.3.服务提供者springcloud-payment-hystrix-8005（服务熔断,一般写在服务消费者一端）

**服务熔断:** 在固定时间内（Hystrix默认是10秒），接口调用出错比率达到一个阈值（Hystrix默认为50%），会进入熔断开                  启状态。

​            进入熔断状态后， 后续对该服务接口的调用不再经过网络，直接**执行本地的fallback方法**

```java
    @HystrixCommand(
            fallbackMethod = "idFallBack",
            commandProperties = {
            //是否开启服务熔断机制
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            //用来设置在滚动时间窗中,断路器熔断的最小请求数,至少有10个请求,熔断器才进行错误率的计算(默认值20)
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            //用来设置当断路器打开之后的休眠时间窗
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            //属性用来设置断路器打开的错误百分比条件
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value  = "50")})
    public R info(Long id) {
        if(id < 0) {
            throw new RuntimeException("id 不能为负数");
        }
        return R.ok().put("id",id);
    }

    public R idFallBack(Long id){
        return R.error().put("id",id);
    }
```

## 10.新一代网关Getway

