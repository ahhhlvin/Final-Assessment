package nyc.c4q;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.models.Book;
import nyc.c4q.models.Member;

public class LibraryActivity extends Activity {

    public EditText inputParameter;
    List <Member> allMembers;
    List <Book> allBooks;
    MySQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        inputParameter = (EditText) findViewById(R.id.input_parameter);

        helper = MySQLiteOpenHelper.getInstance(getApplicationContext());

        try {
            allMembers =helper.loadAllMember();
            allBooks = helper.loadAllBook();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (allBooks.size() == 0){

            ArrayList<Book> bookList = new ArrayList<>();
            bookList = JsonHelper.loadBookJsonRawUsingGson(this, R.raw.books);
            for (int i = 0; i < bookList.size(); i++){

                Book book = bookList.get(i);
                int id = book.getId();
                String title = book.getTitle();
                String author =  book.getAuthor();
                String isbn = book.getIsbn();
                String isbn13 =  book.getIsbn13();
                String publisher = book.getPublisher();
                int publishyear = book.getPublishyear();
                Boolean checkedout = book.getCheckedout();
                if (checkedout != null) {
                    int checkedoutby = book.getCheckedoutby();
                    int checkoutdateyear = book.getCheckoutdateyear();
                    int checkoutdatemonth = book.getCheckoutdatemonth();
                    int checkoutdateday = book.getCheckoutdateday();
                    int duedateyear = book.getDuedateyear();
                    int duedatemonth = book.getDuedatemonth();
                    int duedateday = book.getCheckoutdateday();

                    helper.insertBookCheckedOutData(id, title, author, isbn, isbn13, publisher, publishyear, checkedout,
                            checkedoutby, checkoutdateyear, checkoutdatemonth, checkoutdateday, duedateyear, duedatemonth, duedateday);
                } else{
                    helper.insertBookData(id, title, author, isbn, isbn13, publisher, publishyear);
                }
            }
        }

        try {
            allMembers =helper.loadAllMember();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (allMembers.size() == 0){

            ArrayList<Member> memberList = new ArrayList<>();
            memberList = JsonHelper.loadMemberJsonRawUsingGson(this, R.raw.members);
            for (int i = 0; i < memberList.size(); i++){

                Member member = memberList.get(i);
                String name = member.getName();
                int memberId = member.getId();
                String city = member.getCity();
                String state = member.getState();
                int dobDay = member.getDobDay();
                int dobMonth = member.getDobMonth();
                int dobYear = member.getDobYear();


                helper.insertMemberData(memberId, name, dobMonth, dobDay, dobYear, city, state);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        LibraryDataSource dataSource = new LibraryDataSource(this);
    }

    public void checkOut(int memberId, int bookId) {
        // TODO This method is called when the member with the given ID checks
        //      out the book with the given ID. Update the system accordingly.
        //      The due date for the book is two weeks from today.
    }

    public boolean checkIn(int memberId, int bookId) {
        // TODO This method is called when the member with the given ID returns
        //      the book with the given ID. Update the system accordingly. If
        //      the member is returning the book on time, return true. If it's
        //      late, return false.

        return false;
    }

    public void button_getMember_onClick(View view) throws SQLException {
        String name = inputParameter.getText().toString();

        // TODO Display member information for the member with the given name.

        new MemberDataSeachTask().execute(name);

    }

    public void button_getBook_onClick(View view) {
        String isbn = inputParameter.getText().toString();

        // TODO Display book information for the book with the given ISBN.
        new BooksDataSeachTask().execute(isbn);
    }

    public void button_getCheckedOut_onClick(View view) {
        String name = inputParameter.getText().toString();

        // TODO Display a list of books that the member with the given name
        //      currently has checked out, ordered by due date, with the
        //      earliest due first.
    }



    private class MemberDataSeachTask extends AsyncTask<String, Void, Member> {
        String name;
        Member member;
        @Override
        protected Member doInBackground(String... names) {


            try {
                member = helper.loadMemberData(names[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return member;
        }

        @Override
        protected void onPostExecute(Member member) {
            String memberName = member.getName();
            int memberId = member.getId();
            String city = member.getCity();
            String state = member.getState();
            int dobDay = member.getDobDay();
            int dobMonth = member.getDobMonth();
            int dobYear = member.getDobYear();

            String result = "id: " + String.valueOf(memberId) + "\n" +
                    "name: " + memberName + "\n" +
                    "dob: " + String.valueOf(dobMonth) + "/" + String.valueOf(dobDay)+ "/" + String.valueOf(dobYear)+"\n" +
                    "location: " + city + ", " + state;
            TextView textView = (TextView)findViewById(R.id.text_display);
            textView.setText(result);
        }
    }




    private class BooksDataSeachTask extends AsyncTask<String, Void, Book> {
        String isbn;
        Book book;
        @Override
        protected Book doInBackground(String... isbns) {


            try {
                book = helper.loadBookData(isbns[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return book;
        }

        @Override
        protected void onPostExecute(Book book) {
            int id = book.getId();
            String title = book.getTitle();
            String author =  book.getAuthor();
            String isbn = book.getIsbn();
            String isbn13 =  book.getIsbn13();
            String publisher = book.getPublisher();
            int publishyear = book.getPublishyear();

                String result = "id: " + String.valueOf(id) + "\n" +
                        "title: " + title + "\n" +
                        "author: " + author + "\n" +
                        "isbn: " + isbn + "\n" +
                        "isbn13: " + isbn13 + "\n" +
                        "publisher: " + publisher + "\n" +
                        "publication year: " + String.valueOf(publishyear);
                TextView textView = (TextView) findViewById(R.id.text_display);
                textView.setText(result);

        }
    }
}
