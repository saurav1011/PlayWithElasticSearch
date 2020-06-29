
package com.saurav.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndexMapping {

    @SerializedName("properties")
    @Expose
    private IndexProperties properties;

    public IndexProperties getProperties() {
        return properties;
    }

    public void setProperties(IndexProperties properties) {
        this.properties = properties;
    }

}
