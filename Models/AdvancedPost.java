package Models;

import java.util.ArrayList;

public class AdvancedPost extends Post {
    ArrayList<String> hashtags = new ArrayList<String>();
    Double remainingViews;

    public AdvancedPost(Integer _id, String _text, Profile _owner, Double remainingViews) {
        super(_id, _text, _owner);
        this.remainingViews = remainingViews;

    }

    public ArrayList<String> getHashtags() {
        return this.hashtags;
    }

    public Double getRemainingViews() {
        return this.remainingViews;
    }

    public void addHashtag(String hashtag) {
        this.hashtags.add(hashtag);
    }

    public Boolean decrementViews() {
        if (this.remainingViews-- < 0) {
            return false;
        }
        remainingViews--;
        return true;
    }

    public Boolean hasHashtag(String hashtag) {
        for (String actualHashtag : hashtags) {
            if (hashtag.equals(actualHashtag)) {
                return true;
            }
        }
        return false;
    }
}