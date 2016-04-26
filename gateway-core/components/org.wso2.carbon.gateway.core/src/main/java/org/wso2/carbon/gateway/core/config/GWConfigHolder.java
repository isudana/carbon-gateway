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

package org.wso2.carbon.gateway.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.core.flow.Group;
import org.wso2.carbon.gateway.core.flow.Pipeline;
import org.wso2.carbon.gateway.core.inbound.InboundEndpoint;
import org.wso2.carbon.gateway.core.outbound.OutboundEndpoint;
import org.wso2.carbon.gateway.core.util.VariableUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Object Model which holds configurations related to a one GW process
 */
public class GWConfigHolder {
    private static final Logger log = LoggerFactory.getLogger(GWConfigHolder.class);


    private String name;

    private InboundEndpoint inboundEndpoint;

    private Map<String, Pipeline> pipelines = new HashMap<>();

    private Map<String, Group> groups = new HashMap<>();

    private Map<String, OutboundEndpoint> outboundEndpoints = new HashMap<>();

    private Map<String, Object> globalVariables = new HashMap<>();

    public GWConfigHolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGlobalVariable(String type, String key, String value) {
        Object variable = VariableUtil.createVariable(type, value);
        globalVariables.put(key, variable);
    }

    public Object getGlobalVariable(String key) {
        return globalVariables.get(key);
    }

    public void updateGlobalVariable(String key, String value) {
        if (globalVariables.get(key) == null) {
            log.error("Variable " + key + " is not initialized.");
        } else {
            globalVariables.put(key, value);
        }
    }

    public void removeGlobalVariable(String key) {
        globalVariables.remove(key);
    }

    public InboundEndpoint getInboundEndpoint() {
        return inboundEndpoint;
    }

    public void setInboundEndpoint(InboundEndpoint inboundEndpoint) {
        inboundEndpoint.setGWConfigName(name);
        this.inboundEndpoint = inboundEndpoint;
    }

    public Pipeline getPipeline(String name) {
        return pipelines.get(name);
    }

    public void addPipeline(Pipeline pipeline) {
        pipelines.put(pipeline.getName(), pipeline);
    }

    public Map<String, Pipeline> getPipelines() {
        return pipelines;
    }

    public Map<String, OutboundEndpoint> getOutboundEndpoints() {
        return outboundEndpoints;
    }

    public OutboundEndpoint getOutboundEndpoint(String name) {
        return outboundEndpoints.get(name);
    }

    public Map<String, Object> getGlobalVariables() {
        return globalVariables;
    }

    public void addOutboundEndpoint(OutboundEndpoint outboundEndpoint) {
        outboundEndpoints.put(outboundEndpoint.getName(), outboundEndpoint);
    }

    public void addGroup(Group group) {
        groups.put(group.getPath(), group);
    }

    public Collection<Group> getGroups() {
        return groups.values();
    }

    public Group getGroup(String path) {
        return groups.get(path);
    }

    public boolean hasGroups() {
        return !groups.isEmpty();
    }

}
