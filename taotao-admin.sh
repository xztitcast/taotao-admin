#!/bin/bash
#####JAVA_OPTS参数详解#####
# -Xms:初始化堆内存大小
# -Xmx:最大堆内存大小
# -Xmn:年轻代大小
# -Xss:线程栈内存大小
# -XX:MetaspaceSize 元空间大小JDK1.8以后持久代改元空间内存大小不受JVM控制

#####JAVA_HOME参数详解#####
# JDK安装在那个目录就写那个目录,我的jdk在/root/java/目录下
# 也可以将jdk配置在全局/etc/profile下或者$home/.bash_profile下,相关命令如下:
# vi or vim /etc/profile / $home/.bash_profile
# exprot JAVA_HOME=/root/java/jdk-1.8.01
# exprot CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
# export PATH=$PATH:$JAVA_HOME/bin
# :wq保存之后source /etc/profile

JAVA_OPTS="-server -Xms2G -Xmx2G -Xmn512M -Xss1M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M"
JAVA_HOME="/roo/java/jdk-11.0.15.1"
JAVA_JAR="taotao-admin-0.0.1-SNAPSHOT.jar"

#提示: 由于没有指定JAR包的目录该脚本必须和JAR包在同一目录
#####################################################
#校验程序是否启动
psid=0

checkpid(){
  javaps=`$JAVA_HOME/bin/jps -l |grep $JAVA_JAR`

  if [ -n "$javaps" ];then
     psid=`echo $javaps |awk '{print $1}'`
  else
     psid=0
  fi
}

#####################################################
#启动程序

start(){
  checkpid
  
  if [ $psid -ne 0 ];then
     echo "================================"
     echo "warn: $JAVA_JAR already started! (pid=$psid)"
     echo "================================"
  else
     echo -n "Starting $JAVA_JAR"
     JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $JAVA_JAR --spring.config.activate.on-profile=prod >/dev/null 2>&1 &"
     su - $RUNNING_USER -c "$JAVA_CMD"
     checkpid
     if [ $psid -ne 0 ];then
        echo "(pid=$psid) [OK]"
     else
        echo "[Failed]"
     fi
  fi
}

#####################################################
#停止程序
num=0

stop(){
  checkpid
  num=`expr $num + 1`

  if [ $psid -ne 0 ];then
     if [ "$num" -le 3 ];then
        echo -n "attempt to kill... num:$num"
        kill $psid
        sleep 30s
     else
	echo "force kill ..."
        kill -9 $psid
     fi	
     if [ $? -eq 0 ];then
        echo "[shutdown $JAVA_JAR success]"
     else
        echo "[shutdown $JAVA_JAR failed]"
     fi
    
     checkpid
     if [ $psid -ne 0];then
        stop
     fi
  else
     echo "=================================="
     echo "warn: $JAVA_JAR is not running"
     echo "=================================="
  fi
}

#####################################################
#程序运行状态

status(){
  checkpid

  if [ $psid -ne 0 ];then
     echo "$JAVA_JAR is running! (pid=$psid)"
  else
     echo "$JAVA_JAR is not running"
  fi
}

#####################################################
#switch case 函数读取脚本参数
#读取脚本第一个参数($1)进行判断
#参数取值范围：{start|stop|restart|status|info}
#如参数不在指定范围之内，则打印帮助信息

#################提示################################
#如需将脚本变成通用则JAVA_JAR参数可以读取脚本($2)参数

case $1 in
   'start')
        start
        ;;
   'stop')
        stop
        ;;
   'restart')
        stop
        start
        ;;
   'status')
        status
        ;;
   *)
    echo "Usage: $0 {start|stop|restart|status|info}"
    exit 1



