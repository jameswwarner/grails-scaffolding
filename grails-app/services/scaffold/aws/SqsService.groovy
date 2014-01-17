// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.aws

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.amazonaws.services.sqs.model.SendMessageResult
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.ReceiveMessageResult
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.DeleteMessageRequest


class SqsService {
    static transactional = false

    def grailsApplication

    protected AmazonSQS sqsClient = null

    protected initSqs() {
        sqsClient = new AmazonSQSClient(new BasicAWSCredentials(
                grailsApplication.config.aws.accessKey,
                grailsApplication.config.aws.secretKey)
            )
    }

    public boolean sendMessage(String queueUrl, String body) {
        if (!sqsClient) {
            initSqs()
        }

        def message = new SendMessageRequest(queueUrl, body)
        SendMessageResult result = sqsClient.sendMessage(message)
        log.error "Queued message "+result.messageId
        return result.messageId ? true : false
    }

    public Message getMessage(String queueUrl) {
        if (!sqsClient) {
            initSqs()
        }

        ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl)
        request.setMaxNumberOfMessages(1)
        ReceiveMessageResult result = sqsClient.receiveMessage(request)
        List<Message> messages = result.getMessages()
        if (!messages || messages.size() == 0) {
            // no messages received
            return null
        }
        Message message = messages.get(0)
        return message
    }

    public deleteMessage(String queueUrl, Message message) {
        String messageRecieptHandle = message.getReceiptHandle()
        def request = new DeleteMessageRequest(queueUrl, messageRecieptHandle)
        sqsClient.deleteMessage(request)
    }
}
