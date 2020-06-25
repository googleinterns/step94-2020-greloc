public class AuthLanding implements AuthInterface {
	
    //Instance Variables
    private User user;
    // private UserService userService = UserServiceFactory.getUserService();

    //Constructor
    public AuthLanding (User user) {
        this.user = user;
        this.userService = userService;

    }

	private User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user; 
    }

	public void userLoginActions() {
        if (userService.isUserLoggedIn()) {
            if(user.getType() != null) {
                //send to dashboard page
            	String userEmail = userService.getCurrentUser().getEmail();
                String urlToRedirectToAfterUserLogsOut = "/";
      	        String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
            } else {
                String urlToRedirectToAfterUserLogsIn = "/";
      	        String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
            }
        } else {
            //prompt the login page
            String urlToRedirectToAfterUserLogsIn = "/";
      	    String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
        }
            
    }
    
}