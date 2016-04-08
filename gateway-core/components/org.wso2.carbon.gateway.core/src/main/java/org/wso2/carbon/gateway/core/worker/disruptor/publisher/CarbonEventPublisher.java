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

package org.wso2.carbon.gateway.core.worker.disruptor.publisher;

import com.lmax.disruptor.EventTranslator;
import org.wso2.carbon.gateway.core.worker.WorkerProcessor;
import org.wso2.carbon.gateway.core.worker.disruptor.event.CarbonDisruptorEvent;

/**
 * Event Publisher for RingNBuffer of Disruptor.
 */
public class CarbonEventPublisher implements EventTranslator<CarbonDisruptorEvent> {

    private Object event;
    private WorkerProcessor workerProcessor;


    public CarbonEventPublisher(Object event , WorkerProcessor workerProcessor) {
        this.event = event;
        this.workerProcessor = workerProcessor;
    }

    public void translateTo(CarbonDisruptorEvent event, long sequence) {
        event.setEvent(this.event);
        event.setWorkerProcessor(workerProcessor);
    }
}
