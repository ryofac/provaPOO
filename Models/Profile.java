package Models;

public class Profile {
    private Integer id;
    private String name;
    private String email;

    public Profile(Integer _id, String _name, String _email) {
        this.id = _id;
        this.name = _name;
        this.email = _email;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}