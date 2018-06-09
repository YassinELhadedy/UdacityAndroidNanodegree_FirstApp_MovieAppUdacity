package com.nextbit.yassin.movieappudacity1.infrastructure.cache.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nextbit.yassin.movieappudacity1.infrastructure.cache.DatabaseHandler;

import java.util.Arrays;
import java.util.HashSet;

import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_ID;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_MOV_IMAGE2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_MOV_OV2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_MOV_RAT2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_NAME2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_R_D2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_TRA_ID2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.KEY_V_C2;
import static com.nextbit.yassin.movieappudacity1.domain.confiq.Constants.TABLE_NAME2;



public class MyFavMovContentProvider extends ContentProvider {
    // database
    private DatabaseHandler database;

    // used for the UriMacher
    private static final int TODOS = 10;
    private static final int TODO_ID = 20;

    private static final String AUTHORITY = "com.nextbit.yassin.movieappudacity1.infrastructure.cache.provider";

    private static final String BASE_PATH = "todos";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
    }



    @Override
    public boolean onCreate() {
        database = new DatabaseHandler(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(TABLE_NAME2);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TODOS:
                break;
            case TODO_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(KEY_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id;
        switch (uriType) {
            case TODOS:
                id = sqlDB.insert(TABLE_NAME2, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
                String id = uri.getLastPathSegment();
        rowsDeleted = sqlDB.delete(
                            TABLE_NAME2, KEY_NAME2 + " = ? ",
                new String[]{ String.valueOf(id)});
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case TODOS:
                rowsUpdated = sqlDB.update(TABLE_NAME2,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TODO_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TABLE_NAME2,
                            values,
                            KEY_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TABLE_NAME2,
                            values,
                            KEY_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;    }

    private void checkColumns(String[] projection) {
        String[] available = {KEY_NAME2,
                KEY_MOV_IMAGE2, KEY_MOV_OV2,KEY_MOV_RAT2,KEY_V_C2,KEY_TRA_ID2,KEY_R_D2,
                KEY_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }

}
