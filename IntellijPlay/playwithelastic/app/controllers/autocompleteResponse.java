package controllers;


import java.util.List;

public class autocompleteResponse
{
    private List<String>autoCompleteResults;

    public List<String> getAutoCompleteResults() {
        return autoCompleteResults;
    }
    public void setAutoCompleteResults(List<String> autoCompleteResults) {
        this.autoCompleteResults = autoCompleteResults;
    }
}
