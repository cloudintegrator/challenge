import ballerina/http;
import ballerinax/rabbitmq;
import ballerina/log;
import ballerina/sql;
import ballerinax/mysql;
import ballerinax/mysql.driver as _;

public type Medicine record{|
    int id?;
    string email;
    string created;
    string medicine_name;
    int medicine_qty;
    string medicine_validity;
|};

public type Response record{
    int status;
    string message;
};

service / on new http:Listener(9090) {
    private final rabbitmq:ConnectionConfiguration connectionConfig;
    private final rabbitmq:Client mqClient;
    private final mysql:Client db;

    function init() returns error?{
        self.connectionConfig={
            username: "aannimrm",
            password: "B40g83-bYXRfan3MSsyi1DuQzvH_Nves",
            virtualHost: "aannimrm"
        };
        self.mqClient=check new ("fish-01.rmq.cloudamqp.com",5672,self.connectionConfig);
        self.db = check new ("mysql-3133c145-tesla.a.aivencloud.com", "avnadmin", "AVNS_8EXQIdtLOu0IeqIM8My", "defaultdb", 18801);
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

    resource function get medicines() returns Medicine[] | error{
        stream<Medicine, sql:Error?> medStream = self.db->query(`SELECT * FROM med_data`);
        return from Medicine med in medStream
            select med;
    }
}
