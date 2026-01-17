package com.example.mealtracker.goals;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StreakDao_Impl implements StreakDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Streak> __insertionAdapterOfStreak;

  private final EntityDeletionOrUpdateAdapter<Streak> __deletionAdapterOfStreak;

  private final EntityDeletionOrUpdateAdapter<Streak> __updateAdapterOfStreak;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStreakValues;

  private final SharedSQLiteStatement __preparedStmtOfResetCurrentStreak;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public StreakDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStreak = new EntityInsertionAdapter<Streak>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `streaks` (`id`,`type`,`currentStreak`,`longestStreak`,`lastUpdatedDate`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Streak entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindLong(3, entity.getCurrentStreak());
        statement.bindLong(4, entity.getLongestStreak());
        statement.bindString(5, entity.getLastUpdatedDate());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(6, _tmp);
      }
    };
    this.__deletionAdapterOfStreak = new EntityDeletionOrUpdateAdapter<Streak>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `streaks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Streak entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfStreak = new EntityDeletionOrUpdateAdapter<Streak>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `streaks` SET `id` = ?,`type` = ?,`currentStreak` = ?,`longestStreak` = ?,`lastUpdatedDate` = ?,`isActive` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Streak entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindLong(3, entity.getCurrentStreak());
        statement.bindLong(4, entity.getLongestStreak());
        statement.bindString(5, entity.getLastUpdatedDate());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStreakValues = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE streaks SET currentStreak = ?, longestStreak = ?, lastUpdatedDate = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetCurrentStreak = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE streaks SET currentStreak = 0, lastUpdatedDate = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM streaks";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Streak streak, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfStreak.insertAndReturnId(streak);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Streak streak, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfStreak.handle(streak);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Streak streak, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStreak.handle(streak);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStreakValues(final long streakId, final int currentStreak,
      final int longestStreak, final String lastUpdatedDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStreakValues.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, currentStreak);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, longestStreak);
        _argIndex = 3;
        _stmt.bindString(_argIndex, lastUpdatedDate);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, streakId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStreakValues.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetCurrentStreak(final long streakId, final String date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetCurrentStreak.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, date);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, streakId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfResetCurrentStreak.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<Streak>> getAllActiveStreaks() {
    final String _sql = "SELECT * FROM streaks WHERE isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"streaks"}, false, new Callable<List<Streak>>() {
      @Override
      @Nullable
      public List<Streak> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfLastUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdatedDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<Streak> _result = new ArrayList<Streak>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Streak _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final String _tmpLastUpdatedDate;
            _tmpLastUpdatedDate = _cursor.getString(_cursorIndexOfLastUpdatedDate);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new Streak(_tmpId,_tmpType,_tmpCurrentStreak,_tmpLongestStreak,_tmpLastUpdatedDate,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getStreakByType(final String type, final Continuation<? super Streak> $completion) {
    final String _sql = "SELECT * FROM streaks WHERE type = ? AND isActive = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Streak>() {
      @Override
      @Nullable
      public Streak call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfLastUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdatedDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final Streak _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final String _tmpLastUpdatedDate;
            _tmpLastUpdatedDate = _cursor.getString(_cursorIndexOfLastUpdatedDate);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new Streak(_tmpId,_tmpType,_tmpCurrentStreak,_tmpLongestStreak,_tmpLastUpdatedDate,_tmpIsActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<Streak> getStreakByTypeLiveData(final String type) {
    final String _sql = "SELECT * FROM streaks WHERE type = ? AND isActive = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return __db.getInvalidationTracker().createLiveData(new String[] {"streaks"}, false, new Callable<Streak>() {
      @Override
      @Nullable
      public Streak call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfLastUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdatedDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final Streak _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final String _tmpLastUpdatedDate;
            _tmpLastUpdatedDate = _cursor.getString(_cursorIndexOfLastUpdatedDate);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new Streak(_tmpId,_tmpType,_tmpCurrentStreak,_tmpLongestStreak,_tmpLastUpdatedDate,_tmpIsActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
