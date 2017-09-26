package nightroomcreation.id.realmdemo.activity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import nightroomcreation.id.realmdemo.R;
import nightroomcreation.id.realmdemo.adapter.BooksAdapter;
import nightroomcreation.id.realmdemo.adapter.RealmBooksAdapter;
import nightroomcreation.id.realmdemo.app.GlobalFunction;
import nightroomcreation.id.realmdemo.database.DBController;
import nightroomcreation.id.realmdemo.model.Book;

public class MainActivity extends AppCompatActivity {

    //view
    private FloatingActionButton btnFab;
    private RecyclerView recycler;
    private BooksAdapter adapter;
    private LayoutInflater layoutInflater;

    //database
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initConfig();

        /**
         * action button here
         * ==================
         */
        //action add item card
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //inflating layout
                layoutInflater = MainActivity.this.getLayoutInflater();
                View container = layoutInflater.inflate(R.layout.item_edit, null);
                final EditText editTitle = (EditText) container.findViewById(R.id.edit_title);
                final EditText editAuthor = (EditText) container.findViewById(R.id.edit_author);
                final EditText editThumbnail = (EditText) container.findViewById(R.id.edit_thumbnail);

                //creating the dialog for layout add item
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(container)
                        .setTitle("Add book")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //create a single item and object
                                Book book = new Book();
                                book.setId(DBController.getInstance().getBooks().size() + System.currentTimeMillis());
                                book.setTitle(editTitle.getText().toString());
                                ;
                                book.setAuthor(editAuthor.getText().toString());
                                ;
                                book.setImageUrl(editThumbnail.getText().toString());

                                if (editTitle.getText() == null
                                        || editTitle.getText().toString().equals("")
                                        || editTitle.getText().toString().equals(" ")) {

                                    Snackbar.make(view, "Entry not saved, missing title", Snackbar.LENGTH_LONG).show();
                                } else {
                                    //begin transaction with DB
                                    realm.beginTransaction();
                                    realm.copyToRealm(book);
                                    realm.commitTransaction();

                                    //telling adapter to reflecting data automatically
                                    adapter.notifyDataSetChanged();
                                    //scrolling adapter position to new item were added
                                    recycler.scrollToPosition(DBController.getInstance().getBooks().size() - 1);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * CUSTOM METHOD HERE
     * ==================
     */
    private void initConfig() {
        //get realm `instance`
        this.realm = DBController.with(this).getRealm();

        //setup recycler adapter
        // use this setting to improve performance if you know that changes
        recycler.setHasFixedSize(true);
        //set linear layout of adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        //create empty data adapter first for preparation
        adapter = new BooksAdapter(this);
        recycler.setAdapter(adapter);

        //set preload dta into prefs
        if (!GlobalFunction.prefWith(this).getPreloadPref()) {
            setDummyData();
        }

        //refreshing the instance of realm
        DBController.with(this).refresh();

        //get all persisted data on db
        //create the helper adapter & notify data set changed
        //changes gonna be reflected automatically
        setDBAdapter(DBController.with(this).getBooks());

        Toast.makeText(this, "Press card item for edit, long press to remove it", Toast.LENGTH_LONG).show();
    }

    private void initLayout() {
        btnFab = (FloatingActionButton) findViewById(R.id.btn_fab);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);*/
    }

    private void setDBAdapter(RealmResults<Book> books) {
        RealmBooksAdapter dbAdapter = new RealmBooksAdapter(this.getApplicationContext(), books, true);
        //set the data and tell the recycler view to draw
        adapter.setRealmAdapter(dbAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setDummyData() {
        ArrayList<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setId(1 + System.currentTimeMillis());
        book.setAuthor("Reto Meier");
        book.setTitle("Android 4 Application Development");
        book.setImageUrl("https://api.androidhive.info/images/realm/1.png");
        books.add(book);

        book = new Book();
        book.setId(2 + System.currentTimeMillis());
        book.setAuthor("Itzik Ben-Gan");
        book.setTitle("Microsoft SQL Server 2012 T-SQL Fundamentals");
        book.setImageUrl("https://api.androidhive.info/images/realm/2.png");
        books.add(book);

        book = new Book();
        book.setId(3 + System.currentTimeMillis());
        book.setAuthor("Magnus Lie Hetland");
        book.setTitle("Beginning Python: From Novice To Professional Paperback");
        book.setImageUrl("https://api.androidhive.info/images/realm/3.png");
        books.add(book);

        book = new Book();
        book.setId(4 + System.currentTimeMillis());
        book.setAuthor("Chad Fowler");
        book.setTitle("The Passionate Programmer: Creating a Remarkable Career in Software Development");
        book.setImageUrl("https://api.androidhive.info/images/realm/4.png");
        books.add(book);

        book = new Book();
        book.setId(5 + System.currentTimeMillis());
        book.setAuthor("Yashavant Kanetkar");
        book.setTitle("Written Test Questions In C Programming");
        book.setImageUrl("https://api.androidhive.info/images/realm/5.png");
        books.add(book);

        //begin transaction with DB to store data
        for (Book b : books) {
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }

        //set pref data for preload
        GlobalFunction.prefWith(this).setPreloadPref(true);
    }
}
