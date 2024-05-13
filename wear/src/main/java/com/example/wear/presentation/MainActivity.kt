
package com.example.wear.presentation

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Button
import android.widget.Toast
import com.example.wear.R
import com.example.wear.databinding.ActivityMainBinding
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable

class MainActivity : WearableActivity() {

    private lateinit var sendButton: Button
    private lateinit var messageClient: MessageClient
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageClient = Wearable.getMessageClient(this)
        binding.btn1.setOnClickListener { sendMessage("1") }
        binding.btn2.setOnClickListener { sendMessage("2") }
        binding.btn3.setOnClickListener { sendMessage("3") }
        binding.btn4.setOnClickListener { sendMessage("4") }
        binding.btn5.setOnClickListener { sendMessage("5") }

        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodes ->
            val node = nodes.firstOrNull()
            node?.let {
                Toast.makeText(this, "Устройство подключено", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "Устройство не подключено, инициируем подключение", Toast.LENGTH_SHORT).show()
            }
        }

        setAmbientEnabled()
    }
    private fun sendMessage(message: String) {
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodes ->
            val node = nodes.firstOrNull()
            node?.let {
                messageClient.sendMessage(it.id, "/message_p", message.toByteArray()).addOnSuccessListener {
                    Toast.makeText(this, "Сообщение отправлено"+ message, Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Ошибка отправки сообщения", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Нет подключенных устройств", Toast.LENGTH_SHORT).show()
            }
        }
    }
}