package closetics.Clothes.ClothingImages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    private String filePath;

    private byte[] imageData;

    public Image(long id, String filePath) throws IOException {
        this.id = id;
        this.filePath = filePath;
        File imageFile = new File(filePath);
        this.imageData = Files.readAllBytes(imageFile.toPath());
    }
    public Image() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getImageData(){
        return imageData;
    }
    public void setImageData(byte[] imageData){
        this.imageData = imageData;
    }
    public void readImage() throws IOException {
        File imageFile = new File(filePath);
        this.imageData = Files.readAllBytes(imageFile.toPath());
    }
}

