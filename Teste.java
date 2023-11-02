import Exceptions.AlreadyExistsException;
import Exceptions.NotFoundException;
import Models.Profile;
import Repositories.PostRepository;
import Repositories.ProfileRepository;

class Teste {
    public static void main(String[] args) throws AlreadyExistsException {
        PostRepository postRepository = new PostRepository();
        ProfileRepository profileRepository = new ProfileRepository();
        var facebook = new SocialNetwork(profileRepository, postRepository);

        facebook.includeProfile(new Profile(1, "Ryan", "ryan@gmail.com"));
        try {
            facebook.findProfileByEmail("ryan@gmail.com");
            facebook.findPostsbyOwner(new Profile(2, "Ryan", "ryan@gmail.com"));

        } catch (NotFoundException ex) {

            ex.printStackTrace();

        } finally {
            System.out.println("Fim do programa");
        }

    }

}