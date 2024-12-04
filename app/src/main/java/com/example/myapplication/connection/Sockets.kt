package com.example.myapplication.connection

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myapplication.models.Data
import com.example.myapplication.models.ReplyAdd
import com.google.gson.GsonBuilder

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class Sockets {
    val autoServiceUri: URI = URI("ws://54.151.62.0:5000/client")

    fun initWebSocket() {
        Wsc.webSocketClient.connect()
    }
}
    object Wsc {
        var con = false
        val webSocketClient = object : WebSocketClient(Sockets().autoServiceUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("Coinbase", "onOpen")
            }

            override fun onMessage(message: String?) {
                Log.d(ContentValues.TAG, "onMessage: $message")
                Data.answer = message.toString()
                val builder = GsonBuilder()
                val gson = builder.create()
                val reply = gson.fromJson(Data.answer, ReplyAdd::class.java)
                when(reply.id){
                    "updateStatusOrder" -> {
                        /*Data.replies.add(Data.answer)
                        Data.replies.toObservable()
                            .subscribeBy(
                                onNext = {
                                    println(it)
                                    MyOrdersFragment().addData(reply)
                                },
                                onError = { it.printStackTrace() },
                                onComplete = { println("onComplete!") })*/
                        Data.answer = ""
                    }
                    "updateCar" -> {
                        Data.answer = ""
                    }
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(ContentValues.TAG, "onClose")


            }

            override fun onError(ex: Exception?) {
                Log.e(ContentValues.TAG, "onError: ${ex?.message}")
            }
        }
    }