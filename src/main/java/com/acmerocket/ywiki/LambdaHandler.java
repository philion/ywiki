/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.acmerocket.ywiki;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(LambdaHandler.class);

    private final ResourceConfig app = new ResourceConfig().register(WikiBinder.INSTANCE).packages("com.acmerocket.ywiki").register(JacksonFeature.class);
    private final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler = JerseyLambdaContainerHandler.getAwsProxyHandler(app);

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        LOG.info("request: {}, context: {}", awsProxyRequest, context);
        return handler.proxy(awsProxyRequest, context);
    }
}