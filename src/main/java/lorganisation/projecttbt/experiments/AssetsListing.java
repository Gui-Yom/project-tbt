package lorganisation.projecttbt.experiments;

import lorganisation.projecttbt.AssetsManager;

public class AssetsListing {

    public static void main(String[] args) {

        try {

            //AssetsManager.listFiles("assets/characters", "*.json");
            AssetsManager.gameMaps().forEach((name, path) -> {
                System.out.println(name + ": " + path);
            });

            AssetsManager.gameCharacters().forEach((name, path) -> {
                System.out.println(name + ": " + path);
            });

            AssetsManager.botNames().forEach(name -> {
                System.out.println(name);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
