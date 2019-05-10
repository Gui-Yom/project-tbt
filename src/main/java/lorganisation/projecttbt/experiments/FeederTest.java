package lorganisation.projecttbt.experiments;

import lorganisation.projecttbt.utils.RandomStream;

import java.awt.Color;

public class FeederTest {

    public static void main(String[] args) {

        RandomStream<Color> randomStream = new RandomStream<>(() -> new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));

        for (int i = 0; i < 10; i++) {

            System.out.println(randomStream.next());
        }
    }
}
