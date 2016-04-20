/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 * <p>
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.gateway.mediators.samplemediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.core.config.ParameterHolder;
import org.wso2.carbon.gateway.core.flow.AbstractMediator;
import org.wso2.carbon.gateway.core.flow.MediatorType;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.Constants;
import org.wso2.carbon.messaging.DefaultCarbonMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sample Custom Mediator
 */
public class SampleCustomMediator extends AbstractMediator {

    private static final Logger log = LoggerFactory.getLogger(SampleCustomMediator.class);
    private String logMessage = "Message received at Custom Sample Mediator";

    public SampleCustomMediator() {
    }

    public void setParameters(ParameterHolder parameterHolder) {
        logMessage = parameterHolder.getParameter("parameters").getValue();
    }

    @Override
    public MediatorType getMediatorType() {
        return MediatorType.IO_BOUND;
    }

    @Override
    public String getName() {
        return "SampleCustomMediator";
    }

    @Override
    public boolean receive(CarbonMessage carbonMessage, CarbonCallback carbonCallback) throws Exception {
          super.receive(carbonMessage, carbonCallback);
        List<ByteBuffer> bufferList = carbonMessage.getFullMessageBody();
        int length = 0;
        for (ByteBuffer byteBuffer : bufferList) {
            length = length + byteBuffer.capacity();
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        for (ByteBuffer byteBuffer1 : bufferList) {
            byteBuffer.put(byteBuffer1);
        }

        byte[] array = new byte[length];
        byteBuffer.flip();
        byteBuffer.get(array);

        String content = new String(array, "UTF-8");

        File file = new File("conf" + File.separator + "engine" + File.separator + "response.xml");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            boolean val = file.createNewFile();
            if (val) {
                log.info("file created");
            }
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        try {
            bw.write(content);

            bw.close();
        } catch (IOException e) {
            bw.close();
        }

        //   log.info(content);

        DefaultCarbonMessage response = new DefaultCarbonMessage();

        response.setStringMessageBody(content);
        byte[] errorMessageBytes = content.getBytes(Charset.defaultCharset());

        Map<String, String> transportHeaders = new HashMap<>();
        transportHeaders.put(Constants.HTTP_CONNECTION, Constants.KEEP_ALIVE);
        transportHeaders.put(Constants.HTTP_CONTENT_ENCODING, Constants.GZIP);
        transportHeaders.put(Constants.HTTP_CONTENT_TYPE, Constants.TEXT_XML);
        transportHeaders.put(Constants.HTTP_CONTENT_LENGTH, (String.valueOf(errorMessageBytes.length)));

        response.setHeaders(transportHeaders);

        response.setProperty(Constants.HTTP_STATUS_CODE, 200);
        response.setProperty(Constants.DIRECTION, Constants.DIRECTION_RESPONSE);
        response.setProperty(Constants.CALL_BACK, carbonCallback);
        carbonCallback.done(response);
        return false;
     //   return next(response, carbonCallback);

    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
