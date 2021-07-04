# GC日志分析总结

## 1.环境准备

### 1.1.压测工具

`wrk`

### 1.2.压测命令

`wrk -t8 -c40 -d60s http://localhost:8088/api/hello`

以上命令表示对本机8088端口运行的进程中的/api/hello接口进行压测，参数为8个进程，40个连接，执行时长60秒。

### 1.3.GC日志分析工具

`GCeasy`

地址：`gceasy.io`



## 2.各GC压测结果

| GC           | 堆内存配置 | YGC次数 | YGC平均STW | FGC/CMSGC/G1GC次数 | FGC/CMSGC/G1GC 平均STW |
| ------------ | ---------- | ------- | ---------- | ------------------ | ---------------------- |
| Serial       | 512MB      | 189     | 5.82ms     | 0                  | N/A                    |
| Parallel     | 512MB      | 167     | 1.92ms     | 0                  | N/A                    |
| ParNew + CMS | 512MB      | 200     | 5.00ms     | 0                  | N/A                    |
| G1           | 512MB      | 89      | 4.49ms     | 0                  | N/A                    |
| Serial       | 4g         | 25      | 22.8ms     | 0                  | N/A                    |
| Parallel     | 4g         | 22      | 8.64ms     | 0                  | N/A                    |
| ParNew + CMS | 4g         | 50      | 9.40ms     | 0                  | N/A                    |
| G1           | 4g         | 14      | 27.9ms     | 0                  | N/A                    |



## 3.分析总结

- Srial & Parallel，这两种GC的区别主要为在进行垃圾回收时使用单线程串行执行和使用多线程并行执行，所以在堆内存配置较小时两者差别不明显，当堆内存配置较大时，Parallel的STW时间明显小于Serial，特别是在对老年代进行回收时，差距会更明显，因为老年代的空间较大。
- Parallel & ParNew + CMS，这两者对于YGC都是并行处理的方式，所以相差不大，但是ParNew会略微慢于Parallel。在针对老年代的垃圾回收过程中，CMS会产生STW的阶段只有初始标记和重新标记，所以总的STW会低于Parallel。
- G1的垃圾回收过程类似于CMS，从压测结果中看G1的STW时间是Parallel和ParNew的3倍左右，但是G1垃圾回收器较为灵活，可以为其配置垃圾回收的时间，所以如果系统对于STW时间有要求的话可以考虑使用G1并且通过设置其`-XX:MaxGcPauseMillis`参数来限制垃圾回收消耗的时间。

