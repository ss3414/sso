# sso
单点登录/分布式部署<br>

************************************************************************************************************************

# 单点登录
①同构单点登录<br>
shiro_redis中的两组项目shiro_1/shiro_2和shiro_3/shiro_4的登录模块是统一的，即将Session/Token存储到同一个Redis中
②异构单点登录<br>
两个项目的登录机制不同，项目之间的单点登录实际上是跨项目访问时构造登录状态<br>
③引入第三方框架<br>
跨项目访问时构造登录状态，只不过引入第三方框架如CAS/OAuth<br>

# shiro_redis
①JDK8<br>
②分布式部署<br>
③shiro_1/shiro_2（Shiro+Cookie）<br>
将Session交由Redis管理（ShiroConfig+RedisShiroSessionDao）<br>
User类需要实现序列化<br>
shiro_1/shiro_2域名相同即可共享登录<br>
docker-compose多项目部署<br>
④shiro_3/shiro_4（Shiro+JWT）<br>
登录时储存Token，访问后台时校验Token<br>

# cookie_jwt
①SpringBoot（JDK8）+JPA+Shiro+Cookie/JWT<br>
②异构单点登录<br>
shiro_5（Shiro+Cookie）<br>
shiro_6（Shiro+JWT）<br>
③单点登录方式<br>
回调（项目1带着用户信息访问项目2，项目2调用项目1接口查询用户信息）<br>
构造（项目1带着加密用户信息访问项目2，项目2校验加密用户信息并构造项目2登录用户信息）<br>
④单机缓存<br>
shiro_5不启用Redis时Session放在本机内存中（由Shiro管理）<br>
shiro_6不启用Redis时Token放在本机Spring Cache中（@EnableCaching+spring.cache.type）<br>
⑤Nginx负载均衡<br>

******************************************************************************************

# CAS准备工作
①CAS服务端<br>
将修改过的CAS服务端cas.war（cas-server-4.0.0/不需要HTTPS）中的内容解压放到apache-tomcat-7.0.90(2)\cas\ROOT目录下<br>
配置Tomcat本地单端口多域名（apache-tomcat-7.0.90(2)\conf\server.xml）<br>
修改hosts映射域名（C:\Windows\System32\drivers\etc\hosts）<br>
CAS单点认证地址：http://cas.test.com/login（casuser+Mellon）<br>
②hosts映射域名<br>
CAS服务端：127.0.0.1 cas.test.com<br>
③开发环境<br>
开发环境下cas_ssm/cas_springboot分别使用127.0.0.1:8080/127.0.0.1:8000启动（需要修改对应CAS配置）<br>

# cas_ssm
①SSM（JDK8）+CAS<br>
②CAS客户端<br>
③web.xml配置<br>
允许login1.jsp访问单点地址（CAS Validation Filter）<br>
如果未登录访问login1.jsp会自动单点认证（CAS Authentication Filter）<br>
不允许login2访问单点地址，但允许从request中获取用户信息（CAS HttpServletRequest Wrapper Filter）<br>
允许从任意页面注销（CAS Single Sign Out Filter）<br>
④Java EE/SpringMVC<br>
JSP直接放到webapp目录下，跳转时以.jsp结尾为传统Java EE写法<br>

# cas_springboot
①SpringBoot（JDK8）+CAS<br>
②CAS客户端<br>
③CAS配置<br>
第三方Springboot CAS客户端（也可以将cas_ssm中的web.xml移植到cas_springboot中）<br>
application.properties（对应web.xml配置）<br>
Application @EnableCasClient（Springboot自动配置）<br>
④跨域单点登录<br>
CAS单点登录的核心是：当cas_ssm登录过后，浏览器会保存CAS服务器的Cookie CASTGC，<br>
当cas_springboot访问CAS服务器时会带上这个Cookie（这个Cookie cas_ssm/cas_springboot获取不到）<br>
（除非CAS服务器将Cookie CASTGC跨域写入，否则无法跨域单点登录）<br>