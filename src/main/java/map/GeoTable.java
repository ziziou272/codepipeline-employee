package map;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "GeoTable")
public class GeoTable {
    private String id;
    private String propertyID;
    private String geoHashString;
    private Double latitude;
    private Double longitude;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @DynamoDBAttribute(attributeName = "propertyID")
    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    @DynamoDBAttribute(attributeName = "geoHashString")
    public String getGeoHashString() {
        return geoHashString;
    }

    public void setGeoHashString(String geoHashString) {
        this.geoHashString = geoHashString;
    }

    @DynamoDBAttribute(attributeName = "latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @DynamoDBAttribute(attributeName = "longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
