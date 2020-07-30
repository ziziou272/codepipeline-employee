package map;


import ch.hsr.geohash.GeoHash;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class getProperty implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        double longitude = Double.parseDouble(request.getQueryStringParameters().get("longitude"));
        double latitude = Double.parseDouble(request.getQueryStringParameters().get("latitude"));

        String geoHashString = GeoHash. withCharacterPrecision(latitude, longitude, 5)
                .toBase32();

        Map<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withN(geoHashString));
        expressionAttributeValues.put(":val2", new AttributeValue().withN(geoHashString + "\uFFFD"));
        ScanRequest scanRequest = new ScanRequest()
                .withTableName("GeoTable")
                .withFilterExpression("geoHashString >= :val and geoHashString < :val2");

        ScanResult result = client.scan(scanRequest);

        List<String> propertyList = new ArrayList<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            propertyList.add(item.get("propertyId").toString());
        }

        try {
            String jsonInString = new ObjectMapper().writeValueAsString(propertyList);
            return new APIGatewayProxyResponseEvent().withBody(jsonInString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }
}
