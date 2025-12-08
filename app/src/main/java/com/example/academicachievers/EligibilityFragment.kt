package com.example.academicachievers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class EligibilityFragment : Fragment() {


    private val BASE_URL = "http://172.20.10.2:5000"

    private lateinit var spCountry: Spinner
    private lateinit var spLevel: Spinner
    private lateinit var spField: Spinner
    private lateinit var etGpa: EditText
    private lateinit var swInternational: Switch
    private lateinit var btnCheck: Button
    private lateinit var lvResults: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_eligibility, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spCountry = view.findViewById(R.id.spCountry)
        spLevel = view.findViewById(R.id.spLevel)
        spField = view.findViewById(R.id.spField)
        etGpa = view.findViewById(R.id.etGpa)
        swInternational = view.findViewById(R.id.swInternational)
        btnCheck = view.findViewById(R.id.btnCheck)
        lvResults = view.findViewById(R.id.lvResults)

        setupSpinners()

        btnCheck.setOnClickListener {
            runEligibilityCheck()
        }
    }

    private fun setupSpinners() {

        val countries = listOf("Any country", "Hungary", "Germany", "UK", "USA", "Canada")
        val levels = listOf("Any level", "Bachelor", "Master", "PhD")
        val fields = listOf("Any field", "Engineering", "Business", "Medicine", "IT", "Humanities")

        fun makeAdapter(items: List<String>): ArrayAdapter<String> {
            return ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                items
            ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        }

        spCountry.adapter = makeAdapter(countries)
        spLevel.adapter = makeAdapter(levels)
        spField.adapter = makeAdapter(fields)
    }

    private fun runEligibilityCheck() {
        val rawCountry = spCountry.selectedItem.toString()
        val rawLevel = spLevel.selectedItem.toString()
        val rawField = spField.selectedItem.toString()
        val gpaText = etGpa.text.toString().trim()
        val isInternational = swInternational.isChecked

        val country = if (rawCountry == "Any country") "" else rawCountry
        val level = if (rawLevel == "Any level") "" else rawLevel
        val field = if (rawField == "Any field") "" else rawField

        val gpa: Float? = if (gpaText.isNotEmpty()) {
            try {
                gpaText.toFloat()
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Invalid GPA format", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            null
        }


        Toast.makeText(requireContext(), "Checking eligibility…", Toast.LENGTH_SHORT).show()

        thread {
            try {
                val url = URL("$BASE_URL/api/eligibility")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val json = JSONObject().apply {
                    put("country", country)
                    put("level", level)
                    put("field", field)
                    if (gpa != null) {
                        put("gpa", gpa)
                    } else {
                        put("gpa", JSONObject.NULL)
                    }
                    put("is_international", isInternational)
                }

                conn.outputStream.use { os ->
                    os.write(json.toString().toByteArray())
                }

                val code = conn.responseCode
                if (code == HttpURLConnection.HTTP_OK) {
                    val body = conn.inputStream.bufferedReader().use { it.readText() }
                    val arr = JSONArray(body)

                    val items = mutableListOf<String>()
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        val name = obj.optString("name", "Scholarship")
                        val countryResp = obj.optString("country", "")
                        val levelResp = obj.optString("level", "")
                        val fieldResp = obj.optString("field", "")
                        val deadline = obj.optString("deadline", "")

                        val line1 = name
                        val line2Parts = mutableListOf<String>()
                        if (countryResp.isNotEmpty()) line2Parts.add(countryResp)
                        if (levelResp.isNotEmpty()) line2Parts.add(levelResp)
                        if (fieldResp.isNotEmpty()) line2Parts.add(fieldResp)
                        val line2 = line2Parts.joinToString(" • ")

                        val line3 = if (deadline.isNotEmpty()) "Deadline: $deadline" else ""

                        val display = listOf(line1, line2, line3)
                            .filter { it.isNotEmpty() }
                            .joinToString("\n")

                        items.add(display)
                    }

                    requireActivity().runOnUiThread {
                        if (items.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "No matching scholarships found.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            items
                        )
                        lvResults.adapter = adapter
                    }
                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Server error: HTTP $code",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Could not check eligibility (check IP / Wi-Fi).",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
