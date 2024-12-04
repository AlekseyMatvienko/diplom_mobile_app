package com.example.myapplication.models

class Orders{
    var id = 0
    var payment_type = 0
    var start_date = ""
    var finish_date = ""
    var is_paid = 0
    var status_id = 0
    var status = ""
    var employee_id = 0
    var person_id = 0
    var car_id = 0
    var car = Cars()
    var services = mutableListOf<Int>()
    var servicesList = mutableListOf<Services>()

    fun findDate(){
        for (ord in Data.myOrders.indices){
            for (st in Data.dataModel.statuses.indices){
                if (Data.myOrders[ord].status_id == Data.dataModel.statuses[st].id)
                    Data.myOrders[ord].status = Data.dataModel.statuses[st].name}
            for (ca in Data.myCar.indices){
                if (Data.myOrders[ord].car_id == Data.myCar[ca].id)
                    Data.myOrders[ord].car = Data.myCar[ca]}
            for (isv in Data.myOrders[ord].services.indices){
                for (sv in Data.dataModel.services.indices)
                    if (Data.myOrders[ord].services[isv] == Data.dataModel.services[sv].id)
                        Data.myOrders[ord].servicesList.add(Data.dataModel.services[sv])}
        }
    }
}