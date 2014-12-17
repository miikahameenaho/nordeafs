package com.example.mixu.nordeafs;

public class FSVenue {

    private String name = null;
    private String city = null; // Not shown on UI currently
    private String category = null; // Not shown on UI currently
    private String icon_url = null; // Not shown on UI currently
    private String formatted_address = null;
    private String distance = null;

    public FSVenue() {
    }

    public String getCity() {
        if (city == null) {
            return "";
        }
        return city;
    }

    public void setCity(String city) {
	        this.city = city;
	}

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

	public void setName(String name) {
        this.name = name;
	}

	public String getCategory() {
        if (category == null) {
            return "";
        }
	    return category;
	}

	public void setCategory(String category) {
	    this.category = category;
	}

    public String getIconUrl() {
        if (icon_url == null) {
            return "";
        }
        return icon_url;
    }

    public void setIconUrl(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getFormattedAddress() {
        if (formatted_address == null) {
            return "";
        }
        return formatted_address;
    }

    public void setFormattedAddress(String formatted_address) {
        this.formatted_address = formatted_address.replaceAll("\\[", "")
                                                  .replaceAll("\\]", "")
                                                  .replaceAll("\\\"", "");
    }

    public String getDistance() {
        if (distance == null) {
            return "";
        }
        return distance + " m";
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
