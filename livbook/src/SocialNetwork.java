import Repositories.ProfileRepository;
import Utils.IOUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    public void loadData(){
        loadPostsfromFile(filepath);
        loadProfilesFromFile(filepath);
    }

    public Profile createProfile(String username, String email){
        // O id sempre vai ser o id do último da lista + 1
        if(profileRepository.getAllProfiles().isEmpty()){
            return new Profile(1, username, email);

        }
        Integer id = profileRepository.getAllProfiles().getLast().getId() + 1;
        return new Profile(id, username, email);
        
    }
    
    public Post createPost(String text, Profile owner){
        // O id sempre vai ser o id do último da lista + 1
        if(postRepository.getAllPosts().isEmpty()){
            return new Post(1, text, owner);
        }
        Integer id = postRepository.getAllPosts().getLast().getId() + 1;
        return new Post(id, text, owner);

    }

    public AdvancedPost createAdvancedPost(String text, Profile owner, Integer remainingViews){
        Integer id = postRepository.getPostAmount() + 1;
        return new AdvancedPost(id, text, owner, remainingViews);

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
        if (postsFounded.size() == 0) {
            throw new NotFoundException("Posts with this hashtag does not exist");
        }
        return postsFounded;

    }

    public Post findPostsbyId(Integer id) throws NotFoundException {
        Optional<Post> postFounded = postRepository.findPostById(id);
        if (postFounded.isEmpty()) {
            throw new NotFoundException("This post does not exist");
        }
        return postFounded.get();

    }

    public List<Post> findPostByHashtag(String hashtag) throws NotFoundException {
        List<Post> postFounded = postRepository.findPostByHashtag(hashtag);
        if (postFounded.isEmpty()) {
            throw new NotFoundException("This post does not exist");
        }
        return postFounded;
    }

    public void like(Integer idPost) throws NotFoundException {
        try {
            Post founded = this.findPostsbyId(idPost);
            founded.like();
        } catch (NotFoundException e) {
            throw e;
        }

    }

    public void dislike(Integer idPost) throws NotFoundException {
        try {
            Post founded = this.findPostsbyId(idPost);
            founded.dislike();
        } catch (NotFoundException e) {
            throw e;
        }
    }

    // Usado para formatar os posts no formato adequado
    public String formatPost(Post post) {
        String formated = String.format("""
            -----------------------------
            <ID - %d> %s - at %s
            %s 
            ------------------------------
            %d - likes %d - dislikes
            """, 
            post.getId(), post.getOwner().getName(), post.getCreatedTime().format(DateTimeFormatter.ofPattern("dd/MM (EE): HH:mm")),
            post.getText(), post.getLikes(), post.getDislikes());

        if(post instanceof AdvancedPost){
            formated += String.format("(%d - views remaining)\n hashtags:", ((AdvancedPost) post).getRemainingViews());
            for(String hashtag : ((AdvancedPost) post).getHashtags()){
                formated += " " + hashtag;
            }   
        }
        formated += "\n"; // mais espaço no fim
        return formated;
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
        System.out.println("==== Founded by Profile: ====");
        for (Post actualPost : postsFounded) {
            System.out.println(formatPost(actualPost));
        }
    }

    public void showPostsPerHashtag(String hashtag) throws NotFoundException{
        List<Post> postsFounded = postRepository.findPostByHashtag(hashtag);
        if(postsFounded.size() == 0){
            throw new NotFoundException("Posts with this hashtag does not exist");
        }
        System.out.println("==== Founded by hashtag: ====");
        for(Post post: postsFounded){
            System.out.println(formatPost(post));
        }
    }

    public void showPostsPerText(String text) throws NotFoundException {
        List<Post> postsFoundedByText = findPostByPhrase(text);
        System.out.println("===== Founded by text: =====");
        for (Post post : postsFoundedByText) {
            System.out.println(formatPost(post));
        }
    }

    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();

    }

    public void includePost(Post post) {
        postRepository.includePost(post);
    }

    public void showAllPosts(){
        viewPosts();
        for(Post post: postRepository.getAllPosts()){
            System.out.println(formatPost(post));
        }
        postRepository.removeSeenPosts();
    }

    public List<Post> findPostByProfile(String searchTerm) throws NotFoundException {
        List<Post> postsFounded = postRepository.findPostByProfile(searchTerm);
        if (postsFounded.size() == 0) {
            throw new NotFoundException("Posts with this profile does not exist");
        }
        return postsFounded;
    }

    List<Post> findPostByPhrase(String searchTerm) throws NotFoundException {
        List<Post> postsFounded = postRepository.findPostByPhrase(searchTerm);
        if(postsFounded.size() == 0){
            throw new NotFoundException("Posts with this word in text does not exist");
        }
        return postsFounded;
    }

    public void likePost(Integer idPost) throws NotFoundException {
        Post post = findPostsbyId(idPost);
        post.like();
        postRepository.updatePost(post);
    }
    public void dislikePost(Integer idPost) throws NotFoundException {
        Post post = findPostsbyId(idPost);
        post.dislike();
        postRepository.updatePost(post);
    }

    public void showPopularPosts(){
        for(Post post: postRepository.getAllPosts()){
            if(post.isPopular()){
                System.out.println(formatPost(post));
            }
        }
    }

    public void showPopularHashtags(){
        List<String> hashtags = postRepository.getHashtags();
        for(String hashtag: hashtags){
            System.out.println(hashtag);
        }
    }

    public void viewPosts(){
        for(Post post: getAllPosts()){
            if(post instanceof AdvancedPost){
                ((AdvancedPost) post).decrementViews();
            }
        }

    }


    // Como vem os dados :
    // Em caso de Post = TIPO;id;text;ownerId;createdDatatime
    // Em caso de AdvancedPost = TIPO;id;text;owner;Id;createdDataTime;remainingViews;hashtags[hash1-hash2...]
    public void loadPostsfromFile(String filepath){
        List<String> lines = IOUtils.readLinesOnFile(filepath);
        Stream<String> linesStream = lines.stream();
        linesStream.forEach(line -> {
            String[] data = line.split(";");
            try{
                switch (data[0]) {
                    case "P":
                        // incluindo o post segundo os dados do arquivo
                        includePost(
                            new Post(Integer.parseInt(data[1]), data[2], 
                                findProfileById(Integer.parseInt(data[3])))
                            );
                        break;
                
                    case "AP":
                        // Criando o post a ser adicionado
                        AdvancedPost toBeAdded = new AdvancedPost(Integer.parseInt(data[1]), data[2], 
                            findProfileById(Integer.parseInt(data[3])), Integer.parseInt(data[4]));
                        
                        // Pegando só as hashtags do arquivo
                        String[] hashtags = data[5].split("-");

                        // Adcionando as hashtags do arquivo ao perfil
                        for(String hashtag : hashtags){
                            toBeAdded.addHashtag(hashtag);
                        }
                        includePost(toBeAdded);
                        break;
                }
            }
            catch (NotFoundException e){
                System.out.println("ERROR: user founded in file not related to any post");
                e.printStackTrace();
            }
        });
    }

    // Como vem os dados: id;name;email
    public void loadProfilesFromFile(String filepath){
        List<String> lines = IOUtils.readLinesOnFile(filepath);
        Stream<String> linesStream = lines.stream();
        linesStream.forEach(line -> {
            String[] data = line.split(";");
            try{
                includeProfile(new Profile(Integer.parseInt(data[0]), data[1], data[2]));
                
            }catch(AlreadyExistsException e){
                System.err.println("ERROR: Conflict with existing user in memory and in file");
                e.printStackTrace();
            }
        });

    }

}

