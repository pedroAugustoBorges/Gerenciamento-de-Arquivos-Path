package GerenciamentoDeArquivos.test;

import GerenciamentoDeArquivos.dominio.PathUtil;
import GerenciamentoDeArquivos.dominio.PathWork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class PathWorkTest {

    private static final String FILENAME = "pathworks.dat";
    private static final Path FILE_PATH = Paths.get(FILENAME);

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<PathWork> list = loadFiles(FILENAME);
        System.out.println("Bem vindo a central de gerenciamento de arquivos:");

        while (true) {
            showMenu();

            String entrada = input.nextLine().trim();

            try {
                int opcao = Integer.parseInt(entrada);

                switch (opcao) {
                    case 1:
                        createFIle(input, list);
                        saveFiles(list);
                        break;
                    case 2:
                        writeFile(input, list);
                        break;

                    case 3:
                        readFile(input, list);
                        break;
                    case 4:
                        deleteFile(input, list);
                        break;
                    case 5:
                        removeTextFromFile(input, list);
                        break;
                    case 6:
                       renameFile(input, list);
                        break;
                    case 7:
                        saveFiles(list);
                        System.out.println("Encerrando programa...");
                        return;
                    default:
                        System.out.println("Posição invalida. Tente novamente");

                }


            } catch (NumberFormatException e) {
                System.out.println("Numero invalido " + e.getMessage());
            }
        }

    }

    public static void showMenu() {
        System.out.println("1- Criar arquivo");
        System.out.println("2- Escrever no arquivo");
        System.out.println("3- Ler arquivo");
        System.out.println("4- Deletar arquivo");
        System.out.println("5- Deletar texto de arquivo");
        System.out.println("6- Renomear arquivo");
        System.out.println("7- Sair");
        System.out.println("Insira opção: ");
    }

    public static void createFIle(Scanner input, List<PathWork> list) {
        System.out.println("Insira um nome para o diretorio: ");
        String directoryName = input.nextLine();
        Path dir = Paths.get(directoryName);

        System.out.println("Insira um nome para o arquivo: ");
        String fileName = input.nextLine();
        Path file = Paths.get(directoryName, fileName);

        System.out.println("Insira um autor: ");
        String nome = input.nextLine();

        PathWork pathWork = new PathWork();

        if(Files.exists(dir)){
            pathWork = new PathWork(dir, file, nome);
            pathWork.createDirectory();
            pathWork.createFileDiretory(fileName);
            list.add(pathWork);
            PathUtil.saveFiles(list, FILENAME);
        }else {
            pathWork = new PathWork(dir, file, nome);
            pathWork.createDirectory();
            pathWork.createFile(dir);
            list.add(pathWork);
            PathUtil.saveFiles(list, FILENAME);
        }
    }

    public static void writeFile(Scanner input, List<PathWork> list){
        if (list.isEmpty()) {
            System.out.println("Não há arquivos para ler.");
            return;
        }

        list.forEach(pathWork -> System.out.println("- " + pathWork.getFile().getFileName()));

        System.out.println("Insira o nome do arquivo que deseja escrever: ");
        String arquivoWrite = input.nextLine();

        PathWork pathWork = findByName(arquivoWrite, list);
        if (pathWork != null){

            System.out.println("Insira o texto: ");
            StringBuilder sb = new StringBuilder();
            while (true) {
                String linha = input.nextLine();
                if (linha.equalsIgnoreCase("sair")) {
                    break;
                }
                sb.append(linha).append("\n");
            }


            String texto = sb.toString();
            pathWork.writeFile(texto);
            PathUtil.saveFiles(list, FILENAME);


        }
    }

    public static void removeTextFiles(Path file, String textoRemover, List<PathWork> list){
        try {

            if(!Files.exists(file)){
                System.out.println("Arquivo não encontrado");
                return;
            }
            List<String> linhas = Files.readAllLines(file);
            boolean textoEncontrado = false;
            StringBuilder sb = new StringBuilder();
            for(String linha: linhas){
                if(!linha.contains(textoRemover)){
                    sb.append(linha).append("\n");
                }else {
                   textoEncontrado = true;
                }

            }
            if(textoEncontrado){
                Files.write(file, sb.toString().getBytes());
                System.out.println("Texto removido com sucesso do arquivo!");
            }else {
                System.out.println("Texto não correspondente");
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeTextFromFile(Scanner input, List<PathWork> list){
        if (list.isEmpty()) {
            System.out.println("Não há arquivos para ler.");
            return;
        }

        list.forEach(pathWork -> System.out.println("- " + pathWork.getFile().getFileName()));

        System.out.println("Insira o nome do arquivo que deseja escrever: ");
        String arquivoWrite = input.nextLine();

        PathWork pathWork = findByName(arquivoWrite, list);

        if (pathWork != null) {

            System.out.println("Conteudo do arquivo: ");
            pathWork.readFile(pathWork.getFile());
            System.out.println();
            System.out.println("Insira o texto que deseja remover: ");
            String textoRemover = input.nextLine();
            removeTextFiles(pathWork.getFile(), textoRemover, list);
            PathUtil.saveFiles(list, FILENAME);
        }
    }

    public static void readFile(Scanner input, List<PathWork> list) {
        if (list.isEmpty()) {
            System.out.println("Não há arquivos para ler.");
            return;
        }

        System.out.println("Insira o nome do arquivo que deseja ler: ");
        String fileName = input.nextLine().trim();

        PathWork arquivoLer = findByName(fileName, list);
        if (arquivoLer != null) {
            arquivoLer.readFile(arquivoLer.getFile());
        } else {
            System.out.println("Arquivo não encontrado na lista.");
        }
    }

    public static void deleteFile(Scanner input, List<PathWork> list) {

        System.out.println("Você deseja excluir um diretorio ou arquivo? ");
        String opcao = input.nextLine();

        if (opcao.equalsIgnoreCase("diretorio")) {
            System.out.println("Diretorios: ");
            list.forEach(pathWork -> System.out.println("- " + pathWork.getDirectory().getFileName()));

            System.out.println("Insira o nome do diretorio que deseja remover: ");
            String arquivoRemove = input.nextLine();
            iteratorList(list, arquivoRemove, opcao);

        } else if (opcao.equalsIgnoreCase("arquivo")) {

            System.out.println("Arquivos e diretorios: ");
            list.forEach(pathWork -> System.out.println("- " + pathWork.getFile().getFileName()));
            System.out.println("Insira o nome do arquivo que deseja remover: ");
            String arquivoRemove = input.nextLine();
            iteratorList(list, arquivoRemove, opcao);

        } else {
            System.out.println("Resposta invalida!. Insira uma das duas opções (arquivo/diretorio)");

        }
    }

    public static void renameFile(Scanner input, List<PathWork> list){

        System.out.println("Você deseja renomear um diretorio ou arquivo? ");
        String opcao = input.nextLine();

        if (opcao.equalsIgnoreCase("diretorio")) {
            processRenaming(input, list, true);
        } else if (opcao.equalsIgnoreCase("arquivo")) {
            processRenaming(input, list, false);

        } else {
            System.out.println("Resposta invalida!. Insira uma das duas opções (arquivo/diretorio)");

        }


    }

    public static void processRenaming (Scanner input, List<PathWork> list, boolean isDirectory){

        if(isDirectory){
            System.out.println("Diretorios: ");
            list.forEach(pathWork -> System.out.println("- " + pathWork.getDirectory().getFileName()));
            System.out.println("Insira o nome do diretorio que deseja renomear: ");
        }else {
            System.out.println("Arquivos e diretorios: ");
            list.forEach(pathWork -> System.out.println("- " + pathWork.getFile().getFileName()));
            System.out.println("Insira o nome do arquivo que deseja renomear: ");
        }

        String itemRename = input.nextLine();
        List<PathWork> modifications = new ArrayList<>();
        boolean found = false;

        ListIterator<PathWork> iterator = list.listIterator();

      while(iterator.hasNext()){
          int index = iterator.nextIndex();
          PathWork pathWork = iterator.next();
          Path source = isDirectory ? pathWork.getDirectory() : pathWork.getFile();

          if (source != null && source.getFileName().toString().equalsIgnoreCase(itemRename)) {
              System.out.println("Insira o novo nome: ");
              String novoNome = input.nextLine();
              Path target = source.resolveSibling(novoNome);


              try {
                  Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                  System.out.println((isDirectory ? "Diretorio: " : "Arquivo") + " " + source.getFileName());

                  PathWork updatePathWork = new PathWork(
                          isDirectory ? target : pathWork.getDirectory(), isDirectory ? pathWork.getFile() : target);

                  list.set(index, updatePathWork);

                  if(isDirectory){
                      String oldName = pathWork.getDirectory().getFileName().toString();
                      String newName = target.getFileName().toString();

                      for(PathWork pw : list)
                          if(pw.isDirectory() && pw.getFile().startsWith(source)){
                              Path updateFile = source.relativize((pw.getFile()));
                              Path resolve = target.resolve(updateFile);
                              PathWork updatePath = new PathWork(pw.getDirectory(),resolve);
                              list.set(list.indexOf(pw), updatePath);
                          }
                  }

                  found = true;
                  break;

                } catch (IOException e) {
                    System.err.println("Erro ao renomear " + (isDirectory ? pathWork.getDirectory() : pathWork.getFile().getFileName()));
                }

            }
        }
        if(found){
            PathUtil.saveFiles(list, FILENAME);
                }else {
                    System.out.println("Item não encontrado para renomear.");
                }

    }

    public static PathWork findByName(String nomeArquivo, List<PathWork> list) {
        for (PathWork pathWork : list) {
            Path file = pathWork.getFile();
            if (file != null && file.getFileName() != null && file.getFileName().toString().equals(nomeArquivo)) {
                return pathWork;
            }
        }
        return null;
    }


    public static PathWork iteratorList(List<PathWork> list, String arquivo, String input) {
        Iterator<PathWork> iterator = list.iterator();
        while (iterator.hasNext()) {
            PathWork pathWork = iterator.next();

            if (pathWork.getFile() != null && pathWork.getFile().getFileName().toString().equals(arquivo)) {
                if(input.equalsIgnoreCase("arquivo")){
                    pathWork.deleteFile();
                    iterator.remove();
                    System.out.println("Arquivo excluido com sucesso! " + pathWork.getFile().getFileName());
                    saveFiles(list);
                    return pathWork;
                }
            } else if (pathWork.getDirectory() != null && pathWork.getDirectory().getFileName().toString().equals(arquivo)) {
                if (input.equalsIgnoreCase("diretorio")){
                  pathWork.deleteDirecory();
                    iterator.remove();
                    System.out.println("Diretorio excluido com sucesso: " + pathWork.getDirectory()+
                            "\nArquivos do diretorio: " + pathWork.getFile().getFileName());
                    saveFiles(list);
                    return pathWork;
                }
            }
           else {
                System.out.println("Arquivo não encontrado");
            }
        }
        return null;
    }


    public static void saveFiles(List<PathWork> list) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(FILE_PATH));
            oos.writeObject(list);
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //carregar dados
    public static List<PathWork> loadFiles(String FILENAME) {
        List<PathWork> list = new ArrayList<>();

        try {
            if (Files.exists(FILE_PATH)) {
                ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(FILE_PATH));
                System.out.println("Dados carregados com sucesso!");
                list = (List<PathWork>) ois.readObject();
                ois.close();
            } else {
                System.out.println("Arquivo de dados não encontrado " + FILENAME);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao acessar dados" + e.getMessage());
            throw new RuntimeException(e);
        }
        return list;


    }
}



