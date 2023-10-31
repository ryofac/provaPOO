package Repositories;

import java.util.ArrayList;
import Models.Post;

public class PostRepository {
    private ArrayList<Post> posts = new ArrayList<Post>();

    public Post findPostById(String id) {
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                return post;
            }
        }
        return null;
    }
    public Post findPostByText(String text) {
        for (Post post : posts) {  
            if (post.getText().equals(text)) {
                return post;
            }
        }
        return null;
    }
    
    //retornar vários owners 
    public Post findPostByOwner(String owner) {
        for (Post post : posts) {
            if (post.getOwner().equals(owner)) {
                return post;
            }
        }
        return null;
    }

    public void includePost(Post post) {
        posts.add(post);
    }
    
    
}
