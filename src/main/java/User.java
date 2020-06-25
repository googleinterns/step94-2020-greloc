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
    public getId() {
    	return id; 
    }

    public setId(String newId) {
        id = newId; 
    }

    public getType() {
        return type;
    }

    public setType(int newType) {
        type = newType; 
    }

    public getEmail() {
        return email;
    }

    public setEmail(String newEmail) {
        email = newEmail;

    }
}