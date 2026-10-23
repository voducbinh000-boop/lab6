package demoqa.utils;

public record StudentData(String firstName, String lastName, String email, String gender,
                          String mobile, String day, String month, String year,
                          String subject, String hobby, String address,
                          String state, String city) {
    public String fullName() {
        return firstName + " " + lastName;
    }
}
