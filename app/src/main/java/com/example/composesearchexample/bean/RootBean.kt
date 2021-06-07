package com.example.composesearchexample.bean

/**
 * Created by heyueyang on 2021/6/7
 */
data class RootBean(val provinces: List<ProvinceBean>){
    override fun toString(): String {
        return "RootBean(provinces=$provinces)"
    }
}

data class ProvinceBean(
    val citys: List<CityBean>,
    val provinceName: String
){
    override fun toString(): String {
        return "ProvinceBean(citys=$citys, provinceName='$provinceName')"
    }
}

data class CityBean(
    val citysName: String
){
    override fun toString(): String {
        return "CityBean(cityName='$citysName')"
    }
}
