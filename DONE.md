- 已解决问题
  1. QQ第二种emoi类型表情解析不出来，会报错。已解决此问题，过滤掉解析不出来的emoji表情。
  2. 采用jetty而不是netty作为http服务器，避免http服务接收到empty text导致大量异常的问题。
  3. 所有属性放到了属性文件中。
  4. 代码已上传到git维护，地址：https://github.com/oddrock/qqdataserv.git
  5. 数据库配置放到了属性文件中。