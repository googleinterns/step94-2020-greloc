interface authInterface {
    final User user;
    final UserService userService = UserServiceFactory.getUserService();
    
    User getUser();
    User setUser();
    void userLoginActions();

}

