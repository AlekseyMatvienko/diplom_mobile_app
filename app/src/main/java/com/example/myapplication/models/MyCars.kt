package com.example.myapplication.models

class MyCars{
    var success = false
    var cars = mutableListOf<Cars>()

    fun findCarModel(myCars: MutableList<Cars>){
        for (ca in myCars.indices) {
            for (mo in Data.dataModel.models.indices) {
                if (myCars[ca].model_id == Data.dataModel.models[mo].id){
                    myCars[ca].name_model = Data.dataModel.models[mo].name
                    myCars[ca].carmark_name = Data.dataModel.models[mo].mark_name
                }
            }
        }
    }
}