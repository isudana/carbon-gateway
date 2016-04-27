/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.gateway.core.inbound;

import org.wso2.carbon.gateway.core.config.ConfigRegistry;
import org.wso2.carbon.gateway.core.config.GWConfigHolder;
import org.wso2.carbon.gateway.core.config.ParameterHolder;
import org.wso2.carbon.gateway.core.flow.Group;
import org.wso2.carbon.gateway.core.util.VariableUtil;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;

import java.util.Collection;

/**
 * Base for InboundEndpoints. All Inbound Endpoint types must extend this.
 */
public abstract class InboundEndpoint {

    private String name;

    private String pipeline;

    private String gwConfigName;

    public String getName() {
        return name;
    }

    public String getGWConfigName() {
        return gwConfigName;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGWConfigName(String gwConfigName) {
        this.gwConfigName = gwConfigName;
    }

    /**
     * Check whether Carbon message can be processed with this endpoint
     *
     * @param cMsg Carbon Message
     * @return Check whether Carbon message can be processed with this endpoint
     */
    public abstract boolean canReceive(CarbonMessage cMsg);

    /**
     * Process the message
     *
     * @param cMsg     Carbon Message
     * @param callback Callback to execute response flow
     * @return whether forward processing is successful
     */
    public boolean receive(CarbonMessage cMsg, CarbonCallback callback) {

        GWConfigHolder configHolder = ConfigRegistry.getInstance().getGWConfig(getGWConfigName());
        VariableUtil.pushGlobalVariableStack(cMsg, configHolder.getGlobalConstants());

        String pipelineName = pipeline;

        // For service groups, if any
        if (configHolder.hasGroups()) {
            Collection<Group> groups = configHolder.getGroups();

            for (Group group : groups) {
                if (group.canProcess(cMsg)) {
                    pipelineName = group.getPipeline();
                    break;
                }
            }
        }

        return ConfigRegistry.getInstance().getPipeline(pipelineName).receive(cMsg, callback);
    }

    public abstract String getProtocol();

    public abstract void setParameters(ParameterHolder parameters);

}
