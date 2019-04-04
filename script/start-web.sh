#!/usr/bin/env bash

# java环境变量
export JAVA_HOME=/usr/java/jdk/jdk1.8.0_161 # jdk 根目录
export JRE_HOME=/usr/java/jdk/jdk1.8.0_161/jre # jre 目录
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$JAVA_HOME:$PATH

#noJavaHome=false
#if [ -z "$JAVA_HOME" ] ; then
#    noJavaHome=true
#fi
#
#if [ ! -e "$JAVA_HOME/bin/java" ] ; then
#    noJavaHome=true
#fi
#
#if $noJavaHome ; then
#    echo
#    echo "Error: JAVA_HOME environment variable is not set."
#    echo
#    exit 1
#fi

APP='/home/wwwroot/LDO-training-platform-gateway/apps/soul-bootstrap.jar'
ENV=$1
BASE_DIR=/home/wwwroot/LDO-training-platform-gateway
APP_DIR=$BASE_DIR/apps
LOG_DIR=$BASE_DIR/logs
PID_FILE=$APP_DIR/soul-bootstrap.pid
JAVA_DEBUG_OPTS=" -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5012 "

cd $APP_DIR
#设置java的CLASSPATH

#USER_LOG_DIR=$BASE_DIR/userLogs
#==============================================================================

#set JAVA_OPTS
JAVA_OPTS="-server -Xmx2048m  -Xms2048m -Xmn1024m  -Xss512k"
JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:ParallelGCThreads=20"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -XX:MaxGCPauseMillis=850"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetail"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
JAVA_OPTS="$JAVA_OPTS -Xloggc:$LOG_DIR/gc.log"

# 如果非debug 模式 则置空
if [ ! "$3" = "debug" ]; then
    JAVA_DEBUG_OPTS=""
fi

RETVAL=0
start()
{
    mkdir -p $LOG_DIR
    run_cmd="java $JAVA_OPTS $JAVA_DEBUG_OPTS -jar $APP --spring.profiles.active=$ENV"
    $run_cmd >> $LOG_DIR/soul-bootstrap.log 2>&1 &
    echo $! > "$PID_FILE"

    if [ $? -gt 0 ]; then
		echo "Starting $APP ERROR"
    fi
    echo "Starting $APP OK"

}

#
stop()
{
        PID=$(cat $PID_FILE 2>/dev/null)
        kill -KILL $PID 2>/dev/null
        echo "Stopping $APP OK"
}

restart()
{
        stop
        start
}

case "$2" in
  start)
        start ;;
  stop)
        stop ;;
  restart|force-reload|reload)
        restart ;;
  status)
        status -p $PID_FILE $APP;;
  *)
    echo $"Usage: $0 {start|stop|status|restart|reload|force-reload}"
    exit 1
esac

RETVAL=$?
exit $RETVAL
