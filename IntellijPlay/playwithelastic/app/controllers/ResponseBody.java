package controllers;

public class ResponseBody
{
    private String isSuccessful;
    private String elasticId;
    private String message;


    public String getIsSuccessful() {
        return isSuccessful;
    }
    public void setIsSuccessful(String isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
    public String getElasticId() {
        return elasticId;
    }
    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
