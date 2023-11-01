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
public class PostRepository {
    private List<Post> posts = new ArrayList<Post>();

    private Stream<Post> postsStream = posts.stream();

    public Optional<Post> findPostById(Integer id) {
        Stream<Post> postsFinded = postsStream.filter(post -> post.getId() == id);
        return postsFinded.findFirst();
    }

    public List<Post> findPostByText(String text) {
        Stream<Post> postsFinded = postsStream.filter(post -> post.getText().contains(text));
        return postsFinded.collect(Collectors.toList());
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

}
