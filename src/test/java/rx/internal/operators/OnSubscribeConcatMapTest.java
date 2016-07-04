package rx.internal.operators;

import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

public class OnSubscribeConcatMapTest {

    @Test
    public void producerArbiterWithFewOnSubscribe() throws InterruptedException {
        TestSubscriber<Object> testSubscriber = TestSubscriber.create();

        Observable.just(new Object())
                .startWith(Observable.create(subscriber -> {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(new Object());
                        subscriber.onCompleted();
                    }
                }))
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())   // duplicate
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
    }
}
