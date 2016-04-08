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

package org.wso2.carbon.gateway.core.worker.disruptor.handler;

import org.wso2.carbon.gateway.core.worker.WorkerProcessor;
import org.wso2.carbon.gateway.core.worker.disruptor.event.CarbonDisruptorEvent;
import org.wso2.carbon.messaging.CarbonMessage;

import java.util.concurrent.locks.Lock;

/**
 * Event Consumer of the Disruptor.
 */
public class CarbonDisruptorEventHandler extends DisruptorEventHandler {

    public CarbonDisruptorEventHandler() {
    }

    @Override
    public void onEvent(CarbonDisruptorEvent carbonDisruptorEvent, long l, boolean b) throws Exception {
        CarbonMessage carbonMessage = (CarbonMessage) carbonDisruptorEvent.getEvent();
        WorkerProcessor workerProcessor = carbonDisruptorEvent.getWorkerProcessor();
        Lock lock = carbonMessage.getLock();
        if (lock.tryLock()) {
            workerProcessor.process(carbonMessage);
        }
    }

}

