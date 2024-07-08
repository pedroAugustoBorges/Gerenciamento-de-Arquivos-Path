package GerenciamentoDeArquivos.dominio;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PathUtil  {


    public static void saveFiles(List<PathWork> list, String FILENAME){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Path.of(FILENAME)));
            oos.writeObject(list);
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PathWork> loadFiles(String FILENAME){
        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Path.of(FILENAME)));
            System.out.println("Dados carregador com sucesso!");
            return (List<PathWork>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




}
