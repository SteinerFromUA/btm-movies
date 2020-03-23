package com.steiner.btmmovies.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.steiner.btmmovies.core.data.BaseDao
import com.steiner.btmmovies.model.entity.LastRequest
import com.steiner.btmmovies.model.enumeration.Request

/**
 *
 */
@Dao
abstract class LastRequestDao : BaseDao<LastRequest>() {

    @Query("SELECT * FROM last_requests WHERE request = :request")
    abstract suspend fun takeByRequest(request: Request): LastRequest?
}
