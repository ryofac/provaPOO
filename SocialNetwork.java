import Repositories.ProfileRepository;
import Models.Profile;
import Repositories.PostRepository;

public class SocialNetwork {
    private ProfileRepository profileRepository;
    private PostRepository postRepository;

    public SocialNetwork() {
        this.profileRepository = new ProfileRepository();
        this.postRepository = new PostRepository();
    }

    public ProfileRepository getProfileRepository() {
        return this.profileRepository;
    }

    public PostRepository getPostRepository() {
        return this.postRepository;
    }

    public void setProfileRepository(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    public void includeProfile(Profile profile) {
        if (profile.getId() == null || profile.getName() == null || profile.getEmail() == null) {
            throw new RuntimeException("All fields must be filled");
        }
        if (profileRepository.findProfileById(profile.getId()) != null) {
            throw new RuntimeException("Profile with this id already exists");
        }
        if (profileRepository.findProfileByName(profile.getName()) != null) {
            throw new RuntimeException("Profile with this name already exists");
        }
        if (profileRepository.findProfileByEmail(profile.getEmail()) != null) {
            throw new RuntimeException("Profile with this email already exists");
        }
        profileRepository.includeProfile(profile);
    }

    public Profile findProfileById(Integer id) {
        return profileRepository.findProfileById(id);
    }
    
}
