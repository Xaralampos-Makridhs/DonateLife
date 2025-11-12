import java.util.*;
import java.time.*;

public class Donor {

    private UUID id;
    private String firstName;
    private String lastName;
    private String bloodType;
    private String email;
    private String phone;
    private String city;
    private LocalDateTime lastDonationDate;
    private boolean active;

    // Constructor
    public Donor(UUID id, String firstName, String lastName, String bloodType, String email, String phone, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bloodType = bloodType;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.lastDonationDate = null;
        this.active = true;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public LocalDateTime getLastDonationDate() {
        return lastDonationDate;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Validation method
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Last name is required");
        }

        Set<String> validBloodTypes = Set.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        if (bloodType == null || !validBloodTypes.contains(bloodType.toUpperCase())) {
            errors.add("Blood type is required and must be valid");
        }

        if (email != null && !email.trim().isEmpty()) {
            if (!email.contains("@") || !email.contains(".")) {
                errors.add("Enter a valid email address");
            }
        }

        if (phone == null || phone.trim().isEmpty()) {
            errors.add("Phone number is required");
        } else if (!phone.matches("\\d{10}")) {
            errors.add("Enter a valid 10-digit phone number");
        }

        if (city == null || city.trim().isEmpty()) {
            errors.add("City is required");
        }

        if (lastDonationDate != null && lastDonationDate.isAfter(LocalDateTime.now())) {
            errors.add("Enter a valid last donation date");
        }

        return errors;
    }

    // Eligibility check
    public boolean isEligible() {
        if (!this.isActive()) {
            return false;
        }
        if (lastDonationDate == null) {
            return true;
        }

        long daysBetween = Duration.between(lastDonationDate, LocalDateTime.now()).toDays();
        return daysBetween > 90;
    }

    // Days since last donation
    public long daysSinceLastDonation() {
        if (lastDonationDate == null) {
            return Long.MAX_VALUE;
        }
        return Duration.between(lastDonationDate, LocalDateTime.now()).toDays();
    }

    // Update last donation date
    public boolean updateLastDonationDate(LocalDateTime date) {
        if (!this.isActive()) {
            return false;
        }
        if (date == null || date.isAfter(LocalDateTime.now())) {
            return false;
        }

        this.lastDonationDate = date;
        return true;
    }

    // toString override
    @Override
    public String toString() {
        return String.format(
                "Donor[ID=%s, Name=%s %s, BloodType=%s, Email=%s, Phone=%s, City=%s, LastDonationDate=%s, Active=%s, Eligible=%s]",
                id,
                firstName,
                lastName,
                bloodType,
                email != null ? email : "N/A",
                phone != null ? phone : "N/A",
                city,
                lastDonationDate != null ? lastDonationDate.toString() : "Never",
                active,
                isEligible()
        );
    }
}
