package Repositories;

import java.util.ArrayList;

import Models.Profile;

public class ProfileRepository {
    private ArrayList<Profile> profiles = new ArrayList<Profile>();

    public Profile findProfileById(String id) {
        for (Profile profile : profiles) {
            if (profile.getId().equals(id)) {
                return profile;
            }
        }
        return null;
    }
    public Profile findProfileByName(String name) {
        for (Profile profile : profiles) {  
            if (profile.getName().equals(name)) {
                return profile;
            }
        }
        return null;
    }
    public Profile findProfileByEmail(String email) {
        for (Profile profile : profiles) {
            if (profile.getEmail().equals(email)) {
                return profile;
            }
        }
        return null;
    }

    public void includeProfile(Profile profile) {
        profiles.add(profile);
    }

    
    
}
