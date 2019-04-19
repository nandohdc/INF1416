import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

                    System.out.println(this.MyHandler.getNFilesListFileInfo().get(index).getFilename() + " " + this.algorithm + " " + this.MyHandler.getNFilesListFileInfo().get(index).getDigestByAlgorithm(this.algorithm));
                }
            }
        }
    }

    private void CollisionDigestsCMD() {//Verifica se há entre os arqN de entradas do CMD
        for (int index = 0; index < this.MyHandler.getNFilesListFileInfo().size() - 1; index++) {
            for (int index2 = 0; index2 < this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).size() - 1; index2++) {
                if (this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).get(index2).getHashMapDigest().containsKey(this.algorithm)) {
                    if (this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).get(index2).getDigestByAlgorithm(this.algorithm).equals(this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index + 1).getFilename()).get(index2 + 1).getDigestByAlgorithm(this.algorithm))) {
                        this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).get(index2).setFileStatus("COLLISION");
                        this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index + 1).getFilename()).get(index2 + 1).setFileStatus("COLLISION");
                    }
                }
            }
        }
    }

    private void CollisionDigestsListDigests() {//verificar se há colisão entre os arqN de entrada e ListadeDigests
        for (int i = 0; i < this.MyHandler.getDigestListFileInfo().size(); i++) {
            for (int j = 0; j < this.MyHandler.getListaDigest().get(this.MyHandler.getDigestListFileInfo().get(i).getFilename()).size(); j++) {
                for (int k = 0; k < this.MyHandler.getNFilesListFileInfo().size(); k++) {
                    for (int l = 0; l < this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).size(); l++) {
                        if (this.MyHandler.getListaDigest().get(this.MyHandler.getDigestListFileInfo().get(i).getFilename()).get(j).getFilename().equals(this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).get(l).getFilename())) {
                            if (this.MyHandler.getListaDigest().get(this.MyHandler.getDigestListFileInfo().get(i).getFilename()).get(j).getDigestByAlgorithm(this.algorithm).equals(this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).get(l).getDigestByAlgorithm(this.algorithm))) {
                                this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).get(l).setFileStatus("OK");
                            } else {
                                this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).get(l).setFileStatus("NOT OK");
                            }
                        } else {
                            if (this.MyHandler.getListaDigest().get(this.MyHandler.getDigestListFileInfo().get(i).getFilename()).get(j).getDigestByAlgorithm(this.algorithm).equals(this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).get(l).getDigestByAlgorithm(this.algorithm))) {
                                this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).get(l).setFileStatus("COLLISION");
                            } else {
                                this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(k).getFilename()).get(l).setFileStatus("NOT FOUND");
                            }
                        }
                    }
                }
            }
        }
    }

    private void printAllStatus(){
        for (int index = 0; index < this.MyHandler.getNFilesListFileInfo().size(); index++) {
            for (int index2 = 0; index2 < this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).size(); index2++) {
                String filename = this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).get(index2).getFilename();
                String type = this.algorithm;
                String digest = this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).get(index2).getDigestByAlgorithm(this.algorithm);
                String status = this.MyHandler.getNFilesDigest().get(this.MyHandler.getNFilesListFileInfo().get(index).getFilename()).get(index2).getFileStatus();

                String output = filename + " " + type + " " + digest + " " +status;

                System.out.println(output);
            }
        }

    }

    public void CompareAllDigests() {
        this.CollisionDigestsCMD();
        this.CollisionDigestsListDigests();
        this.printAllStatus();
    }
}
