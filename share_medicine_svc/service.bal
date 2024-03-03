import ballerina/http;
import ballerinax/rabbitmq;
import ballerina/log;

public type Medicine record{
    string email;
    string created;
    string medicine_name;
    int medicine_qty;
    string medicine_validity;

};

public type Response record{
    int status;
    string message;
};

service / on new http:Listener(9090) {
    private final rabbitmq:ConnectionConfiguration connectionConfig;
    private final rabbitmq:Client mqClient;

    function init() returns error?{
        self.connectionConfig={
            username: "aannimrm",
            password: "B40g83-bYXRfan3MSsyi1DuQzvH_Nves",
            virtualHost: "aannimrm"
        };
        self.mqClient=check new ("fish-01.rmq.cloudamqp.com",5672,self.connectionConfig);
        log:printInfo("********** Service Initialized **********");
    }

    resource function get health() returns string{
        return "OK";
    }
    
    resource function post medicines(Medicine medicine) returns Response|error{
        check self.mqClient->publishMessage({
            content: medicine,
            routingKey: "MEDICINE.QUEUE"
        });

        log:printInfo("********** Medicine information posted successfully **********");
        Response r={
            status: 201,
            message: "Sent data to Rabbit MQ"
        };
        return r;
    }
}
