import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import Exceptions.AlreadyExistsException;
import Exceptions.NotFoundException;
import Models.AdvancedPost;
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

        Option(String title, Consumer<Object> callback, Boolean canShow) {
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
            new Option("Create profile", none -> {
                includeProfile();
            }, true),
            new Option("Create Post", none -> {
                createPost();
            }, true),
            new Option("Show Feed", none -> {
                showAllPosts();
            }, true),
            new Option("Search Profile", none -> {
                searchProfile();
            }, true),
            new Option("Search Post", none -> {
                searchPost();
            }, true)

    };

    private void showMenu(Option... options) {
        String title = "==== LIVBOOK ====";
        Integer optionNumber = 0;
        System.out.println(title);
        for (Option option : options) {
            if (option.canShow) {
                System.out.println(String.format("%d - %s", ++optionNumber, option));
            }
        }
    }

    private void includeProfile() {
        try {
            String name = IOUtils.getTextNormalized("Enter the profile username: ");
            String email = IOUtils.getTextNormalized("Enter profile email: ");
            socialNetwork.includeProfile(new Profile(name, email));
            System.out.println("User created!");
        } catch (AlreadyExistsException e) {
            System.out.println("CANNOT CREATE USER: " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("Ocorreu um erro...");
            e.printStackTrace();
        }
    }

    private void searchProfile() {
        String searchTerm = IOUtils.getTextNormalized("Enter the search term : [email/username] \n> ");
        try {
            Profile foundedbyEmail = socialNetwork.findProfileByEmail(searchTerm);
            System.out.println("Founded: " + foundedbyEmail);
        } catch (NotFoundException e) {
            try {
                Profile foundedbyUsername = socialNetwork.findProfileByName(searchTerm);
                System.out.println("Founded: " + foundedbyUsername);
            } catch (NotFoundException err) {
                System.out.println("User not founded!");
            }

        }

    }

    private void showAllPosts() {
        System.out.println("FEED - " + LocalDateTime.now());
        socialNetwork.showAllPosts();
        System.out.println("==========");
    }

    // Função acessória para achar hashtags diretamente o texto
    private List<String> findHashtagInText(String text) {
        List<String> result = new ArrayList<>();
        boolean coletting = false;
        String actualHashtag = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '#' || coletting) {
                actualHashtag += text.charAt(i);
                coletting = true;
                if (text.charAt(i) == ' ' || text.charAt(i) == '\n' || i + 1 == text.length()) {
                    result.add(actualHashtag);
                    coletting = false;
                    actualHashtag = "";
                }
            }
        }
        return result;

    }

    private void createPost() {
        System.out.println("Autenticate...");
        String name = IOUtils.getText("Enter your name: ");
        String email = IOUtils.getText("Enter your email: ");
        try {
            Profile foundedByEmail = socialNetwork.findProfileByEmail(email);
            Profile foundedByName = socialNetwork.findProfileByName(name);
            if (foundedByEmail != foundedByName) {
                System.out.println("Autentication failed!");
                return;
            }
            String text = IOUtils.getTextNormalized("What do you want to share with world today? \n > ");
            List<String> hashtagsFounded = findHashtagInText(text);
            if (hashtagsFounded.size() > 0)
                System.out.println("Warn: you can only embed hashtags in a advanced post");
            Boolean isAdvanced = IOUtils.getChoice("Do you want to turn this into a advanced post? ");

            Post created;
            if (isAdvanced) {
                Integer remainingViews = IOUtils.getInt("Set the max views: ");
                created = new AdvancedPost(null, text, foundedByEmail, remainingViews);
            } else {
                created = new Post(null, text, foundedByEmail);

            }
            // hashtags vão ser adcionadas a medida que são encontradas no próprio texto
            for (String hashtag : hashtagsFounded) {
                text = text.replace(hashtag.trim(), " ");
                if (created instanceof AdvancedPost) {
                    ((AdvancedPost) created).addHashtag(hashtag);
                } else {
                    System.out.println("Hashtag " + hashtag + " removed: you have to create an advanced post");
                }

            }
            created.setText(text);
            socialNetwork.includePost(created);
            System.out.println("Post added to feed!");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchPost() {
        String searchTerm = IOUtils.getTextNormalized("Enter the search parameter: [profile username/phrase/hashtag]\n > ");
        try {
            Profile userFoundedByName = socialNetwork.findProfileByName(searchTerm);
            socialNetwork.showPostsPerProfile(userFoundedByName);
        } catch (NotFoundException e) {
            System.out.println("No posts founded by user");
        }
        try {
            List<Post> postsFoundedByText = socialNetwork.findPostByPhrase(searchTerm);
            System.out.println("===== Founded by text: =====");
            for (Post post : postsFoundedByText) {
                System.out.println(post);

            }
        } catch (NotFoundException e) {
            System.out.println("No posts founded by text");
        }
        try {
            socialNetwork.showPostsPerHashtag(searchTerm);
        } catch (NotFoundException err) {
            System.out.println("No posts founded by hashtag");
        }

        // nao sei se vai ser util, mas ta ai
        // tive uma ideia melhor, as hashtags serão caçadas e adcionadas no texto
        // try{
        // List<Post> postsFoundedByHashtagInText =
        // socialNetwork.findPostByHashtagInText(searchTerm);
        // System.out.println("Founded by hashtag in text: ");
        // for (Post post : postsFoundedByHashtagInText) {
        // System.out.println(post);
        // }
        // } catch(NotFoundException err){
        // System.out.println("No posts founded by hashtag in text");
        // }
        // implementar nas opções de menu

        try {
            List<Post> postsFoundedByPhrase = socialNetwork.findPostByPhrase(searchTerm);
            System.out.println("===== Founded by phrase: =====");
            for (Post post : postsFoundedByPhrase) {
                System.out.println(post);
            }
        } catch (NotFoundException err) {
            System.out.println("No posts founded by phrase");
        }
    }

    public void run() {
        Integer chosen;
        while (true) {
            showMenu(options);
            // Controla a opção escolhida atual: entrada de dados do programa
            try {
                chosen = IOUtils.getInt("Enter a option: \n> ");

                // verifica se é maior ou menor que o número de conteúdos da lista, senão for,
                // continua...
                if (chosen > options.length || chosen < 0) {
                    System.out.println("Please, digit a valid option number!");
                    continue;
                }
                if (chosen == 0) { // Opção sair: termina o loop
                    break;
                }
                // Escolhe a opção pelo que foi digitado - 1 (o indice real do array)
                options[chosen - 1].callback.accept(null);
            } catch (NumberFormatException e) {
                System.out.println("Enter only numbers, please!");
            }

            IOUtils.clearScreen();

        }

        IOUtils.closeScanner(); // TODO: Achar um método mais "bonito" de fazer isso"

    }

}
