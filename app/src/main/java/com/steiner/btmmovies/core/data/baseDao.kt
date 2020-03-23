package com.steiner.btmmovies.core.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

/**
 *
 */
abstract class BaseDao<T : Any> : BaseSuspendDao<T> {

    @Transaction
    open suspend fun upsert(obj: T) {
        val rowID = insertOrIgnoreSuspend(obj)
        if (rowID == -1L) {
            updateOrReplaceSuspend(obj)
        }
    }

    @Transaction
    open suspend fun upsert(objList: List<T>) {
        val rowIDs = insertOrIgnoreSuspend(objList)
        val objListToUpdate = rowIDs.mapIndexedNotNull { index, rowID ->
            if (rowID == -1L) null else objList[index]
        }
        objListToUpdate.forEach { obj ->
            updateOrReplaceSuspend(obj)
        }
    }
}

/**
 *
 */
interface BaseSuspendDao<T : Any> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSuspend(obj: T): Long

    /**
     * Insert a list of objects in the database.
     *
     * @param objList the list of objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSuspend(objList: List<T>): List<Long>

    /**
     * Insert an array of objects in the database.
     *
     * @param objArray the array of objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSuspend(vararg objArray: T): LongArray

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreSuspend(obj: T): Long

    /**
     * Insert a list of objects in the database.
     *
     * @param objList the list of objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreSuspend(objList: List<T>): List<Long>

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrAbortSuspend(obj: T): Long

    /**
     * Insert a list of objects in the database.
     *
     * @param objList the list of objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrAbortSuspend(objList: List<T>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrReplaceSuspend(obj: T): Int

    /**
     * Update a list of objects in the database.
     *
     * @param objList the list of objects to be updated
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrReplaceSuspend(objList: List<T>): Int

    /**
     * Update an array of objects in the database.
     *
     * @param objArray the array of objects to be updated
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrReplaceSuspend(vararg objArray: T): Int

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateOrIgnoreSuspend(obj: T): Int

    /**
     * Update a list of objects in the database.
     *
     * @param objList the list of objects to be updated
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateOrIgnoreSuspend(objList: List<T>): Int

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateOrAbortSuspend(obj: T): Int

    /**
     * Update a list of objects in the database.
     *
     * @param objList the list of objects to be updated
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateOrAbortSuspend(objList: List<T>): Int

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun deleteSuspend(obj: T): Int

    /**
     * Delete an array of objects in the database.
     *
     * @param objArray the array of objects to be deleted
     */
    @Delete
    suspend fun deleteSuspend(vararg objArray: T): Int

    /**
     * Delete a list of objects in the database.
     *
     * @param objList the list of objects to be deleted
     */
    @Delete
    suspend fun deleteSuspend(objList: List<T>): Int
}
