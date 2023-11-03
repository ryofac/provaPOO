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
        new Option("Search Profile", none -> {searchProfile();})
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
            System.out.println(e.getMessage());
            return;
        } catch(Exception e){
            System.out.println("Ocorreu um erro...");
            e.printStackTrace();
        }
    }

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

    public void run(){
        //TODO: Fazer com que o menu dependa somente das opções (No caso de não exibir opções não disponíveis)
        do{
            showMenu(options);
            switch (sc.nextInt()) {
                case 1:
                    options[0].callback.accept(null);
                    break;
                case 2:
                    options[1].callback.accept(null);
                    break; 
            }
        }while( sc.nextInt() != 0);

        IOUtils.closeScanner(); // TODO: Achar um método mais "bonito" de fazer isso"

    }

    public static void main(String[] args) {
        SocialNetwork socialNetwork = new SocialNetwork(new ProfileRepository(), new PostRepository());
        //TODO: concertar scanner pedindo mais de uma linha no looping principal
        App app = new App(socialNetwork, new Scanner(System.in));
        app.run();
    }
    
    
}
