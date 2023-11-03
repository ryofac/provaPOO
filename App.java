import java.util.Scanner;

public class App {
    private SocialNetwork socialNetwork;
    Scanner sc;

    public App(SocialNetwork socialNetwork, Scanner sc) {
        this.socialNetwork = socialNetwork;
        this.sc = sc;
    }

    int out = 0;

    String menu() {
        return ("Choose a option:" +
        "\n====================================" +
        "1 - Include Profile\t 2 -Find Profile By Id\t 3 - Find Profile By Email\t 4 - Find Profile By Name");
        
    }
    
    
}
