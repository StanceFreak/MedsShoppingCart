package com.example.testing.data.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testing.data.api.model.response.MedicineList
import com.example.testing.data.api.model.response.MedicineListModel
import com.example.testing.utils.SingleLiveEvent
import retrofit2.HttpException

class PagingSource(
    private val repo: RemoteDataSource,
    private val path: String
): PagingSource<Int, MedicineList>() {
    override fun getRefreshKey(state: PagingState<Int, MedicineList>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MedicineList> {
        return try {
            val currentPage = params.key ?: 1
            val response = repo.getMedicineByCategory(path, currentPage)
            LoadResult.Page (
                response.body()!!.medicineList,
                if (currentPage == 1) null else -1,
                currentPage.plus(1)
            )
        }
        catch (e: Exception) {
            LoadResult.Error(e)
        }
        catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}