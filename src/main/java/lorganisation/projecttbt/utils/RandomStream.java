package lorganisation.projecttbt.utils;

public class RandomStream<T> {

    private Feeder<T> feed;

    public RandomStream(Feeder<T> feed) {

        this.feed = feed;
    }

    public T next() {

        return feed.feed();
    }
}
