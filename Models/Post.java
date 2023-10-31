package Models;

import java.time.LocalDateTime;

public class Post {
    private String _id;
    private String _text;
    private Integer _likes = 0;
    private Integer _dislikes = 0;
    private LocalDateTime _createdTime;
    private Profile _owner;

    public Post(String _id, String _text, Profile _owner) {
        this._id = _id;
        this._text = _text;
        this._owner = _owner;
        this._createdTime = LocalDateTime.now();
    }

    public Boolean isPopular() {
        // Returns true if the like amount is greatear than 50 % of dislikes
        return this._likes > (50 / 100 * this._dislikes);
    }

    public String getId() {
        return this._id;
    }

    public String getText() {
        return this._text;
    }

    public void setText(String text) {
        this._text = text;

    }

    public Integer getLikes() {
        return this._likes;
    }

    public Integer getDislikes() {
        return this._dislikes;
    }

    public void like() {
        this._likes++;
    }

    public void dislike() {
        this._dislikes++;
    }
    public void setOwner(Profile owner) {
        this._owner = owner;
    }
    public Profile getOwner() {
        return this._owner;
    }

}