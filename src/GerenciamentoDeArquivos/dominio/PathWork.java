package GerenciamentoDeArquivos.dominio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PathWork implements Serializable {
    private static final long serialVersionUID = 8451522680436957847L;
    private transient Path directory;
    private transient Path file;
    private String nomeAutor;

    public PathWork (){

    }

    public PathWork (Path directory){
        this.directory = directory;
    }

    public PathWork (Path path1, Path path2){
        this.directory = path1;
        this.file = path2;
    }


    public PathWork(Path directory, Path file, String nomeAutor) {
        this.directory = directory;
        this.file = file;
        this.nomeAutor = nomeAutor;
    }



    //desserilização para objetos não serelizaveis

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(directory.toString());
        out.writeObject(file != null ? file.toString() : null);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String directoryPath = (String) in.readObject();
        String filePath = (String) in.readObject();
        this.directory = Paths.get(directoryPath);
        this.file = (filePath != null) ? Paths.get(filePath) : null;
    }

    // Métodos de manipulação de arquivos

    public void createDirectory() {
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createFileDiretory(String nomeArquivo){
        if(Files.exists(directory)){
            Path fileIn = directory.resolve(nomeArquivo);
            try {
                if(Files.notExists(fileIn)){
                    Path fileCreated = Files.createFile(fileIn);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createFile(Path directory) {
        try {
            if (Files.notExists(file)) {
                Files.createFile(file);
                System.out.println("Arquivo criado com sucesso.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFile(String texto) {

        appendFile(file, texto);
        System.out.println("Arquivo escrito com sucesso!");

    }

    public static void appendFile(Path file, String texto){

        try {
            if(Files.notExists(file)) {
                Files.createFile(file);
            }
            Files.write(file, texto.getBytes(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile(Path file) {
        try {
            Files.lines(file).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile() {

        if (Files.exists(file)) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteDirecory() {

        try {
            Files.walkFileTree(directory, new DeleteFileVisitor());
            Files.delete(directory);

        } catch (IOException e) {
            e.getMessage();
        }
    }


    // Getters e Setters

    @Override
    public String toString() {
        return "PathWork{" +
                "directory=" + directory +
                ", file=" + file +
                ", nomeAutor='" + nomeAutor + '\'' +
                '}';
    }

    public Path getDirectory() {
        return directory;
    }

    public Path getFile() {
        return file;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public boolean isDirectory (){
        return directory!= null;
    }



}
