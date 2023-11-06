package Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Models.Post;
import Models.Profile;
import Utils.IOUtils;

public class ProfileRepository {
    private List<Profile> profiles = new ArrayList<Profile>();

    public void writeProfilesinFile(String filepath) {
        StringBuilder str = new StringBuilder();
        for (Profile profile : profiles) {
            str.append(profile.toString().trim() + "\n");
        }
        IOUtils.writeOnFile(filepath, str.toString());
    }

    public List<Profile> getAllProfiles() {
        return profiles;
    }

    public Integer getProfileAmount() {
        return profiles.size();
    }

    public Optional<Profile> findProfileById(Integer id) {
        for (Profile profile : profiles) {
            if (profile.getId() == id) {
                return Optional.of(profile);
            }
        }
        return Optional.empty();
    }

    public Optional<Profile> findProfileByName(String name) {
        for (Profile profile : profiles) {
            if (profile.getName().equals(name)) {
                return Optional.of(profile);
            }
        }
        return Optional.empty();
    }

    public Optional<Profile> findProfileByEmail(String email) {
        for (Profile profile : profiles) {
            if (profile.getEmail().equals(email)) {
                return Optional.of(profile);
            }
        }
        return Optional.empty();
    }

    public Boolean addProfile(Profile profile) {
        Optional<Profile> equalProfile = findProfileById(profile.getId());
        if (equalProfile.isPresent()) {
            return false;
        }
        profiles.add(profile);
        return true;
    }

}
