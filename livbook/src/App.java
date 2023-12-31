import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import Exceptions.AlreadyExistsException;
import Exceptions.NotFoundException;
import Models.AdvancedPost;
import Models.Post;
import Models.Profile;
import Utils.ConsoleColors;
import Utils.IOUtils;

public class App {
    private SocialNetwork socialNetwork;

    public App(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    private final String MENU_TITLE = """
             __       __  ____    ____ .______     ______     ______    __  ___
            |  |     |  | \\   \\  /   / |   _  \\   /  __  \\   /  __  \\  |  |/  /
            |  |     |  |  \\   \\/   /  |  |_)  | |  |  |  | |  |  |  | |  '  /
            |  |     |  |   \\      /   |   _  <  |  |  |  | |  |  |  | |    <
            |  `----.|  |    \\    /    |  |_)  | |  `--'  | |  `--'  | |  .  \\
            |_______||__|     \\__/     |______/   \\______/   \\______/  |__|\\__\\

            """;

    private final String PROFILE_PATH = "/home/ryan-dev/Works/Faculdade/Second Period/ads-poo/prova/provaPOO/livbook/src/data/profiles.txt";
    private final String POST_PATH = "/home/ryan-dev/Works/Faculdade/Second Period/ads-poo/prova/provaPOO/livbook/src/data/posts.txt";

    // This class is used for menu options
    private class Option {
        String title; // Nome que será mostrado
        Consumer<Object> callback; // função que será chamada pela opcao

        Option(String title, Consumer<Object> callback) {
            this.title = title;
            this.callback = callback;
        }

        @Override
        public String toString() {
            return title;
        }

    }

    private Option[] options = {
            new Option("Create profile", none -> {
                includeProfile();
            }),
            new Option("Create Post", none -> {
                createPost();
            }),
            new Option("Show Feed", none -> {
                showAllPosts();
            }),
            new Option("Show users", none -> {
                socialNetwork.showAllProfiles();
            }),
            new Option("Search Profile", none -> {
                searchProfile();
            }),
            new Option("Search Post", none -> {
                searchPost();
            }),
            new Option("Like Post", none -> {
                likePost();
            }),
            new Option("Dislike Post", none -> {
                dislikePost();
            }),
            new Option("Delete Post", none -> {
                deletePost();
            }),
            new Option("Delete Profile", none -> {
                removeProfile();
            }),
            new Option("Show Popular Advanced Posts", none -> {
                showPopularAPosts();
            }),

    };

    private void showMenu(Option... options) {
        String title = MENU_TITLE;
        Integer optionNumber = 0;
        System.out.println(title);
        for (Option option : options) {
            System.out.println(ConsoleColors.YELLOW_BRIGHT + "+> " + ConsoleColors.GREEN + ++optionNumber + "-" + option
                    + ConsoleColors.RESET);
        }
        System.out.println(String.format(ConsoleColors.RED_BRIGHT + "+> %d - %s", 0, "Exit" + ConsoleColors.RESET));
    }

    private void showPopularAPosts() {
        socialNetwork.showPopularAPosts();
    }

    private void deletePost() {
        Integer idPost = IOUtils.getInt("Enter the post id: ");
        try {
            socialNetwork.deletePost(idPost);
            System.out.println("Post deleted!");
        } catch (NotFoundException e) {
            System.out.println("Post not founded!");
        }
    }

    private void includeProfile() {
        try {
            String name = IOUtils.getTextNormalized("Enter the profile username: ");
            String email = IOUtils.getTextNormalized("Enter profile email: ");
            socialNetwork.includeProfile(socialNetwork.createProfile(name, email));
            System.out.println("User created!");
        } catch (AlreadyExistsException e) {
            System.out.println(ConsoleColors.RED + "CANNOT CREATE USER: " + e.getMessage() + ConsoleColors.RESET);
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
                System.out.println(ConsoleColors.RED + "User not founded!" + ConsoleColors.RESET);
            }

        }

    }

