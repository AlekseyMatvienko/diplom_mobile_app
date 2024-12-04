package com.example.myapplication.models

class DataModel {
    var success = false
    var marks = mutableListOf<Marks>()
    var models = mutableListOf<Models>()
    var statuses = mutableListOf<Statuses>()
    var services = mutableListOf<Services>()
    var serviceTypes = mutableListOf<ServiceTypes>()
}
object Data{
    var selected_services = mutableListOf<Services>()
    var dataModel: DataModel = DataModel()
    var answer = ""
    var myCar = mutableListOf<Cars>()
    var myOrders = mutableListOf<Orders>()
    var replies = mutableListOf<String>()
    var selected_order = Orders()
    var selected_car = Cars()
}



