//username by purdue email


public class Account {
    private String name;
    private String email;
    private String userName;
    private String password;


    public Account(String name, String email, String password) throws InvalidEmailException {
        try {
            this.userName = email.substring(0, email.indexOf("@purdue.edu"));
        } catch (IndexOutOfBoundsException e ) {
            throw new InvalidEmailException("Please use Purdue email: " + email);
        }

        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Account{name='" + name + "', email='" + email + "', userName='" +
                userName + "', password='" + password + "'}";
    }
}
