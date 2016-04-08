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

package org.wso2.carbon.gateway.core.worker.config;

import org.wso2.carbon.gateway.core.worker.Constants;
import org.wso2.carbon.gateway.core.worker.disruptor.config.DisruptorConfig;
import org.wso2.carbon.gateway.core.worker.disruptor.config.DisruptorManager;
import org.wso2.carbon.gateway.core.worker.threadpool.ThreadPoolFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * JAXB representation of the Engine Thread Model.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "engine")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThreadModelConfiguration {


    public static ThreadModelConfiguration getDefault() {
        ThreadModelConfiguration defaultConfig = new ThreadModelConfiguration();
        DisruptorConfiguration disruptorConfiguration = DisruptorConfiguration.getDefault();
        HashSet<DisruptorConfiguration> disruptorConfigurations = new HashSet<>();
        disruptorConfigurations.add(disruptorConfiguration);
        defaultConfig.setDisruptorConfigurations(disruptorConfigurations);
        ThreadPoolConfiguration threadPoolConfiguration = ThreadPoolConfiguration.getDefault();
        defaultConfig.setThreadPoolConfiguration(threadPoolConfiguration);
        return defaultConfig;
    }

    @XmlElementWrapper(name = "disruptorConfigurations")
    @XmlElement(name = "disruptorConfiguration")
    private Set<DisruptorConfiguration> disruptorConfigurations;


    @XmlElement(name = "threadPoolConfiguration")
    private ThreadPoolConfiguration threadPoolConfiguration;


    public Set<DisruptorConfiguration> getDisruptorConfigurations() {
        return disruptorConfigurations;
    }

    public void setDisruptorConfigurations(Set<DisruptorConfiguration> disruptorConfigurations) {
        this.disruptorConfigurations = disruptorConfigurations;
    }

    public ThreadPoolConfiguration getThreadPoolConfiguration() {
        return threadPoolConfiguration;
    }

    public void setThreadPoolConfiguration(ThreadPoolConfiguration threadPoolConfiguration) {
        this.threadPoolConfiguration = threadPoolConfiguration;
    }

    public void configure() {
        for (DisruptorConfiguration disruptorConfiguration : disruptorConfigurations) {
            String id = disruptorConfiguration.getId();
            if (id.equals(Constants.CPU_BOUND)) {
                DisruptorConfig disruptorConfig = new DisruptorConfig();
                List<Parameter> parameterList = disruptorConfiguration.getParameters();
                for (Parameter parameter : parameterList) {
                    if (parameter.getName().equals(Constants.DISRUPTOR_BUFFER_SIZE)) {
                        disruptorConfig.setBufferSize(Integer.parseInt(parameter.getValue()));
                    } else if (parameter.getName().equals(Constants.DISRUPTOR_COUNT)) {
                        disruptorConfig.setNoDisruptors(Integer.parseInt(parameter.getValue()));
                    } else if (parameter.getName().equals(Constants.DISRUPTOR_EVENT_HANDLER_COUNT)) {
                        disruptorConfig.setNoOfEventHandlersPerDisruptor(Integer.parseInt(parameter.getValue()));
                    } else if (parameter.getName().equals(Constants.WAIT_STRATEGY)) {
                        disruptorConfig.setDisruptorWaitStrategy(parameter.getValue());
                    }
                    DisruptorManager.createDisruptors(DisruptorManager.DisruptorType.CPU_INBOUND, disruptorConfig);
                }
            } else if (id.equals(Constants.IO_BOUND)) {
                DisruptorConfig disruptorConfig = new DisruptorConfig();
                List<Parameter> parameterList = disruptorConfiguration.getParameters();
                for (Parameter parameter : parameterList) {
                    if (parameter.getName().equals(Constants.DISRUPTOR_BUFFER_SIZE)) {
                        disruptorConfig.setBufferSize(Integer.parseInt(parameter.getValue()));
                    } else if (parameter.getName().equals(Constants.DISRUPTOR_COUNT)) {
                        disruptorConfig.setNoDisruptors(Integer.parseInt(parameter.getValue()));
                    } else if (parameter.getName().equals(Constants.DISRUPTOR_EVENT_HANDLER_COUNT)) {
                        disruptorConfig.setNoOfEventHandlersPerDisruptor(Integer.parseInt(parameter.getValue()));
                    } else if (parameter.getName().equals(Constants.WAIT_STRATEGY)) {
                        disruptorConfig.setDisruptorWaitStrategy(parameter.getValue());
                    }
                    DisruptorManager.createDisruptors(DisruptorManager.DisruptorType.IO_INBOUND, disruptorConfig);
                }
            }


        }
        ThreadPoolFactory.getInstance().createThreadPool(threadPoolConfiguration.getNoOfThreads());
    }
}
