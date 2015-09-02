package materialtest.vivz.slidenerd.materialtest.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;

import materialtest.vivz.slidenerd.materialtest.logging.L;
import materialtest.vivz.slidenerd.materialtest.pojo.Movie;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class MoviesDatabase {
    private SQLiteDatabase mDatabase;

    public MoviesDatabase(Context context) {
        MoviesHelper mHelper = new MoviesHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertMoviesBoxOffice(ArrayList<Movie> listMovies, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        //create asql prepared statement
        String sql = "INSERT INTO " + MoviesHelper.TABLE_BOX_OFFICE + " VALUES(?,?,?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            Movie currentMovie = listMovies.get(i);
            statement.clearBindings();
            // for a given column index, simply bin the data to be put inside that index
            statement.bindString(2, currentMovie.getTitle());
            statement.bindLong(3, currentMovie.getReleaseDateTheater() == null ? -1 : currentMovie.getReleaseDateTheater().getTime());
            statement.bindLong(4, currentMovie.getAudienceScore());
            statement.bindString(5, currentMovie.getSynopsis());
            statement.bindString(6, currentMovie.getUrlThumbnail());
            statement.bindString(7, currentMovie.getUrlSelf());
            statement.bindString(8, currentMovie.getUrlCast());
            statement.bindString(9, currentMovie.getUrlReviews());
            statement.bindString(10, currentMovie.getUrlSimilar());
            L.m("inserting entry " + i);
            statement.execute();
        }

        // set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

    }

    public ArrayList<Movie> getAllMoviesBoxOffice() {
        ArrayList<Movie> listMovies = new ArrayList<>();
        //get a list of columns to be regtrieved we need all of them
        String[] columns = {MoviesHelper.COLUMN_UID,
                MoviesHelper.COLUMN_TITLE,
                MoviesHelper.COLUMN_RELEASE_DATE,
                MoviesHelper.COLUMN_AUDIENCE_SCORE,
                MoviesHelper.COLUMN_SYNOPSIS,
                MoviesHelper.COLUMN_URL_THUMBNAIL,
                MoviesHelper.COLUMN_URL_SELF,
                MoviesHelper.COLUMN_URL_CAST,
                MoviesHelper.COLUMN_URL_REVIEWS,
                MoviesHelper.COLUMN_URL_SIMILAR
        };
        Cursor cursor = mDatabase.query(MoviesHelper.TABLE_BOX_OFFICE, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                Movie movie = new Movie();
                //each sept is a 2 part process, find the index of the column first, find the data of that column using
                // that index and finally set out blanck movie object to the contain our data
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_TITLE)));
                long releaseDateMilliseconds = cursor.getLong(cursor.getColumnIndex(MoviesHelper.COLUMN_RELEASE_DATE));
                movie.setReleaseDateTheater(releaseDateMilliseconds != -1 ? new Date(releaseDateMilliseconds) : null);
                movie.setAudienceScore(cursor.getInt(cursor.getColumnIndex(MoviesHelper.COLUMN_AUDIENCE_SCORE)));
                movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_SYNOPSIS)));
                movie.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_URL_THUMBNAIL)));
                movie.setUrlSelf(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_URL_SELF)));
                movie.setUrlCast(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_URL_CAST)));
                movie.setUrlReviews(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_URL_REVIEWS)));
                movie.setUrlSimilar(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_URL_SIMILAR)));

                L.m("getting movie object " + movie);
                listMovies.add(movie);
            }
            while (cursor.moveToNext());
        }
        return listMovies;
    }

    public void deleteAll() {
        mDatabase.delete(MoviesHelper.TABLE_BOX_OFFICE, null, null);
    }

    private static class MoviesHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "movies_db";
        public static final int DB_VERSION = 6;
        public static final String TABLE_BOX_OFFICE = "movies_box_office";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_AUDIENCE_SCORE = "audience_score";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_URL_THUMBNAIL = "url_thumbnail";
        public static final String COLUMN_URL_SELF = "url_self";
        public static final String COLUMN_URL_CAST = "url_cast";
        public static final String COLUMN_URL_REVIEWS = "url_reviews";
        public static final String COLUMN_URL_SIMILAR = "url_similar";
        private static final String CREATE_TABLE_BOX_OFFICE = "CREATE TABLE " + TABLE_BOX_OFFICE + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_RELEASE_DATE + " INTEGER," +
                COLUMN_AUDIENCE_SCORE + " INTEGER," +
                COLUMN_SYNOPSIS + " TEXT," +
                COLUMN_URL_THUMBNAIL + " TEXT," +
                COLUMN_URL_SELF + " TEXT," +
                COLUMN_URL_CAST + " TEXT," +
                COLUMN_URL_REVIEWS + " TEXT," +
                COLUMN_URL_SIMILAR + " TEXT" +
                ");";
        private Context mContext;

        public MoviesHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_BOX_OFFICE);
                L.m("Create table box office executed");
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                L.m("upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_BOX_OFFICE + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }
    }
}
