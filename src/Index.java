import utils.UIComponents;

public class Index {
    public static void main(String[] args) {
        UIComponents.loadingBarWithColor("initializing app...", 15, 100, "RED");
        App.run();
    }
}