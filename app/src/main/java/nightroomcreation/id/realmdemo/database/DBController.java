package nightroomcreation.id.realmdemo.database;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import nightroomcreation.id.realmdemo.model.Book;

/**
 * Created by iand on 25/09/17.
 */

public class DBController {

    private static DBController instance;
    private final Realm realm;

    public DBController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static DBController with(Fragment fragment) {
        if (instance == null) {
            instance = new DBController(fragment.getActivity().getApplication());
        }

        return instance;
    }

    public static DBController with(Activity activity) {
        if (instance == null) {
            instance = new DBController(activity.getApplication());
        }

        return instance;
    }

    public static DBController with(Application application) {
        if (instance == null) {
            instance = new DBController(application);
        }

        return instance;
    }

    public static DBController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //refreshing realm instance
    public void refresh() {
        realm.refresh();
    }

    //clear all object from book class model
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(Book.class);
        realm.commitTransaction();
    }

    //find all object from model `Book.class`
    public RealmResults<Book> getBooks() {
        return realm.where(Book.class).findAll();
    }

    //get single item of model `Book.class`
    public Book getBook(String id) {
        return realm.where(Book.class).equalTo("id", id).findFirst();
    }

    //checking item of model `Book.class` is empty
    public boolean isBookEmpty() {
        return !realm.allObjects(Book.class).isEmpty();
    }

    //query example
    public RealmResults<Book> queryBooks() {
        return realm.where(Book.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();
    }
}
