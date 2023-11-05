package Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import Models.AdvancedPost;
import Models.Post;
import Models.Profile;

/*
 * Stores and handle the posts data
 */

 // @TODO: Descobrir porque as Streams tão fechando e bugando as pesquisas por Posts
public class PostRepository {
    private List<Post> posts = new ArrayList<Post>();
    private Stream<Post> postsStream = posts.stream();
    
    public List<Post> getAllPosts(){
        return posts;
    }

    public Optional<Post> findPostById(Integer id) {
        Stream<Post> postsFinded = postsStream.filter(post -> post.getId() == id);
        return postsFinded.findFirst();
    }

    public List<Post> findPostByText(String text) {
        List<Post> founded = new ArrayList<>();
        for(Post post : posts){
            if(post.getText().contains(text)){
                founded.add(post);
            }
        }
        return founded;
    }

    public List<Post> findPostByOwner(Profile owner) {
        Stream<Post> postsFinded = postsStream.filter(post -> post.getOwner().equals(owner));
        return postsFinded.collect(Collectors.toList());
    }

    public List<Post> findPostByHashtag(String hashtag) {
        Stream<Post> postsFinded = postsStream.filter(post -> {
            if (post instanceof AdvancedPost) {
                return ((AdvancedPost) post).hasHashtag(hashtag);
            }
            return false;
        });
        return postsFinded.collect(Collectors.toList());

    }

    public void includePost(Post post) {
        posts.add(post);
    }

    public List<Post> findPostByProfile(String searchTerm) {
        Stream<Post> postsFinded = postsStream.filter(post -> post.getOwner().getName().equals(searchTerm));
        return postsFinded.collect(Collectors.toList());

        
    }

    public List<Post> findPostByHashtagInText(String searchTerm) {
        Stream<Post> postsFinded = postsStream.filter(post -> post.getText().contains(searchTerm));
        return postsFinded.collect(Collectors.toList());
    }

}
