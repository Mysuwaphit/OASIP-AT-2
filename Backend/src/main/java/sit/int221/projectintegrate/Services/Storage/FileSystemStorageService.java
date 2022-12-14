package sit.int221.projectintegrate.Services.Storage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, Integer eventId) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path newPath = newFolder(eventId.toString());
            Path destinationFile = newPath.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
//			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
//				// This is a security check
//				throw new StorageException(
//						"Cannot store file outside current directory.");
//			}
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Path newFolder(String folderName) throws IOException {
        File folder = new File(rootLocation + "\\" + folderName);
        Path pathWithFolder = Paths.get(folder.getPath());
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println(rootLocation + "\\" + folderName);
                return pathWithFolder;
            } else {
                System.out.println("Failed to create directory!");
            }
        }else {
            FileUtils.cleanDirectory(folder);
            System.out.println(rootLocation + "\\" + folderName);
            return pathWithFolder;
        }
        return pathWithFolder;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(Integer eventId, String filename) {
        return rootLocation.resolve(String.valueOf(eventId)).resolve(filename);
    }

    @Override
    public Resource loadAsResource(Integer eventId, String filename) {
        try {
            Path file = load(eventId, filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public List<String> listFileName(Integer eventId) {
        File folder = new File(rootLocation.resolve(String.valueOf(eventId)).toUri());
        List<File> listOfFiles = List.of(folder.listFiles());
        List<String> listOfFilenames = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                listOfFilenames.add(file.getName());
                System.out.println(listOfFilenames);
            }
        }
        return listOfFilenames;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void deleteFileById(Integer eventId) {
        String dest = eventId.toString();
        FileSystemUtils.deleteRecursively(rootLocation.resolve(dest).toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
