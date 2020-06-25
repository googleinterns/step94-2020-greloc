public class User {

    //Instance Variables
    String id;
    int type; 
    String email; 

    // Constructor
    public User(String id, int type, String email) {
    this.id = id;
    this.type = type;
    this.email = email;
    }
    
    // Getters and Setters
    public String getId() {
    	return id; 
    }

    public void setId(String newId) {
        id = newId; 
    }

    public int getType() {
        return type;
    }

    public void setType(int newType) {
        type = newType; 
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        email = newEmail;

    }
}