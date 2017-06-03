# ywiki

Pronounced "lambda-wiki", but spelled with a 'y' because it was easier that typing out "lambda", and *much* easier than figuring out the Unicode every time.

This is a simple Markdown wiki, in [Java](https://docs.oracle.com/javase/8/docs/api/), [Jersey](https://jersey.github.io/), [AWS Lambda](https://aws.amazon.com/lambda/), and [aws-serverless-java](https://github.com/awslabs/aws-serverless-java-container). It is designed to demonstrate fully automated build and deploy to AWS Lambda, as well as simple monitoring and metrics (devops).

Starting with https://github.com/awslabs/aws-serverless-java-container/tree/master/samples/jersey/pet-store as a seed project.

## Building & Running

Standard [Maven](https://maven.apache.org/) build:

    % mvn clean packge
    
To run an instance locally:

    % mvn exec:java
    
This will wrap the Jersey instance in a Grizzly server and run at:

    http://localhost:8080/static/index.html

## Next Steps

* [x] Add simple MD file, make sure it gets served.
* [x] Add simple SPA editor, host resources locally.
* [ ] Add Cognito integrated.
* [ ] Setup Swagger and API frontend
* [ ] Get POST/UPDATE working.
* [ ] Add cloud-based doc store.
* [ ] Clean up and release.

### Notes on sam.yaml

* Auto-generate: (fill std template)
  * Handler: from annotation on method
  * Runtime: from mvn
  * CodeUri: from mvn
  * API Value: from @Path
  
### Notes on Deploying

* Create a bucket:

    aws s3api create-bucket --bucket wiki-7vhvp4r9 --region us-west-2 --create-bucket-configuration LocationConstraint=us-west-2

* Update sam.yml to have correct version (CodeUri)
* Upload JAR

    aws cloudformation package --template-file sam.yaml --output-template-file target/output-sam.yaml --s3-bucket wiki-7vhvp4r9
    
* Deploy Lambda

    aws cloudformation deploy --template-file target/output-sam.yaml --stack-name wiki --capabilities CAPABILITY_IAM
    
* Confirm it's installed

    aws cloudformation describe-stacks --stack-name wiki

**TODO:** Add the above to mvn as special targets. CodeUri (in sam.yml) can be generated (resource copy) as well.
  
## Open Questions

* How will revision history be stored?
* How will AUTH be handled?
* How will authorization work? Ownership?
* What MD front-end? Pick editor with display only?

## Use Cases

### Display public page

### Display protected page

### Edit page

### Authenticate (Login)

### Search

## Technology Choices

### Persistance
* DynamoDB: http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-dynamodb.html
* S3: http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-s3.html

### Markdown Editing
* SimpleMDE: https://simplemde.com/
* StackEdit: https://github.com/benweet/stackedit
* Editor.md: https://pandao.github.io/editor.md/en.html  DIAGRAM, 2-pane
* Trumpbowyg: http://alex-d.github.io/Trumbowyg/
* MarkdownPlus: https://github.com/tylingsoft/markdown-plus
* Bootstrap Markdown: http://www.codingdrama.com/bootstrap-markdown/

https://github.com/bramp/js-sequence-diagrams

- Which supports interaction diagrams?

Others:
* https://github.com/substance/substance

Note: It would be great to use the same lib for both edit and display.

See also: http://www.developersfeed.com/awesome-javascript-wysiwyg-markdown-editors/

### Markdown Rendering


----

# Original Pet Store Docs

A basic pet store written with the [Jersey framework](https://jersey.java.net/). The `LambdaHandler` object is the main entry point for Lambda.

The application can be deployed in an AWS account using the [Serverless Application Model](https://github.com/awslabs/serverless-application-model). The `sam.yaml` file in the root folder contains the application definition

## Installation
To build and install the sample application you will need [Maven](https://maven.apache.org/) and the [AWS CLI](https://aws.amazon.com/cli/) installed on your computer.

In a shell, navigate to the sample's folder and use maven to build a deployable jar.
```
$ mvn package
```

This command should generate a `serverless-jersey-example-1.0-SNAPSHOT.jar` in the `target` folder. Now that we have generated the jar file, we can use the AWS CLI to package the template for deployment. 

You will need an S3 bucket to store the artifacts for deployment. Once you have created the S3 bucket, run the following command from the sample's folder:

```
$ aws cloudformation package --template-file sam.yaml --output-template-file output-sam.yaml --s3-bucket <YOUR S3 BUCKET NAME>
Uploading to xxxxxxxxxxxxxxxxxxxxxxxxxx  6464692 / 6464692.0  (100.00%)
Successfully packaged artifacts and wrote output template to file output-sam.yaml.
Execute the following command to deploy the packaged template
aws cloudformation deploy --template-file /your/path/output-sam.yaml --stack-name <YOUR STACK NAME>
```

As the command output suggests, you can now use the cli to deploy the application. Choose a stack name and run the `aws cloudformation deploy` command from the output of the package command.
 
```
$ aws cloudformation deploy --template-file output-sam.yaml --stack-name ServerlessJerseySample --capabilities CAPABILITY_IAM
```

Once the application is deployed, you can describe the stack to show the API endpoint that was created. The endpoint should be the `JerseyPetStoreApi` key of the `Outputs` property:

```
$ aws cloudformation describe-stacks --stack-name ServerlessJerseySample
{
    "Stacks": [
        {
            "StackId": "arn:aws:cloudformation:us-west-2:xxxxxxxx:stack/JerseySample/xxxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxx", 
            "Description": "Example Pet Store API written in jersey with the aws-serverless-java-container library", 
            "Tags": [], 
            "Outputs": [
                {
                    "Description": "URL for application", 
                    "OutputKey": "JerseyPetStoreApi", 
                    "OutputValue": "https://xxxxxxx.execute-api.us-west-2.amazonaws.com/Prod/pets"
                }
            ], 
            "CreationTime": "2016-12-13T22:59:31.552Z", 
            "Capabilities": [
                "CAPABILITY_IAM"
            ], 
            "StackName": "JerseySample", 
            "NotificationARNs": [], 
            "StackStatus": "UPDATE_COMPLETE"
        }
    ]
}

```

Copy the `OutputValue` into a browser to test a first request.
