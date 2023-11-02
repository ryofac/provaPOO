package Models;

import java.time.LocalDateTime;

public class Post {
    private Integer _id;
    private String _text;
    private Integer _likes = 0;
    private Integer _dislikes = 0;
    private LocalDateTime _createdTime;
    private Profile _owner;

    public Post(Integer _id, String _text, Profile _owner) {
        this._id = _id;
        this._text = _text;
        this._owner = _owner;
        this._createdTime = LocalDateTime.now();
    }

    public Boolean isPopular() {
        // Returns true if the like amount is greatear than 50 % of dislikes
        return this._likes > (50 / 100 * this._dislikes);
    }

    public void like() {
        this._likes++;
    }

    public void dislike() {
        this._dislikes++;
    }

    public Integer getId() {
        return this._id;
    }

    public String getText() {
        return this._text;
    }

    public void setText(String text) {
        this._text = text;
    }

    public Profile getOwner() {
        return this._owner;
    }

    public LocalDateTime getCreatedTime() {
        return this._createdTime;
    }

    @Override
    public String toString() {
        return String.format("%d : %s : %s : %s", this._id, this._createdTime.toString(), this._owner.getName(),
                this._likes);
    }

}