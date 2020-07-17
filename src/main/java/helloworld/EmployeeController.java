package helloworld;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class EmployeeController {
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDBMapper mapper = new DynamoDBMapper(client);

    //create
    public void create(Employee employee){
        mapper.save(employee);
    }

    //read
    public Employee get(String id){
        return mapper.load(Employee.class, id);
    }

    //update
    public Employee update(String id, Employee employee){
        Employee updateEmployee = this.get(id);
        updateEmployee.setUserName(employee.getUserName());
        updateEmployee.setFirstName(employee.getFirstName());
        updateEmployee.setLastName(employee.getLastName());
        updateEmployee.setUserName(employee.getUserName());
        updateEmployee.setEmail(employee.getEmail());
        mapper.save(updateEmployee);
        return updateEmployee;
    }

    //delete
    public Employee delete(String id){
        Employee deleteItem = this.get(id);
        mapper.delete(deleteItem);
        return deleteItem;
    }


}
