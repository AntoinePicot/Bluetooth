package fr.esme.esme_map

import android.app.Activity
import android.app.Service
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList


class LeDeviceListAdapter(
    private val context: Context
) : BaseAdapter() {
    private val mLeDevices: ArrayList<BluetoothDevice>

    var bluetoothGatt: BluetoothGatt? = null

    init {
        mLeDevices = ArrayList<BluetoothDevice>()
    }

    fun addDevice(device: BluetoothDevice) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device)
        }
    }

    fun getDevice(position: Int): BluetoothDevice? {
        return mLeDevices[position]
    }

    fun clear() {
        mLeDevices.clear()
    }

    override fun getCount(): Int {
        return mLeDevices.size
    }

    override fun getItem(i: Int): Any {
        return mLeDevices[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView: View? = convertView
        // General ListView optimization code.
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item_view, parent,false)
        }

        var deviceName: TextView = convertView!!.findViewById(R.id.userName) as TextView
        var deviceName1: TextView = convertView!!.findViewById(R.id.userName1) as TextView

        val device = mLeDevices[i]
        if (deviceName != null && deviceName.text.length > 0) {
            deviceName.text = device.address
            deviceName1.text = device.name
        }
        else
            deviceName.text = "error"

        deviceName.setOnClickListener{
            //(context as MainActivity).
            bluetoothGatt = mLeDevices[i].connectGatt(context, false, gattCallback)
            Toast.makeText(context, mLeDevices[i].address, Toast.LENGTH_SHORT).show()
        }
        return convertView
    }
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val deviceAddress = gatt.device.address

            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.w("BluetoothGattCallback", "Successfully connected to $deviceAddress")

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.w("BluetoothGattCallback", "Successfully disconnected from $deviceAddress")
                    gatt.close()
                }
            } else {
                Log.w(
                    "BluetoothGattCallback",
                    "Error $status encountered for $deviceAddress! Disconnecting..."
                )
                gatt.close()

            }
        }
    }
}

