
package com.saurav.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndexProperties {

	@SerializedName("id")
	@Expose
	private Id id;
	@SerializedName("title")
	@Expose
	private Title title;
	@SerializedName("productType")
	@Expose
	private ProductType productType;
	@SerializedName("tags")
	@Expose
	private Tags tags;
	@SerializedName("fit")
	@Expose
	private Fit fit;
	@SerializedName("color")
	@Expose
	private Color color;
	@SerializedName("performance")
	@Expose
	private Performance performance;
	@SerializedName("fabric")
	@Expose
	private Fabric fabric;
	@SerializedName("collar")
	@Expose
	private Collar collar;
	@SerializedName("searchTerms")
	@Expose
	private SearchTerms searchTerms;
	@SerializedName("searchTermsAsList")
	@Expose
	private SearchTermsAsList searchTermsAsList;
	@SerializedName("realProductType")
	@Expose
	private RealProductType realProductType;
	@SerializedName("bodyHtml")
	@Expose
	private BodyHtml bodyHtml;
	@SerializedName("handle")
	@Expose
	private Handle handle;
	@SerializedName("published_scope")
	@Expose
	private PublishedScope publishedScope;
	@SerializedName("images")
	@Expose
	private Images images;
	@SerializedName("image")
	@Expose
	private Image image;
	@SerializedName("bgColor")
	@Expose
	private BgColor bgColor;
	@SerializedName("sleeveLength")
	@Expose
	private SleeveLength sleeveLength;
	@SerializedName("pattern")
	@Expose
	private Pattern pattern;
	@SerializedName("gender")
	@Expose
	private Gender gender;
	@SerializedName("pngHeroImage")
	@Expose
	private PngHeroImage pngHeroImage;
	@SerializedName("pngImages")
	@Expose
	private PngImages pngImages;
	@SerializedName("variantsVOs")
	@Expose
	private VariantsVOs variantsVOs;
	@SerializedName("matchPercentage")
	@Expose
	private MatchPercentage matchPercentage;
	@SerializedName("updatedTimeInMillis")
	@Expose
	private UpdatedTimeInMillis updatedTimeInMillis;

//	@SerializedName("address")
//	@Expose
//	private Address address;
//
//	public Address getAddress() {
//		return address;
//	}
//
//	public void setAddress(Address address) {
//		this.address = address;
//	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	public Fit getFit() {
		return fit;
	}

	public void setFit(Fit fit) {
		this.fit = fit;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Performance getPerformance() {
		return performance;
	}

	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	public Fabric getFabric() {
		return fabric;
	}

	public void setFabric(Fabric fabric) {
		this.fabric = fabric;
	}

	public Collar getCollar() {
		return collar;
	}

	public void setCollar(Collar collar) {
		this.collar = collar;
	}

	public SearchTerms getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(SearchTerms searchTerms) {
		this.searchTerms = searchTerms;
	}

	public SearchTermsAsList getSearchTermsAsList() {
		return searchTermsAsList;
	}

	public void setSearchTermsAsList(SearchTermsAsList searchTermsAsList) {
		this.searchTermsAsList = searchTermsAsList;
	}

	public RealProductType getRealProductType() {
		return realProductType;
	}

	public void setRealProductType(RealProductType realProductType) {
		this.realProductType = realProductType;
	}

	public BodyHtml getBodyHtml() {
		return bodyHtml;
	}

	public void setBodyHtml(BodyHtml bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	public Handle getHandle() {
		return handle;
	}

	public void setHandle(Handle handle) {
		this.handle = handle;
	}

	public PublishedScope getPublishedScope() {
		return publishedScope;
	}

	public void setPublishedScope(PublishedScope publishedScope) {
		this.publishedScope = publishedScope;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public BgColor getBgColor() {
		return bgColor;
	}

	public void setBgColor(BgColor bgColor) {
		this.bgColor = bgColor;
	}

	public SleeveLength getSleeveLength() {
		return sleeveLength;
	}

	public void setSleeveLength(SleeveLength sleeveLength) {
		this.sleeveLength = sleeveLength;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public PngHeroImage getPngHeroImage() {
		return pngHeroImage;
	}

	public void setPngHeroImage(PngHeroImage pngHeroImage) {
		this.pngHeroImage = pngHeroImage;
	}

	public PngImages getPngImages() {
		return pngImages;
	}

	public void setPngImages(PngImages pngImages) {
		this.pngImages = pngImages;
	}

	public VariantsVOs getVariantsVOs() {
		return variantsVOs;
	}

	public void setVariantsVOs(VariantsVOs variantsVOs) {
		this.variantsVOs = variantsVOs;
	}

	public MatchPercentage getMatchPercentage() {
		return matchPercentage;
	}

	public void setMatchPercentage(MatchPercentage matchPercentage) {
		this.matchPercentage = matchPercentage;
	}

	public UpdatedTimeInMillis getUpdatedTimeInMillis() {
		return updatedTimeInMillis;
	}

	public void setUpdatedTimeInMillis(UpdatedTimeInMillis updatedTimeInMillis) {
		this.updatedTimeInMillis = updatedTimeInMillis;
	}


}
