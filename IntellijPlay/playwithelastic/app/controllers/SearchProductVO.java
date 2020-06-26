package controllers;

import java.util.List;

public class SearchProductVO// this is input body
{

    private String id;
    private String title;
    private String searchTerms;
    private String bodyHtml;
    private String productType;// changed return type from ProductType to String.... same with image,sex,realProductType
    private String handle;
    private String published_scope;
    private String tags;
    private String images;
    private String image;
    private String realProductType;
    private String fabric;
    private String collar;
    private String fit;
    private String color;
    private String bgColor;
    private String performance;
    private String sleeveLength;
    private String pattern;
    private String gender;
    private String pngHeroImage;
    private List<Image> pngImages;
    private List<SearchedVariantsVO> variantsVOs;
    private float matchPercentage;
    private long updatedTimeInMillis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPublished_scope() {
        return published_scope;
    }

    public void setPublished_scope(String published_scope) {
        this.published_scope = published_scope;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRealProductType() {
        return realProductType;
    }

    public void setRealProductType(String realProductType) {
        this.realProductType = realProductType;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public String getCollar() {
        return collar;
    }

    public void setCollar(String collar) {
        this.collar = collar;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(String sleeveLength) {
        this.sleeveLength = sleeveLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPngHeroImage() {
        return pngHeroImage;
    }

    public void setPngHeroImage(String pngHeroImage) {
        this.pngHeroImage = pngHeroImage;
    }

    public List<Image> getPngImages() {
        return pngImages;
    }

    public void setPngImages(List<Image> pngImages) {
        this.pngImages = pngImages;
    }

    public List<SearchedVariantsVO> getVariantsVOs() {
        return variantsVOs;
    }

    public void setVariantsVOs(List<SearchedVariantsVO> variantsVOs) {
        this.variantsVOs = variantsVOs;
    }

    public float getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(float matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public long getUpdatedTimeInMillis() {
        return updatedTimeInMillis;
    }

    public void setUpdatedTimeInMillis(long updatedTimeInMillis) {
        this.updatedTimeInMillis = updatedTimeInMillis;
    }

    @Override
    public String toString() {
        return "SearchProductVO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", searchTerms='" + searchTerms + '\'' +
                ", bodyHtml='" + bodyHtml + '\'' +
                ", productType='" + productType + '\'' +
                ", handle='" + handle + '\'' +
                ", published_scope='" + published_scope + '\'' +
                ", tags='" + tags + '\'' +
                ", images='" + images + '\'' +
                ", image='" + image + '\'' +
                ", realProductType='" + realProductType + '\'' +
                ", fabric='" + fabric + '\'' +
                ", collar='" + collar + '\'' +
                ", fit='" + fit + '\'' +
                ", color='" + color + '\'' +
                ", bgColor='" + bgColor + '\'' +
                ", performance='" + performance + '\'' +
                ", sleeveLength='" + sleeveLength + '\'' +
                ", pattern='" + pattern + '\'' +
                ", gender='" + gender + '\'' +
                ", pngHeroImage='" + pngHeroImage + '\'' +
                ", pngImages=" + pngImages +
                ", variantsVOs=" + variantsVOs +
                ", matchPercentage=" + matchPercentage +
                ", updatedTimeInMillis=" + updatedTimeInMillis +
                '}';
    }
}

