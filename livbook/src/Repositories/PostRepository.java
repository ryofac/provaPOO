package Repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Models.AdvancedPost;
import Models.Post;
import Models.Profile;
import Utils.IOUtils;

/*
 * Stores and handle the posts data
 */

public class PostRepository {
    private List<Post> posts = new ArrayList<Post>();

    public void writePostsinFile(String filepath) {
        StringBuilder str = new StringBuilder();
        for (Post post : posts) {
            str.append(post.toString().trim() + "\n");
        }
        IOUtils.writeOnFile(filepath, str.toString());
    }

    public List<Post> getAllPosts() {
        return posts;
    }

    public Integer getPostAmount() {
        return posts.size();
    }

    public void removeSeenPosts() {
        posts.removeIf(post -> post instanceof AdvancedPost && !((AdvancedPost) post).canSee());

    }

    public Optional<Post> findPostById(Integer id) {
        Stream<Post> postsStream = posts.stream();
        Stream<Post> postsFinded = postsStream.filter(post -> post.getId() == id);
        return postsFinded.findFirst();
    }

    public List<Post> findPostByOwner(Profile owner) {
        Stream<Post> postsStream = posts.stream();
        Stream<Post> postsFinded = postsStream.filter(post -> post.getOwner().equals(owner));
        return postsFinded.collect(Collectors.toList());
    }

    public List<Post> findPostByHashtag(String hashtag) {
        Stream<Post> postsStream = posts.stream();
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
        Stream<Post> postsStream = posts.stream();
        Stream<Post> postsFinded = postsStream.filter(post -> post.getOwner().getName().equals(searchTerm));
        return postsFinded.collect(Collectors.toList());

    }

    public List<Post> findPostByPhrase(String searchTerm) {
        Stream<Post> postsStream = posts.stream();
        Stream<Post> postsFinded = postsStream.filter(post -> post.getText().contains(searchTerm));
        return postsFinded.collect(Collectors.toList());
    }

    public void updatePost(Post post) {
        Optional<Post> founded = findPostById(post.getId());
        if (founded.isEmpty()) {
            return;
        }
        Post postFounded = founded.get();
        postFounded.setText(post.getText());
    }

    public List<String> getHashtags() {
        return null;
    }

    public void deletePost(Post post) {
        posts.remove(post);
    }

}
