import java.util.Scanner;
import java.util.function.Consumer;
import Exceptions.AlreadyExistsException;
import Exceptions.NotFoundException;
import Models.Profile;
import Repositories.PostRepository;
import Repositories.ProfileRepository;
import Utils.IOUtils;

public class App {
    private SocialNetwork socialNetwork;
    private Scanner sc;
    public App(SocialNetwork socialNetwork, Scanner sc) {
        this.socialNetwork = socialNetwork;
        this.sc = sc;
    }
    // This class is used for menu options  
    private class Option {
        String title;
        Consumer<Object> callback;

        Option(String title, Consumer<Object> callback){
            this.title = title;
            this.callback = callback;
        }

        @Override
        public String toString() {
            return title;
        }

    }

    private Option[] options = {
        new Option("Create profile",  none -> {includeProfile();}),
        new Option("Search Profile", none -> {searchProfile();}),
        new Option("Find post", none -> {})
    };

    private void showMenu(Option...options){
        Integer optionNumber = 0;
        for (Option option : options) {
            System.out.println(String.format("%d - %s", ++optionNumber, option));         
        }
    }

    private void includeProfile(){
        try{
            String name = IOUtils.getText("Enter the profile username: ").toLowerCase().trim();
            String email = IOUtils.getText("Enter profile email: ").toLowerCase().trim();
            socialNetwork.includeProfile(new Profile(name, email));
            System.out.println("Usuário criado com sucesso");
        }catch(AlreadyExistsException e){
            System.out.println("CANNOT CREATE USER: " + e.getMessage());
            return;
        } catch(Exception e){
            System.out.println("Ocorreu um erro...");
            e.printStackTrace();
        }
    }
    //TODO: Talvez seja bom retornar a profile achada ( se achada ), para reuso em outros métodos
    private void searchProfile(){
        String searchTerm = IOUtils.getText("Enter the search term : [email/username]").toLowerCase().trim();
        try{
            Profile foundedbyEmail = socialNetwork.findProfileByEmail(searchTerm);
            System.out.println("Founded: " + foundedbyEmail);
        } catch( NotFoundException e){
            try{
                Profile foundedbyUsername = socialNetwork.findProfileByName(searchTerm);
                 System.out.println("Founded: " + foundedbyUsername);
            } catch( NotFoundException err){
                System.out.println("User not founded!");
                return;
            }
           
        }

    }

    //TODO: Terminar de criar o método para pesquisar em todas as opções (perfil, parte do post e hashtag)
    private void searchPost(){
        String searchTerm = IOUtils.getText("Enter the search parameter: [profile/phrase/hashtag]").toLowerCase().trim();

    }

    public void run(){
        Integer chosen;
        while (true) { // TODO: Analisar se é uma boa continuar com esse while true
             showMenu(options);
            // Controla a opção escolhida atual: entrada de dados do programa
            try{
                chosen = IOUtils.getInt("Enter a option: ");

                // verifica se é maior ou menor que o número de conteúdos da lista, senão for, continua...
                if(chosen > options.length || chosen < 0){
                    System.out.println("Please, digit a valid option number!");
                    continue;
                }
                if(chosen == 0){ // Opção sair: termina o loop
                    break;
                }
                // Escolhe a opção pelo que foi digitado - 1 (o indice real do array)
                options[chosen - 1].callback.accept(null);
                IOUtils.clearScreen();
            }
            catch(NumberFormatException e){
                System.out.println("Enter only numbers, please!");
            }
            
        }

        IOUtils.closeScanner(); // TODO: Achar um método mais "bonito" de fazer isso"

    }

    public static void main(String[] args) {
        SocialNetwork socialNetwork = new SocialNetwork(new ProfileRepository(), new PostRepository());
        App app = new App(socialNetwork, new Scanner(System.in));
        app.run();
    }
    
    
}
