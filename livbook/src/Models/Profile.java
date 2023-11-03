package Models;

public class Profile {
    private static Integer counter = 0;
    private Integer _id;
    private String _name;
    private String _email;

    public Profile(String _name, String _email) {
        this._id = counter;
        this._name = _name;
        this._email = _email;
        counter++;
    }

    public Integer getId() {
        return this._id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getEmail() {
        return this._email;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    @Override
    public String toString() {
        return String.format("@%s : %s ", getName(), getEmail());
    }

}