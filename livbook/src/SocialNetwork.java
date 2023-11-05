import Repositories.ProfileRepository;

import java.util.List;
import java.util.Optional;
import Exceptions.AlreadyExistsException;
import Exceptions.NotFoundException;
import Models.AdvancedPost;
import Models.Post;
import Models.Profile;
import Repositories.PostRepository;

public class SocialNetwork {
    private ProfileRepository profileRepository;
    private PostRepository postRepository;
  
    public SocialNetwork(ProfileRepository profileRepository, PostRepository postRepository) {
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
    }

    public void includeProfile(Profile profile) throws AlreadyExistsException {

        Optional<Profile> foundedById = profileRepository.findProfileById(profile.getId());
        Optional<Profile> foundedByEmail = profileRepository.findProfileByEmail(profile.getEmail());
        Optional<Profile> foundedByName = profileRepository.findProfileByName(profile.getName());

        if (foundedById.isPresent()) {
            throw new AlreadyExistsException("Profile with this id already exists");
        }
        if (foundedByName.isPresent()) {
            throw new AlreadyExistsException("Profile with this name already exists");
        }
        if (foundedByEmail.isPresent()) {
            throw new AlreadyExistsException("Profile with this email already exists");

        }
        profileRepository.addProfile(profile);
    }

    public Profile findProfileById(Integer id) throws NotFoundException {
        var founded = profileRepository.findProfileById(id);
        if (founded.isPresent()) {
            return founded.get();
        }
        throw new NotFoundException("Profile with this id not founded!");
    }

    public Profile findProfileByEmail(String email) throws NotFoundException {
        var founded = profileRepository.findProfileByEmail(email);
        if (founded.isPresent()) {
            return founded.get();
        }
        throw new NotFoundException("Profile with this email not founded!");
    }

    public Profile findProfileByName(String name) throws NotFoundException {
        var founded = profileRepository.findProfileByName(name);
        if (founded.isPresent()) {
            return founded.get();
        }
        throw new NotFoundException("Profile with this name not founded!");
    }

    public List<Post> findPostsbyOwner(Profile owner) throws NotFoundException {
      List<Post> postsFounded = postRepository.findPostByOwner(owner);
        if(postsFounded.size() == 0){
            throw new NotFoundException("Posts with this hashtag does not exist");
        }
        return postsFounded;

    }

    public Post findPostsbyId(Integer id) throws NotFoundException {
        Optional<Post> postFounded = postRepository.findPostById(id);
        if(postFounded.isEmpty()){
            throw new NotFoundException("This post does not exist");
        }
        return postFounded.get();

    }

    public List<Post> findPostByHashtag(String hashtag) throws NotFoundException{
        List<Post> postFounded = postRepository.findPostByHashtag(hashtag);
        if(postFounded.isEmpty()){
            throw new NotFoundException("This post does not exist");
        }
        return postFounded;
    }

    public void like(Integer idPost) throws NotFoundException {
        try{
            Post founded = this.findPostsbyId(idPost);
            founded.like();
        } catch (NotFoundException e){
            throw e;
        }
       

    }

    public void dislike(Integer idPost) throws NotFoundException {
        try{
            Post founded = this.findPostsbyId(idPost);
            founded.dislike();
        } catch (NotFoundException e){
            throw e;
        }

    }

    public void decrementViews(Integer idPost) throws NotFoundException {
        Optional<Post> founded = postRepository.findPostById(idPost);
        if (founded.isEmpty()) {
            throw new NotFoundException("This post doesn't exist!");
        }
        Post post = founded.get();
        if (!(post instanceof AdvancedPost)) {
            System.out.println("Post não é uma instância de ADVANCED POST");
            return;
        }
        post.dislike();

    }

    public void showPostsPerProfile(Profile owner) throws NotFoundException {
        List<Post> postsFounded = postRepository.findPostByOwner(owner);
        if (postsFounded.size() == 0) {
            throw new NotFoundException("Posts with this owner does not exist");
        }
        for (Post actualPost : postsFounded) {
            System.out.println(actualPost.toString());
        }
    }

    public void showPostsPerHashtag(String hashtag) throws NotFoundException {
        List<Post> postsFounded = postRepository.findPostByHashtag(hashtag);
        if(postsFounded.size() == 0){
            throw new NotFoundException("Posts with this hashtag does not exist");
        }
        for(Post post: postsFounded){
            System.out.println(post.toString());
        }

    }
    public List<Post> getAllPosts(){
        return postRepository.getAllPosts();

    }

    public List<Post> findPostByText(String text) throws NotFoundException{
        List<Post> postsFounded = postRepository.findPostByText(text);
        if(postsFounded.size() == 0){
            throw new NotFoundException("Posts with this text does not exist");
        }
        return postsFounded;
    }

    public void includePost(Post post){
        postRepository.includePost(post);
    }

    public void showAllPosts(){
        for(Post post: postRepository.getAllPosts()){
            System.out.println(post);
        }
    }

    public List<Post> findPostByProfile(String searchTerm) throws NotFoundException {
        List<Post> postsFounded = postRepository.findPostByProfile(searchTerm);
        if(postsFounded.size() == 0){
            throw new NotFoundException("Posts with this profile does not exist");
        }
        return postsFounded;
    }

    public List<Post> findPostByHashtagInText(String searchTerm) throws NotFoundException {
        List<Post> postsFounded = postRepository.findPostByHashtagInText(searchTerm);
        if(postsFounded.size() == 0){
            throw new NotFoundException("Posts with this hashtag in text does not exist");
        }
        return postsFounded;
    }

    public List<Post> findPostByPhrase(String searchTerm) throws NotFoundException {
        return null;
    }

}
