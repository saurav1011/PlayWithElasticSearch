package controllers;

public class Product /// THIS IS ENCAPSULATION
{
    //non-nested fields
    private String color;
    private String title;
    private String realProductType;
    private String fit;
    private String product_type;
    private String collar;
    private String fabric;

    //nested fields
    private String searchTerms;
    private String tags;
    private String performance;

    public String getColor()
    {
        return color;
    }
    public  void setColor(String color)
    {
        this.color=color;
    }
    public String getTitle()
    {
        return title;
    }
    public  void setTitle(String title)
    {
        this.title=title;
    }
    public String getRealProductType()
    {
        return realProductType;
    }
    public  void setRealProductType(String realProductType)
    {
        this.realProductType=realProductType;
    }
    public String getFit()
    {
        return fit;
    }
    public  void setFit(String fit)
    {
        this.fit=fit;
    }
    public String getProduct_type()
    {
        return product_type;
    }
    public  void setProduct_type (String product_type)
    {
        this.product_type=product_type;
    }
    public String getCollar()
    {
        return collar;
    }
    public  void setCollar(String collar)
    {
        this.collar=collar;
    }
    public String getFabric()
    {
        return fabric;
    }
    public  void setFabric(String fabric)
    {
        this.fabric=fabric;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("garments{");
        sb.append("title='").append(title).append('\'');
        sb.append(", color='").append(color).append('\'');
        sb.append(", realProductType=").append(realProductType);
        sb.append(", fit=").append(fit);
        sb.append('}');
        return sb.toString();
    }

}
