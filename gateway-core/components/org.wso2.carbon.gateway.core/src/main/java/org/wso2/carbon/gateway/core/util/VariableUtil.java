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
package org.wso2.carbon.gateway.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.core.Constants;
import org.wso2.carbon.messaging.CarbonMessage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

/**
 * Utility class that provides commons variable related functions like creating a new variable map and pushing
 * it onto CarbonMessage variables stack.
 */
public class VariableUtil {

    private static final Logger log = LoggerFactory.getLogger(VariableUtil.class);


    /**
     * Creates a new variable stack containing global variables and attaches this to CarbonMessage.
     * @param cMsg
     */
    public static void pushGlobalVariableStack(CarbonMessage cMsg, Map<String, Object> globalVariables) {
        // check if stack exists in cMsg, create empty otherwise
        Stack<Map<String, Object>> variableStack;
        if (cMsg.getProperty(Constants.VARIABLE_STACK) != null) {
            variableStack = (Stack<Map<String, Object>>) cMsg.getProperty(Constants.VARIABLE_STACK);
        } else {
            variableStack = new Stack<Map<String, Object>>();
            cMsg.setProperty(Constants.VARIABLE_STACK, variableStack);
        }

        if (variableStack.size() == 0) {
            variableStack.push(globalVariables);
        } else {
            Map<String, Object> gtScope = variableStack.peek();
            globalVariables.put(Constants.GW_GT_SCOPE, gtScope);
            variableStack.push(globalVariables);
        }
    }

    /**
     * Creates a new variable stack with an empty variable map if the stack size is zero or else it will create a new
     * map with a reference to the map on top of the stack and push this new map onto the stack.
     * @param cMsg
     */
    public static void pushNewVariableStack(CarbonMessage cMsg) {
        // check if stack exists in cMsg, create empty otherwise
        Stack<Map<String, Object>> variableStack;
        if (cMsg.getProperty(Constants.VARIABLE_STACK) != null) {
            variableStack = (Stack<Map<String, Object>>) cMsg.getProperty(Constants.VARIABLE_STACK);
        } else {
            variableStack = new Stack<Map<String, Object>>();
            cMsg.setProperty(Constants.VARIABLE_STACK, variableStack);
        }

        if (variableStack.size() == 0) {
            variableStack.push(new HashMap<String, Object>());
        } else {
            Map<String, Object> newMap = new HashMap<>();
            newMap.put(Constants.GW_GT_SCOPE, variableStack.peek());
            variableStack.push(newMap);
        }
    }

    /**
     * Pop top item off variable stack.
     * @param cMsg
     * @param stack
     */
    public static void popVariableStack(CarbonMessage cMsg, Stack<Map<String, Object>> stack) {
        if (cMsg.getProperty(Constants.VARIABLE_STACK) == null) {
            cMsg.setProperty(Constants.VARIABLE_STACK, stack);
        } else {
            Stack<Map<String, Object>> existingStack =
                    (Stack<Map<String, Object>>) cMsg.getProperty(Constants.VARIABLE_STACK);
            existingStack.pop();
        }
    }

    /**
     * Set variable stack to CarbonMessage if it does not already exist.
     * @param cMsg
     * @param stack
     */
    public static void setVariableStack(CarbonMessage cMsg, Stack<Map<String, Object>> stack) {
        if (cMsg.getProperty(Constants.VARIABLE_STACK) == null) {
            cMsg.setProperty(Constants.VARIABLE_STACK, stack);
        }
    }

    /**
     * Get variable stack from CarbonMessage. This method will create an empty variable stack if it does not exist.
     * @param cMsg
     * @return variable stack
     */
    public static Stack<Map<String, Object>> getVariableStack(CarbonMessage cMsg) {
        if (cMsg.getProperty(Constants.VARIABLE_STACK) != null) {
            return ((Stack<Map<String, Object>>) cMsg.getProperty(Constants.VARIABLE_STACK));
        } else {
            pushNewVariableStack(cMsg);
            return (Stack<Map<String, Object>>) cMsg.getProperty(Constants.VARIABLE_STACK);
        }
    }

    /**
     * Provides instantiated object of give variable type.
     * @param type
     * @param value
     * @return Object of variable type
     */
    public static Object getVariable(String type, String value) {
        type = type.toLowerCase(Locale.ROOT);
        if (type.equals("string")) {
            return String.valueOf(value);
        } else if (type.equals("integer")) {
            return Integer.valueOf(value);
        } else if (type.equals("boolean")) {
            return Boolean.valueOf(value);
        } else if (type.equals("double")) {
            return Double.valueOf(value);
        } else if (type.equals("float")) {
            return Float.valueOf(value);
        } else if (type.equals("long")) {
            return Long.valueOf(value);
        } else if (type.equals("short")) {
            return Short.valueOf(value);
        } else if (type.equals("xml")) {
            log.info("XML Variable type not yet implemented! Using string instead.");
            return String.valueOf(value);
        } else if (type.equals("json")) {
            log.info("JSON Variable type not yet implemented! Using string instead.");
            return String.valueOf(value);
        } else {
            log.error("Unrecognized variable type " + type);
            return null;
        }
    }

}
