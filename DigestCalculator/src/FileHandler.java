import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.HashMap; // import the HashMap class

public class FileHandler {
    private String Caminho_ArqListaDigest; //Informa a localização do arquivo que contém uma lista de digests de arquivos conhecidos.
    private List<String> InfoArqListaDigest;
    private List<String> ListNFiles;
    private List<FileInfo> DigestListFileInfo;
    private List<FileInfo> NFilesListFileInfo;
    private HashMap<String, List<FileInfo>> ListaDigest;
    private HashMap<String, List<FileInfo>> NFilesDigest;

    public FileHandler(String newPathToCaminho_ArqListaDigest, List<String> NewNArquivos){
        this.setCaminho_ArqListaDigest(newPathToCaminho_ArqListaDigest);
        this.setListNFiles(NewNArquivos);
        this.setListaDigest(new HashMap<String, List<FileInfo>>());
        this.setNFilesDigest(new HashMap<String, List<FileInfo>>());
        this.setDigestListFileInfo(new ArrayList<FileInfo>());
        this.setNFilesListFileInfo(new ArrayList<FileInfo>());
    }

    public void readCaminho_ArqListaDigest(){

        File file = new File(this.getCaminho_ArqListaDigest());

        if(file.exists() && file.isFile()) {
            this.InfoArqListaDigest = new ArrayList<String>();

            try {
                this.InfoArqListaDigest = Files.readAllLines(Paths.get(this.getCaminho_ArqListaDigest()));
                for (String line : InfoArqListaDigest) {
                    String[] infos = line.split(" ");

                    if (infos.length <= 0) {
                        System.out.println("readCaminho_ArqListaDigest: infos nulo.\n");
                        System.exit(1);
                    } else if (infos.length > 5) {
                        System.out.println("readCaminho_ArqListaDigest: infos muito grande.\n");
                        System.exit(1);
                    } else if (infos.length > 3) {
                        FileInfo element = new FileInfo(this.Caminho_ArqListaDigest);
                        element.setFilename(infos[0]);
                        element.addAlgortihmDigest(infos[1],infos[2]);
                        element.addAlgortihmDigest(infos[3],infos[4]);
                        this.getDigestListFileInfo().add(element);
                        this.getListaDigest().put(infos[0], this.getDigestListFileInfo());
                    } else {
                        FileInfo element = new FileInfo(infos[0]);
                        element.setFilename(infos[0]);
                        element.addAlgortihmDigest(infos[1],infos[2]);
                        this.getDigestListFileInfo().add(element);
                        this.getListaDigest().put(infos[0], this.getDigestListFileInfo());
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void readCaminho_ListNFiles(){
        for(int index = 0; index < this.getListNFiles().size(); index++){
            String[] temp = this.getListNFiles().get(index).split("/");
            String filename = temp[temp.length-1];
            FileInfo element = new FileInfo(this.getListNFiles().get(index));
            element.setFilename(filename);
            this.getNFilesListFileInfo().add(element);
            this.getNFilesDigest().put(filename, this.getNFilesListFileInfo());
        }
    }

    public void writeComparison(String newDigest ){
        try {
            Files.write(Paths.get(this.Caminho_ArqListaDigest), newDigest.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public String getCaminho_ArqListaDigest() {
        return this.Caminho_ArqListaDigest;
    }

    public void setCaminho_ArqListaDigest(String caminho_ArqListaDigest) {
        this.Caminho_ArqListaDigest = caminho_ArqListaDigest;
    }

    public List<String> getInfoArqListaDigest() {
        return this.InfoArqListaDigest;
    }

    public void setInfoArqListaDigest(List<String> infoArqListaDigest) {
        this.InfoArqListaDigest = infoArqListaDigest;
    }

    public List<String> getListNFiles() {
        return this.ListNFiles;
    }

    public void setListNFiles(List<String> listNFiles) {
        this.ListNFiles = listNFiles;
    }

    public List<FileInfo> getDigestListFileInfo() {
        return this.DigestListFileInfo;
    }

    public void setDigestListFileInfo(List<FileInfo> digestListFileInfo) {
        this.DigestListFileInfo = digestListFileInfo;
    }

    public List<FileInfo> getNFilesListFileInfo() {
        return this.NFilesListFileInfo;
    }

    public void setNFilesListFileInfo(List<FileInfo> NFilesListFileIndo) {
        this.NFilesListFileInfo = NFilesListFileIndo;
    }

    public HashMap<String, List<FileInfo>> getListaDigest() {
        return this.ListaDigest;
    }

    public void setListaDigest(HashMap<String, List<FileInfo>> listaDigest) {
        this.ListaDigest = listaDigest;
    }

    public HashMap<String, List<FileInfo>> getNFilesDigest() {
        return this.NFilesDigest;
    }

    public void setNFilesDigest(HashMap<String, List<FileInfo>> NFilesDigest) {
        this.NFilesDigest = NFilesDigest;
    }
}
