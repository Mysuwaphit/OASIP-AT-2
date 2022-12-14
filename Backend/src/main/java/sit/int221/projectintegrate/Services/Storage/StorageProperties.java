package sit.int221.projectintegrate.Services.Storage;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.stereotype.Component;
@Component

@ConfigurationProperties("storage2")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
