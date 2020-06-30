package controllers;

public class DeleteDocRequestBody
{
    private String elasticId;
    private String index;


    public String getElasticId() {
        return elasticId;
    }
    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
