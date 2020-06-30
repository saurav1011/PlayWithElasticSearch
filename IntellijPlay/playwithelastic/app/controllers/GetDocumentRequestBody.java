package controllers;

public class GetDocumentRequestBody {
    private String elasticId;
    private String index;

    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    public String getElasticId() {
        return elasticId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
