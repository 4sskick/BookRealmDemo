package nightroomcreation.id.realmdemo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmResults;
import nightroomcreation.id.realmdemo.R;
import nightroomcreation.id.realmdemo.app.ApplicationCustom;
import nightroomcreation.id.realmdemo.app.GlobalFunction;
import nightroomcreation.id.realmdemo.database.DBController;
import nightroomcreation.id.realmdemo.model.Book;

/**
 * Created by iand on 25/09/17.
 */

public class BooksAdapter extends RealmRecyclerViewAdapater<Book> {

    final Context context;
    private Realm realm;
    private LayoutInflater layoutInflater;

    public BooksAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        realm = DBController.getInstance().getRealm();

        //get the article
        final Book book = getItem(position);
        // cast the generic view holder to our specific one
        final ViewHolderItem holderItem = (ViewHolderItem) holder;

        //set title and snippet
        holderItem.txtTitle.setText(book.getTitle());
        holderItem.txtAuthor.setText(book.getAuthor());
        holderItem.txtDescription.setText(book.getDescription());

        //load background image
        if (book.getImageUrl() != null) {
            Glide.with(context)
                    .load(book.getImageUrl().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holderItem.imgBackground);
        }

        //remove single match item from db
        holderItem.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                RealmResults<Book> results = realm.where(Book.class).findAll();

                //get book title to show on toast
                Book b = results.get(position);
                String title = b.getTitle();

                //begin the transaction data of DB
                realm.beginTransaction();

                //remove the matching one
                results.remove(position);
                //commiting the transaction
                realm.commitTransaction();

                if (results.size() == 0) {
                    //load preference data
                    GlobalFunction.prefWith(context).setPreloadPref(false);
                } else {
                    GlobalFunction.prefWith(context).setPreloadPref(true);
                }

                notifyDataSetChanged();

                Toast.makeText(context, title + " is removed from database", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        holderItem.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //inflating to new layout ofr editing item
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.item_edit, null);

                final EditText editTitle = (EditText) container.findViewById(R.id.edit_title);
                final EditText editAuthor = (EditText) container.findViewById(R.id.edit_author);
                final EditText editThumbnail = (EditText) container.findViewById(R.id.edit_thumbnail);

                editTitle.setText(book.getTitle());
                editAuthor.setText(book.getAuthor());
                editThumbnail.setText(book.getImageUrl());

                //creating the Alert dialog to show the editing layout
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(container)
                        .setTitle("Edit Book")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //begin the transaction of database
                                //get all the book
                                RealmResults<Book> results = realm.where(Book.class).findAll();
                                realm.beginTransaction();

                                results.get(position).setAuthor(editAuthor.getText().toString());
                                results.get(position).setTitle(editTitle.getText().toString());
                                ;
                                results.get(position).setImageUrl(editThumbnail.getText().toString());

                                //ending the transaction of DB
                                realm.commitTransaction();
                                //notifying data adapter
                                notifyDataSetChanged();
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

    //return the size of your data (invoked by layout manager)
    @Override
    public int getItemCount() {
        if(getRealmAdapter() != null){
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    //sub class
    public static class ViewHolderItem extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txtTitle;
        public TextView txtAuthor;
        public TextView txtDescription;
        public ImageView imgBackground;

        public ViewHolderItem(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_books);
            txtTitle = (TextView) itemView.findViewById(R.id.text_books_title);
            txtAuthor = (TextView) itemView.findViewById(R.id.text_books_author);
            txtDescription = (TextView) itemView.findViewById(R.id.text_books_description);
            imgBackground = (ImageView) itemView.findViewById(R.id.image_background);
        }
    }
}
