#!/bin/bash

# Name of the bucket to use to deploy
deployBucket=com.acmerocket.util.DEPLOY

# Name of the stack
stackName=wiki

# force build
echo ...building
mvn clean package
if [ $? != 0 ]
then
    echo build failed.
    exit 1;
fi

# Make sure deploy bucket exists
aws s3 ls $deployBucket &> /dev/null
if [ $? != 0 ]
then 
    echo ...making deploy bucket: $deployBucket
    aws s3 mb s3://$deployBucket
fi

# Package the cloudformation, using the provided SAM
echo ...packaging cloudformation
aws cloudformation package --template-file sam.yaml --output-template-file target/output-sam.yaml --s3-bucket $deployBucket

# Deploy
echo ...deploying cloud formation
aws cloudformation deploy --template-file target/output-sam.yaml --stack-name $stackName --capabilities CAPABILITY_IAM
    
# Use a query to get the URL of the deployed app
testUrl=`aws cloudformation describe-stacks --stack-name $stackName --query Stacks[0].Outputs[0].OutputValue --output text`

# GET the URL, to confirm the deployment
echo ...checking $testUrl/version
version=`curl -s $testUrl/version`
echo ...deployed version $version

if [ $version != "0.8-SNAPSHOT" ]
then
    echo !!! unexpected version: $version
    exit 1;
fi

echo done.
exit 0
