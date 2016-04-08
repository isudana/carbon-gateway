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

package org.wso2.carbon.gateway.core.flow;

import org.wso2.carbon.gateway.core.worker.Constants;
import org.wso2.carbon.gateway.core.worker.WorkerModelDispatcher;
import org.wso2.carbon.gateway.core.worker.WorkerProcessor;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;

/**
 * An abstract mediator for IO bound mediators.All the IO bound mediators must
 * extend this Mediator.
 */
public abstract class AbstractIOBoundMediator extends AbstractMediator implements WorkerProcessor {


    @Override
    public boolean receive(CarbonMessage carbonMessage, CarbonCallback carbonCallback) throws Exception {
        carbonMessage.setProperty(Constants.PARENT_CALLBACK, carbonCallback);
        WorkerModelDispatcher.getInstance().dispatch(carbonMessage, this, WorkerModelDispatcher.WorkerMode.IO);
        return false;
    }

}
