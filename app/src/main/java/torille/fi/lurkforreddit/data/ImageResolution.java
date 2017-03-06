package torille.fi.lurkforreddit.data;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Model containing information about image resolution
 */

@Parcel
public class ImageResolution {

    @SerializedName("url")
    public String url;
    @SerializedName("width")
    public Integer width;
    @SerializedName("height")
    public Integer height;

    public ImageResolution() {
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "ImageResolution{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}