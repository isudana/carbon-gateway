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

import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.core.worker.disruptor.config.DisruptorManager;
import org.wso2.carbon.gateway.core.worker.disruptor.publisher.CarbonEventPublisher;
import org.wso2.carbon.gateway.core.worker.threadpool.ThreadPoolFactory;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.Constants;

import java.util.concurrent.ExecutorService;

/**
 * A class which can be used to get the underlying thread model.It can
 * be Disruptor model or Executor Service.
 */
public class WorkerModelDispatcher {


    private static final Logger logger = LoggerFactory.getLogger(WorkerModelDispatcher.class);

    private static final WorkerModelDispatcher WORKER_MODEL_DISPATCHER = new WorkerModelDispatcher();

    private boolean customMediatorDeployed;

    private WorkerModelDispatcher() {

    }


    public static WorkerModelDispatcher getInstance() {
        return WORKER_MODEL_DISPATCHER;
    }


    public boolean dispatch(CarbonMessage carbonMessage, WorkerProcessor workerProcessor, WorkerMode workerMode) {

        if (!customMediatorDeployed) {
            if (workerMode == WorkerMode.CPU) {
                RingBuffer ringBuffer = DisruptorManager.getDisruptorConfig
                           (DisruptorManager.DisruptorType.CPU_INBOUND).getDisruptor();
                ringBuffer.publishEvent(new CarbonEventPublisher(carbonMessage, workerProcessor));
            } else if (workerMode == WorkerMode.IO) {
                RingBuffer ringBuffer = DisruptorManager.getDisruptorConfig
                           (DisruptorManager.DisruptorType.IO_INBOUND).getDisruptor();
                ringBuffer.publishEvent(new CarbonEventPublisher(carbonMessage, workerProcessor));
            }
        } else {
            if (carbonMessage.getProperty(Constants.DIRECTION) == null) {
                ExecutorService executorService = ThreadPoolFactory.getInstance().getInbound();
                executorService.execute(new PoolWorker(workerProcessor, carbonMessage));
            } else if (carbonMessage.getProperty(Constants.DIRECTION).
                       equals(Constants.DIRECTION_RESPONSE)) {
                ExecutorService executorService = ThreadPoolFactory.getInstance().getOutbound();
                executorService.execute(new PoolWorker(workerProcessor, carbonMessage));
            }
        }
        return false;
    }

    /**
     * This represents the mode of worker modes
     */
    public enum WorkerMode {
        CPU, IO
    }

    public void setCustomMediatorDeployed(boolean customMediatorDeployed) {
        this.customMediatorDeployed = customMediatorDeployed;
        DisruptorManager.setDoNotStart(true);
        DisruptorManager.shutdownAllDisruptors();
    }

    /**
     * A worker class for ThreadPool
     */
    private static class PoolWorker implements Runnable {

        private WorkerProcessor workerProcessor;
        private CarbonMessage carbonMessage;

        public PoolWorker(WorkerProcessor workerProcessor, CarbonMessage carbonMessage) {
            this.workerProcessor = workerProcessor;
            this.carbonMessage = carbonMessage;
        }

        @Override
        public void run() {
            workerProcessor.process(carbonMessage);
        }
    }

}
