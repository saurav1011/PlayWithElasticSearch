package controllers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Image
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("product_id")
    @Expose
    private Long product_id;
    @SerializedName("position")
    @Expose
    private Long position;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("width")
    @Expose
    private Long width;
    @SerializedName("height")
    @Expose
    private Long height;

    @SerializedName("content_width")
    @Expose
    private Long contentWidth;
    @SerializedName("content_height")
    @Expose
    private Long contentHeight;

    @SerializedName("content_ratio")
    @Expose
    private Float contentRatio;

    @SerializedName("src")
    @Expose
    private String src;
//    @SerializedName("variant_ids")
//    @Expose
//    private List<Object> variantIds = null;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(Long contentWidth) {
        this.contentWidth = contentWidth;
    }

    public Long getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(Long contentHeight) {
        this.contentHeight = contentHeight;
    }

    public Float getContentRatio() {
        return contentRatio;
    }

    public void setContentRatio(Float contentRatio) {
        this.contentRatio = contentRatio;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
