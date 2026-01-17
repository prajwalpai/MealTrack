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
public final class GoalDao_Impl implements GoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Goal> __insertionAdapterOfGoal;

  private final EntityDeletionOrUpdateAdapter<Goal> __deletionAdapterOfGoal;

  private final EntityDeletionOrUpdateAdapter<Goal> __updateAdapterOfGoal;

  private final SharedSQLiteStatement __preparedStmtOfDeactivateGoal;

  private final SharedSQLiteStatement __preparedStmtOfUpdateGoalProgress;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public GoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGoal = new EntityInsertionAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `goals` (`id`,`type`,`targetValue`,`currentValue`,`startDate`,`endDate`,`isActive`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Goal entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindLong(3, entity.getTargetValue());
        statement.bindLong(4, entity.getCurrentValue());
        statement.bindString(5, entity.getStartDate());
        if (entity.getEndDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getEndDate());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfGoal = new EntityDeletionOrUpdateAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `goals` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Goal entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGoal = new EntityDeletionOrUpdateAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `goals` SET `id` = ?,`type` = ?,`targetValue` = ?,`currentValue` = ?,`startDate` = ?,`endDate` = ?,`isActive` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Goal entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindLong(3, entity.getTargetValue());
        statement.bindLong(4, entity.getCurrentValue());
        statement.bindString(5, entity.getStartDate());
        if (entity.getEndDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getEndDate());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeactivateGoal = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE goals SET isActive = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateGoalProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE goals SET currentValue = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM goals";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Goal goal, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGoal.insertAndReturnId(goal);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Goal goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGoal.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Goal goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGoal.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deactivateGoal(final long goalId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeactivateGoal.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, goalId);
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
          __preparedStmtOfDeactivateGoal.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateGoalProgress(final long goalId, final int currentValue,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateGoalProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, currentValue);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, goalId);
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
          __preparedStmtOfUpdateGoalProgress.release(_stmt);
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
  public LiveData<List<Goal>> getAllActiveGoals() {
    final String _sql = "SELECT * FROM goals WHERE isActive = 1 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"goals"}, false, new Callable<List<Goal>>() {
      @Override
      @Nullable
      public List<Goal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfCurrentValue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentValue");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Goal> _result = new ArrayList<Goal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Goal _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpTargetValue;
            _tmpTargetValue = _cursor.getInt(_cursorIndexOfTargetValue);
            final int _tmpCurrentValue;
            _tmpCurrentValue = _cursor.getInt(_cursorIndexOfCurrentValue);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Goal(_tmpId,_tmpType,_tmpTargetValue,_tmpCurrentValue,_tmpStartDate,_tmpEndDate,_tmpIsActive,_tmpCreatedAt);
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
  public Object getGoalById(final long goalId, final Continuation<? super Goal> $completion) {
    final String _sql = "SELECT * FROM goals WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, goalId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Goal>() {
      @Override
      @Nullable
      public Goal call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfCurrentValue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentValue");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Goal _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpTargetValue;
            _tmpTargetValue = _cursor.getInt(_cursorIndexOfTargetValue);
            final int _tmpCurrentValue;
            _tmpCurrentValue = _cursor.getInt(_cursorIndexOfCurrentValue);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Goal(_tmpId,_tmpType,_tmpTargetValue,_tmpCurrentValue,_tmpStartDate,_tmpEndDate,_tmpIsActive,_tmpCreatedAt);
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
  public LiveData<List<Goal>> getActiveGoalsByType(final String type) {
    final String _sql = "SELECT * FROM goals WHERE type = ? AND isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return __db.getInvalidationTracker().createLiveData(new String[] {"goals"}, false, new Callable<List<Goal>>() {
      @Override
      @Nullable
      public List<Goal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTargetValue = CursorUtil.getColumnIndexOrThrow(_cursor, "targetValue");
          final int _cursorIndexOfCurrentValue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentValue");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Goal> _result = new ArrayList<Goal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Goal _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpTargetValue;
            _tmpTargetValue = _cursor.getInt(_cursorIndexOfTargetValue);
            final int _tmpCurrentValue;
            _tmpCurrentValue = _cursor.getInt(_cursorIndexOfCurrentValue);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Goal(_tmpId,_tmpType,_tmpTargetValue,_tmpCurrentValue,_tmpStartDate,_tmpEndDate,_tmpIsActive,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
