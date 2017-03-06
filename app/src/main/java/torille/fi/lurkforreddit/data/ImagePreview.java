package torille.fi.lurkforreddit.data;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Model containing preview images
 */
@Parcel
public class ImagePreview {

    @SerializedName("images")
    public List<Image> images = new ArrayList<>();

    public List<Image> getImages() {
        return images;
    }

    @ParcelConstructor
    public ImagePreview(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImagePreview{" +
                "images=" + images +
                "}";
    }
}