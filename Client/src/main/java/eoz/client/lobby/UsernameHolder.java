package eoz.client.lobby;

public class UsernameHolder {
    private static String username;

    public static void setUsername(String newUsername) {
        username = newUsername;
    }

    public static String getUsername() {
        return username;
    }
}
