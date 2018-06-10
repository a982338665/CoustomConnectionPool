# CoustomConnectionPool
自定义连接池--

**mysql相关：**

    ·默认最大连接数:100,最大可达16384
    ·查看最大连接数:show variables like '%max_connections%';
    ·修改最大连接数:set GLOBAL max_connections = 200;

**连接池前提：**
    
    能标识：连接池中的某一个连接是否属于空闲状态

**连接池的核心作用：**

    复用连接对象
    
**连接池的参数定义：**
    
    ·初始化连接数：3   --最少要维持的连接数
    ·最大连接数： 20
    ·每次增加的连接：5
    ·超时时间：2000
    
**实现过程：**  

    ·封装Connection，标记空闲状态   --ConnectionPool
    ·定义线程池抽象类及实现类       --AbstractDataSourcePool/DataSourcePool
    ·数据库操作配置                 --DataSourceConfig
    ·数据库基础配置                 --pool.properties