package com.fanny.jsonparser

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun httpHandler(urlString: String): List<Kontak> {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "GET"

    val responseCode = connection.responseCode
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val inputStream = connection.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()

        val json = JSONObject(response.toString())
        val jsonArray = JSONArray(json.getString("contacts"))
        val data = mutableListOf<Kontak>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val phoneObject = JSONObject(jsonObject.getString("phone"))

            val name = jsonObject.getString("name")
            val email = jsonObject.getString("email")
            val phone = phoneObject.getString("mobile")

            data.add(Kontak(name, phone, email))
        }

        return data
    } else {
        return emptyList()
    }
}