public class authLib implements authInterface {

	public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user; 
    }

	public void userLoginActions() {
        if (userService.isUserLoggedIn()) {
            
            
    }
    
}