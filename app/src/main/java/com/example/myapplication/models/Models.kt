package com.example.myapplication.models

class Models {
    var id =  0
    var name = ""
    //var body_type = ""
    //var type_engine = ""
    //var type_drive = ""
    var mark_id = 0
    var mark_name = ""

    fun findModelMark() {
        for (mod in Data.dataModel.models.indices) {
            for (mar in Data.dataModel.marks.indices){
                if (Data.dataModel.models[mod].mark_id == Data.dataModel.marks[mar].id) {
                    Data.dataModel.models[mod].mark_name = Data.dataModel.marks[mar].name
                }
            }
        }
    }
}