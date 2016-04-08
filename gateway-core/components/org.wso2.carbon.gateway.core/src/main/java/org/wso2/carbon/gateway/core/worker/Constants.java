/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.gateway.core.worker;

/**
 * Common Constants used by gate way.
 */
public final class Constants {

    // Server state constants
    public static final String STATE_TRANSITION = "STATE_TRANSITION";

    public static final String STATE_STARTED = "STATE_STARTED";

    public static final String STATE_STOPPED = "STATE_STOPPED";

    // Disruptor related constants
    public static final String BUSY_SPIN = "BUSY_SPIN";

    public static final String BLOCKING_WAIT = "BLOCKING_WAIT";

    public static final String LITE_BLOCKING = "LITE_BLOCKING";

    public static final String PHASED_BACKOFF = "PHASED_BACKOFF";

    public static final String TIME_BLOCKING = "TIME_BLOCKING";

    public static final String SLEEP_WAITING = "SLEEP_WAITING";

    public static final String YIELD_WAITING = "YIELD_WAITING";

    public static final String WAIT_STRATEGY = "disruptor.wait.strategy";

    public static final String DISRUPTOR_BUFFER_SIZE = "disruptor.buffer.size";

    public static final String DISRUPTOR_COUNT = "disruptor.count";

    public static final String DISRUPTOR_EVENT_HANDLER_COUNT = "disruptor.eventhandler.count";

    public static final String SHARE_DISRUPTOR_WITH_OUTBOUND = "share.disruptor.with.outbound";

    // Client Bootstrap related
    public static final String CLINET_BOOTSTRAP_TCP_NO_DELY = "client.bootstrap.nodelay";

    public static final String CLINET_BOOTSTRAP_KEEPALIVE = "client.bootstrap.keepalive";

    public static final String CLINET_BOOTSTRAP_SEND_BUFFER_SIZE = "client.bootstrap.sendbuffersize";

    public static final String CLINET_BOOTSTRAP_RECEIVE_BUFFER_SIZE = "client.bootstrap.recievebuffersize";

    public static final String CLINET_BOOTSTRAP_CONNECT_TIME_OUT = "client.bootstrap.connect.timeout";

    public static final String CLINET_BOOTSTRAP_SO_REUSE = "client.bootstrap.socket.reuse";

    public static final String CLINET_BOOTSTRAP_SO_TIMEOUT = "client.bootstrap.socket.timeout";


    // Server Bootstrap related
    public static final String SERVER_BOOTSTRAP_TCP_NO_DELY = "server.bootstrap.nodelay";

    public static final String SERVER_BOOTSTRAP_KEEPALIVE = "server.bootstrap.keepalive";

    public static final String SERVER_BOOTSTRAP_SEND_BUFFER_SIZE = "server.bootstrap.sendbuffersize";

    public static final String SERVER_BOOTSTRAP_RECEIVE_BUFFER_SIZE = "server.bootstrap.recievebuffersize";

    public static final String SERVER_BOOTSTRAP_CONNECT_TIME_OUT = "server.bootstrap.connect.timeout";

    public static final String SERVER_BOOTSTRAP_SO_REUSE = "server.bootstrap.socket.reuse";

    public static final String SERVER_BOOTSTRAP_SO_BACKLOG = "server.bootstrap.socket.backlog";

    public static final String SERVER_BOOTSTRAP_SO_TIMEOUT = "server.bootstrap.socket.timeout";


    // Connection Pool parameters
    public static final String NUMBER_OF_POOLS = "connection.pool.count";

    public static final String MAX_ACTIVE_CONNECTIONS_PER_POOL = "max.active.connections.per.pool";

    public static final String MIN_IDLE_CONNECTIONS_PER_POOL = "min.idle.connections.per.pool";

    public static final String MAX_IDLE_CONNECTIONS_PER_POOL = "max.idle.connections.per.pool";

    public static final String MIN_EVICTION_IDLE_TIME = "min.eviction.idle.time";

    public static final String ENABLE_GLOBAL_CONNECTION_POOLING = "enable.global.client.connection.pooling";

    public static final String NO_THREADS_IN_EXECUTOR_SERVICE = "sender.thread.count";

    public static final String DISRUPTOR_CONSUMER_EXTERNAL_WORKER_POOL = "disruptor.consumer.worker.pool.size";


    public static final String IS_DISRUPTOR_ENABLE = "enable.disruptor";
    public static final String EXECUTOR_WORKER_POOL_SIZE = "executor.workerpool.size";

    public static final String EXECUTOR_WORKER_POOL = "executor.workerpool";

    public static final String PARENT_CALLBACK = "parent.callback";

    private Constants() {
    }

}