    private void removeProfile() {
        socialNetwork.showAllProfiles();
        System.out.println(
                ConsoleColors.RED + "The posts related to that person will be removed too!" + ConsoleColors.RESET);
        Integer id = IOUtils.getInt("Enter the id: ");
        socialNetwork.removeProfile(id);
        System.out.println("Profile removed!");
    }

    private void showAllPosts() {
        System.out.println("-=-=-=-=-=- FEED =-=-=-=-=-=-= ");
        socialNetwork.showAllPosts();
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
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
        String name = IOUtils.getTextNormalized("Enter your username: ");
        String email = IOUtils.getTextNormalized("Enter your email: ");
        try {
            Profile foundedByEmail = socialNetwork.findProfileByEmail(email);
            Profile foundedByName = socialNetwork.findProfileByName(name);
            if (foundedByEmail != foundedByName) {
                System.out.println(ConsoleColors.RED + "Autentication failed!" + ConsoleColors.RESET);
                return;
            }
            String text = IOUtils.getText("What do you want to share with world today? >_<\n > ").replace(";", "*");

            List<String> hashtagsFounded = findHashtagInText(text);
            if (hashtagsFounded.size() > 0)
                System.out.println(ConsoleColors.YELLOW
                        + "Warn: you can only embed hashtags in a advanced post" + ConsoleColors.RESET);
            Boolean isAdvanced = IOUtils.getChoice("Do you want to turn this into a advanced post? ");
            Post created;
            if (isAdvanced) {
                Integer remainingViews = IOUtils.getInt("Set the max views: ");
                created = socialNetwork.createAdvancedPost(text, foundedByEmail, remainingViews);
            } else {
                created = socialNetwork.createPost(text, foundedByEmail);

            }
            // hashtags vão ser adcionadas a medida que são encontradas no próprio texto
            for (String hashtag : hashtagsFounded) {
                if (created instanceof AdvancedPost) {
                    ((AdvancedPost) created).addHashtag(hashtag);
                } else {
                    text = text.replace(hashtag, " ");
                    System.out.println(ConsoleColors.RED + "Hashtag " + hashtag
                            + " removed: you have to create an advanced post" + ConsoleColors.RESET);
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
        String searchTerm = IOUtils
                .getTextNormalized("Enter the search parameter: [profile username/phrase/hashtag]\n > ");
        try {
            Profile userFoundedByName = socialNetwork.findProfileByName(searchTerm);
            socialNetwork.showPostsPerProfile(userFoundedByName);
        } catch (NotFoundException e) {
            System.out.println("No posts founded by user");
        }
        try {
            socialNetwork.showPostsPerText(searchTerm);
        } catch (NotFoundException e) {
            System.out.println("No posts founded by text");
        }
        try {
            socialNetwork.showPostsPerHashtag(searchTerm);
        } catch (NotFoundException err) {
            System.out.println("No posts founded by hashtag");
        }
    }

    private void likePost() {
        showAllPosts();
        Integer idPost = IOUtils.getInt("Enter the post id: ");
        try {
            socialNetwork.likePost(idPost);
            Post founded = socialNetwork.findPostsbyId(idPost);
            System.out.println("Post from " + founded.getOwner().getName() + "liked!");
        } catch (NotFoundException e) {
            System.out.println("Post not founded!");
        }
    }

    private void dislikePost() {
        Integer idPost = IOUtils.getInt("Enter the post id: ");
        try {
            socialNetwork.dislikePost(idPost);
            Post founded = socialNetwork.findPostsbyId(idPost);
            System.out.println("Post disliked!");
            System.out.println("Post from " + founded.getOwner().getName() + "disliked!");
        } catch (NotFoundException e) {
            System.out.println("Post not founded!");
        }
    }

    public void run() {
        Integer chosen;
        socialNetwork.readData(PROFILE_PATH, POST_PATH);
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
                socialNetwork.saveData(PROFILE_PATH, POST_PATH);
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED + "Enter only numbers, please!" + ConsoleColors.RESET);
            }

            IOUtils.clearScreen();

        }

        IOUtils.closeScanner(); // TODO: Achar um método mais "bonito" de fazer isso"

    }

}
