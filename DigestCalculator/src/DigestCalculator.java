import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DigestCalculator {

    private FileHandler MyHandler;
    private String algorithm;

    public DigestCalculator(String newPathToCaminho_ArqListaDigest, List<String> NewNArquivos, String NewTypeDigest) throws NoSuchAlgorithmException {
        this.MyHandler = new FileHandler(newPathToCaminho_ArqListaDigest, NewNArquivos);
        this.algorithm = NewTypeDigest;
    }

    public void initDigest() {
        this.MyHandler.readCaminho_ArqListaDigest();
        this.MyHandler.readCaminho_ListNFiles();
    }

    private byte[] DigestByChunk(File newFile) throws NoSuchAlgorithmException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(newFile);
            MessageDigest myMessageDigest = MessageDigest.getInstance(this.algorithm);
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            while ((bytesCount = fis.read(byteArray)) != -1) {
                myMessageDigest.update(byteArray, 0, bytesCount);
            }

            fis.close();

            return myMessageDigest.digest();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException a) {
            a.printStackTrace();
            return null;
        }
    }

    public void CalcuateDigest() throws IOException, NoSuchAlgorithmException {

        for (int index = 0; index < this.MyHandler.getNFilesListFileInfo().size(); index++) {
            File file = new File(this.MyHandler.getNFilesListFileInfo().get(index).getPath());

            if (file.getAbsoluteFile().exists() && file.getAbsoluteFile().isFile()) {

                byte[] myDigest = this.DigestByChunk(file);

                if (myDigest != null) {
                    StringBuffer buf = new StringBuffer();

                    for (int i = 0; i < myDigest.length; i++) {
                        String hex = Integer.toHexString(0x0100 + (myDigest[i] & 0x00FF)).substring(1);
                        buf.append((hex.length() < 2 ? "0" : "") + hex);
                    }
                    this.MyHandler.getNFilesListFileInfo().get(index).addAlgortihmDigest(this.algorithm, buf.toString());

                    //System.out.println(this.MyHandler.getNFilesListFileInfo().get(index).getFilename() + " " + this.algorithm + " " + this.MyHandler.getNFilesListFileInfo().get(index).getDigestByAlgorithm(this.algorithm));
                }
            }
        }
    }

    private String Compare(FileInfo fileInfo){
        String[] split = fileInfo.getFilename().split("\\\\");
        String name = split[split.length - 1];
        for(FileInfo fileInfo2 : MyHandler.getDigestListFileInfo()) {
            if (fileInfo2.getFilename().equals(name)) {
                if (fileInfo2.getHashMapDigest().containsKey(this.algorithm)) {
                    if (fileInfo.getHashMapDigest().get(this.algorithm).equals(fileInfo2.getHashMapDigest().get(this.algorithm))) {
                        printStatus(fileInfo, "OK");
                        return "OK";
                    } else {
                        printStatus(fileInfo, "NOT OK");
                        return "NOT OK";
                    }
                } else {
                    fileInfo2.getHashMapDigest().put(this.algorithm, fileInfo.getHashMapDigest().get(this.algorithm));
                    printStatus(fileInfo, "NOT FOUND");
                    return "NOT FOUND";
                }
            }
        }
        for (FileInfo fileInfo1 : MyHandler.getDigestListFileInfo()) {
            if (fileInfo1.getHashMapDigest().containsKey(this.algorithm) && fileInfo1.getHashMapDigest().get(this.algorithm).equals(fileInfo.getDigestByAlgorithm(this.algorithm))) {
                printStatus(fileInfo, "COLLISION");
                return "COLLISION";
            }
        }
        fileInfo.setFilename(name);
        MyHandler.getDigestListFileInfo().add(fileInfo);
        printStatus(fileInfo, "NOT FOUND");
        return "NOT FOUND";
    }

    private void printStatus(FileInfo fileInfo, String status){
        String filename = fileInfo.getFilename();
        String type = this.algorithm;
        String digest = fileInfo.getDigestByAlgorithm(this.algorithm);

        String output = filename + " " + type + " " + digest + " " + status;
        System.out.println(output);
    }

    private void Loop() throws IOException {

        for(FileInfo fileInfo : MyHandler.getNFilesListFileInfo()){
            Compare(fileInfo);
        }

        PrintWriter writer = new PrintWriter(this.MyHandler.getCaminho_ArqListaDigest());

        for(int i = 0; i < this.MyHandler.getDigestListFileInfo().size(); i++){
            String filename = this.MyHandler.getDigestListFileInfo().get(i).getFilename();

            String digests="";

            for(String key: this.MyHandler.getDigestListFileInfo().get(i).getHashMapDigest().keySet()){
                String digest = this.MyHandler.getDigestListFileInfo().get(i).getHashMapDigest().get(key);
                digests = digests + key + " " + digest + " ";
            }

            String output = filename + " " + digests;

            writer.println(output);

        }

        writer.close();
    }

    public void CompareAllDigests() {
        try {
            Loop();
        } catch (Exception e){

        }

    }
}
