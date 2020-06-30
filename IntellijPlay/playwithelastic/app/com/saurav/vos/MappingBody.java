package com.saurav.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingBody
{
    @SerializedName("index")
    @Expose
    private String index;


    @SerializedName("mappings")
    @Expose
    private IndexMapping mappings;


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public IndexMapping getMappings() {
        return mappings;
    }

    public void setMappings(IndexMapping mappings) {
        this.mappings = mappings;
    }

}
