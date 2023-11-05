import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import Exceptions.AlreadyExistsException;
import Exceptions.NotFoundException;
import Models.Post;
import Models.Profile;

import Utils.IOUtils;

public class App {
    private SocialNetwork socialNetwork;
    public App(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }
    // This class is used for menu options  
    private class Option {
        String title; // Nome que será mostrado
        Consumer<Object> callback; // função que será chamada pela opcao 
        Boolean canShow; // define se pode ser mostrada ou não

        Option(String title, Consumer<Object> callback, Boolean canShow){
            this.title = title;
            this.callback = callback;
            this.canShow = canShow;
        }

        @Override
        public String toString() {
            return title;
        }

    }

    private Option[] options = {
        new Option("Create profile",  none -> {includeProfile();}, true),
        new Option("Create Post", none -> {createPost();}, true),
        new Option("Show Feed", none -> {showAllPosts();}, true),
        new Option("Search Profile", none -> {searchProfile();}, true),
        new Option("Search Post", none -> {searchPost();}, true)
       
    };

    private void showMenu(Option...options){
        String title = "==== LIVBOOK ====";
        Integer optionNumber = 0;
        System.out.println(title);
        for (Option option : options) {
            if(option.canShow){
                System.out.println(String.format("%d - %s", ++optionNumber, option));
                
            }
        }
    }

    private void includeProfile(){
        try{
            String name = IOUtils.getTextNormalized("Enter the profile username: ");
            String email = IOUtils.getTextNormalized("Enter profile email: ");
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
        String searchTerm = IOUtils.getTextNormalized("Enter the search term : [email/username] \n> ");
        try{
            Profile foundedbyEmail = socialNetwork.findProfileByEmail(searchTerm);
            System.out.println("Founded: " + foundedbyEmail);
        } catch( NotFoundException e){
            try{
                Profile foundedbyUsername = socialNetwork.findProfileByName(searchTerm);
                 System.out.println("Founded: " + foundedbyUsername);
            } catch( NotFoundException err){
                System.out.println("User not founded!");
            }
           
        }

    }

    private void showAllPosts(){
        System.out.println("FEED - " + LocalDateTime.now());
        socialNetwork.showAllPosts();
        System.out.println("==========");
    }

    private void createPost(){
        System.out.println("Autenticate...");
        var name = IOUtils.getText("Enter your name: ");
        var email = IOUtils.getText("Enter your email: ");
        try{
            var foundedByEmail = socialNetwork.findProfileByEmail(email);
            var foundedByName = socialNetwork.findProfileByName(name);
            if(foundedByEmail != foundedByName){
                System.out.println("Autentication failed!");
                return;
            }
            String text = IOUtils.getTextNormalized("What you want to share: \n");
            socialNetwork.includePost(new Post(null, text, foundedByName));
        } catch(NotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    //TODO: Terminar de criar o método para pesquisar em todas as opções (perfil, parte do post e hashtag)
    private void searchPost(){
        String searchTerm = IOUtils.getTextNormalized("Enter the search parameter: [profile username/phrase/hashtag]\n > ");
        try{
            Profile userFoundedByName = socialNetwork.findProfileByName(searchTerm);
            List<Post> postsFoundedByUser = socialNetwork.findPostsbyOwner(userFoundedByName);
            System.out.println("Founded by user: ");
            for(Post post : postsFoundedByUser){
                System.out.println(post);
            }
        } catch (NotFoundException e){
            System.out.println("No posts founded by user");
        }
        try{
            List<Post> postsFoundedByText = socialNetwork.findPostByText(searchTerm);
            System.out.println("Founded by text: ");
            for (Post post : postsFoundedByText) {
                System.out.println(post);
                
            }
        } catch(NotFoundException e){
            System.out.println("No posts founded by text");
        }
        
        
        try{
            List<Post> postsFoundedByHashtag = socialNetwork.findPostByHashtag(searchTerm);
            System.out.println("Founded by hashtag: ");
            for (Post post : postsFoundedByHashtag) {
                System.out.println(post);
            }
        } catch(NotFoundException err){
            System.out.println("No posts founded by hashtag");
        }

        //nao sei se vai ser util, mas ta ai
        try{
            List<Post> postsFoundedByHashtagInText = socialNetwork.findPostByHashtagInText(searchTerm);
            System.out.println("Founded by hashtag in text: ");
            for (Post post : postsFoundedByHashtagInText) {
                System.out.println(post);
            }
        } catch(NotFoundException err){
            System.out.println("No posts founded by hashtag in text");
        }
        //implementar nas opções de menu
        try{
            List<Post> postsFoundedByPhrase = socialNetwork.findPostByPhrase(searchTerm);
            System.out.println("Founded by phrase: ");          
            for (Post post : postsFoundedByPhrase) {
                System.out.println(post);
            }
        } catch(NotFoundException err){
            System.out.println("No posts founded by phrase");
        }
    }

    public void run(){
        Integer chosen;
        while (true) { // TODO: Analisar se é uma boa continuar com esse while true (acho que está bom assim)
             showMenu(options);
            // Controla a opção escolhida atual: entrada de dados do programa
            try{
                chosen = IOUtils.getInt("Enter a option: \n> ");

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
            }
            catch(NumberFormatException e){
                System.out.println("Enter only numbers, please!");
            }
            
            IOUtils.clearScreen();
            
        }

        IOUtils.closeScanner(); // TODO: Achar um método mais "bonito" de fazer isso"

    }
    
}
