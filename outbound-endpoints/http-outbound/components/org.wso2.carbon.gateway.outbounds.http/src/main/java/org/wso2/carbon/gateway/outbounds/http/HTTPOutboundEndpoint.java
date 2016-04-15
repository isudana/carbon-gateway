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

package org.wso2.carbon.gateway.outbounds.http;

import org.wso2.carbon.gateway.core.ServiceContextHolder;
import org.wso2.carbon.gateway.core.config.ParameterHolder;
import org.wso2.carbon.gateway.core.outbound.AbstractOutboundEndpoint;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.Constants;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP Outbound Endpoint.
 */
public class HTTPOutboundEndpoint extends AbstractOutboundEndpoint {

    private String uri;

    @Override
    public boolean receive(CarbonMessage carbonMessage, CarbonCallback carbonCallback)
            throws Exception {
        processRequest(carbonMessage);
        ServiceContextHolder.getInstance().getSender().send(carbonMessage, carbonCallback);
        return false;
    }

    private void processRequest(CarbonMessage cMsg) throws MalformedURLException {

        URL url = new URL(uri);
        String host = url.getHost();
        int port = (url.getPort() == -1) ? 80 : url.getPort();
        String urlPath = url.getPath();

        cMsg.setProperty(Constants.HOST, host);
        cMsg.setProperty(Constants.PORT, port);
        cMsg.setProperty(Constants.TO, urlPath);

        if (port != 80) {
            cMsg.getHeaders().put(Constants.HTTP_HOST, host + ":" + port);
        } else {
            cMsg.getHeaders().put(Constants.HTTP_HOST, host);
        }
    }

    public HTTPOutboundEndpoint(String name, String uri) {
        super(name);
        this.uri = uri;
    }

    public HTTPOutboundEndpoint() {

    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public void setParameters(ParameterHolder parameters) {
        uri = parameters.getParameter("host").getValue();
    }
}
