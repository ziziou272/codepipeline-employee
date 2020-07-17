package helloworld;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeHandler  {

    private DynamoDBMapper initDynamoDbClient() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        return new DynamoDBMapper(client);
    }

    public APIGatewayProxyResponseEvent create(APIGatewayProxyRequestEvent request, Context context) {
        DynamoDBMapper mapper = this.initDynamoDbClient();

        String body = request.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Employee employee = objectMapper.readValue(body, Employee.class);
            mapper.save(employee);
            return new APIGatewayProxyResponseEvent().withStatusCode(201);
        } catch (IOException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }

    public APIGatewayProxyResponseEvent read(APIGatewayProxyRequestEvent request, Context context) {
        DynamoDBMapper mapper = this.initDynamoDbClient();

        String[] arr = request.getPath().split("/");
        String id = arr[2];
        Employee employee = mapper.load(Employee.class, id);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString = objectMapper.writeValueAsString(employee);

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(jsonInString);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }
}
