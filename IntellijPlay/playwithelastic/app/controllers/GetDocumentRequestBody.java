package controllers;

public class GetDocumentRequestBody {
    private String elasticId;

    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    public String getElasticId() {
        return elasticId;
    }
}
