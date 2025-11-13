import java.util.*;
import java.util.stream.Collectors;

public class DonorManager {
    private List<Donor> donors;

    public DonorManager() {
        donors = new ArrayList<>();
    }

    
    public List<Donor> getDonorList() {
        return Collections.unmodifiableList(donors);
    }


    public boolean addDonor(Donor donor) {
        if (donor == null) return false;

        for (Donor d : donors) {
            if (d.getId().equals(donor.getId())) {
                return false;
            }
        }

        donors.add(donor);
        return true;
    }


    public boolean removeDonor(Donor donor) {
        return donors.remove(donor);
    }


    public boolean removeDonorById(UUID id) {
        Donor donor = findDonorById(id);
        if (donor == null) return false;
        return donors.remove(donor);
    }


    public Donor findDonorById(UUID id) {
        for (Donor donor : donors) {
            if (donor.getId().equals(id)) {
                return donor;
            }
        }
        return null;
    }

    public List<Donor> searchDonors(String bloodType, String city, Boolean eligible) {
        return donors.stream()
                .filter(d -> (bloodType == null || d.getBloodType().equalsIgnoreCase(bloodType)))
                .filter(d -> (city == null || d.getCity().equalsIgnoreCase(city)))
                .filter(d -> (eligible == null || d.isEligible() == eligible))
                .collect(Collectors.toList());
    }
}
