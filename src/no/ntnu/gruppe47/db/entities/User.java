package no.ntnu.gruppe47.db.entities;


public class User {

    private String name;
    private String username;
    private String password;
    private final int userId;
    private String email;

	public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasword() {
        return password;
    }

    /**
     * Creates a new user, setting all information.
     *
     * @param name     The full name of the user
     * @param username The selected username, used for login
     * @param pasword  The selected password, used for login
     * @param userId   The assigned userId, used in the database
     * @param email    The users email address
     * @param phone    The users phone number
     */
    public User(int userId, String username, String pasword, String name, 
                String email) {
        this.name = name;
        this.username = username;
        this.password = pasword;
        this.userId = userId;
        this.email = email;
    }

    public String toString() {
        return "User: " + userId + " - " + username + " - " + password + " - " + name + " - " + email;
    }

	public void setName(String newName) {
		this.name = newName;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof User))
			return false;
		
		User other = (User) o;
		
		if (other.getUserId() != this.getUserId())
			return false;
		if (other.getUsername() != this.getUsername())
			return false;
		if (other.getPasword() != this.getPasword())
			return false;
		if (other.getName() != this.getName())
			return false;
		if (other.getEmail() != this.getEmail())
			return false;
		
		return true;
	}


}
