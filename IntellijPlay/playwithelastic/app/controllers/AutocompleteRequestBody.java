package controllers;

public class AutocompleteRequestBody
{
    private String autoCompleteQuery;
    private  String index;


    public String getAutoCompleteQuery() {
        return autoCompleteQuery;
    }
    public void setAutoCompleteQuery(String autoCompleteQuery) {
        this.autoCompleteQuery = autoCompleteQuery;
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}


