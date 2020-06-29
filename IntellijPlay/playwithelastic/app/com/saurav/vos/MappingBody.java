package com.saurav.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingBody
{
    @SerializedName("mappings")
    @Expose
    private IndexMapping mappings;

    public IndexMapping getMapping() {
        return mappings;
    }

    public void setMapping(IndexMapping mappings) {
        this.mappings = mappings;
    }
}
