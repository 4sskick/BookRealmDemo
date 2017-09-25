package nightroomcreation.id.realmdemo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmResults;
import nightroomcreation.id.realmdemo.R;
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

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
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
