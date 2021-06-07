package com.example.composesearchexample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.example.composesearchexample.bean.RootBean
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.Charset

/**
 * Created by heyueyang on 2021/6/7
 */
class SearchRequest {

    private var searchData: RootBean? = null

    suspend fun search(str: String): Flow<ArrayList<String>> {
        val result = withContext(Dispatchers.IO) {
            if (searchData == null) {
                val jsonStr = read(Utils.appContext?.resources?.assets?.open("search_data"))
                val gson = Gson()
                searchData = gson.fromJson(jsonStr, RootBean::class.java)
            }
            val result = arrayListOf<String>()
            searchData?.let {
                it.provinces.forEach { prov ->
                    if (prov.provinceName.contains(str)) {
                        result.add(prov.provinceName)
                    }
                    prov.citys.forEach { city ->
                        if (city.citysName.contains(str)) {
                            result.add(city.citysName)
                        }
                    }
                }
            }
            result
        }
        return MutableLiveData<ArrayList<String>>(result).asFlow()
    }


    /**
     * 读取文件流
     * @param `is` 文件流
     * @return 文件内容
     */
    private fun read(inputStream: InputStream?): String? {
        val strBuf = StringBuilder()
        try {
            val data = ByteArray(1024 * 8)
            var length: Int
            inputStream?.let {
                while (it.read(data).also { length = it } != -1) {
                    strBuf.append(String(data, 0, length, Charset.forName("UTF-8")))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                return strBuf.toString().replace("\n".toRegex(), "")
            }
        }
        return strBuf.toString().replace("\n".toRegex(), "")
    }


}