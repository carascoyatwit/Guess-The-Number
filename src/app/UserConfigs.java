package app;

public class UserConfigs {

    private static String clientName = "Player";
    private static int setClients = 4;
    private static int clients = 0;
    private static String serverHost = "localhost";
    private static int serverPort = 8080;
    
    public static String getClientName() {
        return clientName;
    }

    public static void setClientName(String name) {
        clientName = name;
    }

    public static int getSetClients() {
        return setClients;
    }

    public static void setSetClients(int maxClients) {
        setClients = maxClients;
    }

    public static int getClients() {
        return clients;
    }

    public static void setClients(int currentClients) {
        clients = currentClients;
    }

    public static void incrementClients() {
        clients++;
    }

    public static void decrementClients() {
        clients--;
    }

    public static String getServerHost() {
        return serverHost;
    }

    public static void setServerHost(String host) {
        serverHost = host;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int port) {
        serverPort = port;
    }

}
