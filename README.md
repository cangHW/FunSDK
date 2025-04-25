
## 配置初始化
| 名称                  | 作用                      | 优先级   |
|---------------------|-------------------------|-------|
| LogFileConfig       | 日志文件库——配置初始化            | -1000 |
| ApmConfig           | 异常监控库——配置初始化            | -999  |
| CoreFrameworkConfig | 架构基础库——配置初始化            | -990  |
| ThreadPoolConfig    | 线程库——配置初始化              | -900  |
| WebBridgeConfig     | Web容器Bridge库(DS)——配置初始化 | -550  |


## 启动初始化
| 名称                | 作用                            | 优先级 |
| ------------------- | ------------------------------- |----|
| LogFileApplication  | 日志文件库——初始化               | -500   |
| WebApplicationImpl  | Web容器库——初始化               | -100 |



# ApiHttp