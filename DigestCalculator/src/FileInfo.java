import java.util.HashMap;

public class FileInfo {

    private String filename;
    private String path;
    private HashMap<String, String> HashMapDigest;
    private String fileStatus;

    public FileInfo (String newPath){
        this.setPath(new String(newPath));
        this.setHashMapDigest(new HashMap<String, String>());
        this.setFileStatus(null);
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, String> getHashMapDigest() {
        return this.HashMapDigest;
    }

    public void setHashMapDigest(HashMap<String, String> hashMapDigest) {
        this.HashMapDigest = hashMapDigest;
    }

    public void addAlgortihmDigest(String newAlgorithm, String newDigest){
        this.getHashMapDigest().put(newAlgorithm,newDigest);
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getDigestByAlgorithm(String Algorithm){
        return this.getHashMapDigest().get(Algorithm);
    }
}
