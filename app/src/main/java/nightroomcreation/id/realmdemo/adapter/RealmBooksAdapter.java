package nightroomcreation.id.realmdemo.adapter;

import android.content.Context;

import io.realm.RealmResults;
import nightroomcreation.id.realmdemo.model.Book;

/**
 * Created by iand on 25/09/17.
 */

public class RealmBooksAdapter extends RealmModelAdapter<Book> {
    public RealmBooksAdapter(Context context, RealmResults<Book> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
