package com.example.academicachievers

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ScholarshipListActivity : AppCompatActivity() {

    private lateinit var rvScholarships: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var btnRefresh: Button
    private lateinit var adapter: ScholarshipAdapter

    private val allScholarships = mutableListOf<Scholarship>()
    private val shownScholarships = mutableListOf<Scholarship>()

    private val BASE_URL = "http://172.20.10.2:5000"  // change to your IP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scholarship_list_simple)

        rvScholarships = findViewById(R.id.rvScholarships)
        etSearch = findViewById(R.id.etSearch)
        btnRefresh = findViewById(R.id.btnRefresh)

        rvScholarships.layoutManager = LinearLayoutManager(this)

        adapter = ScholarshipAdapter(shownScholarships) { s ->
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("path", "/scholarships/${s.id}")
            startActivity(intent)
        }

        rvScholarships.adapter = adapter

        btnRefresh.setOnClickListener { loadScholarships() }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFilter()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loadScholarships()
    }

    private fun loadScholarships() {
        thread {
            try {
                val url = URL("$BASE_URL/api/scholarships")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                if (conn.responseCode == 200) {
                    val body = conn.inputStream.bufferedReader().readText()
                    val arr = JSONArray(body)

                    val list = mutableListOf<Scholarship>()

                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)

                        val checklistJson = obj.optJSONArray("checklist")
                        val checklist = mutableListOf<String>()
                        if (checklistJson != null) {
                            for (j in 0 until checklistJson.length()) {
                                checklist.add(checklistJson.getString(j))
                            }
                        }

                        list.add(
                            Scholarship(
                                id = obj.getInt("id"),
                                name = obj.getString("name"),
                                country = obj.getString("country"),
                                deadline = if (obj.isNull("deadline")) null else obj.getString("deadline"),
                                link = obj.getString("link"),
                                checklist = checklist
                            )
                        )
                    }

                    runOnUiThread {
                        allScholarships.clear()
                        allScholarships.addAll(list)
                        applyFilter()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error loading scholarships", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun applyFilter() {
        val q = etSearch.text.toString().lowercase()

        val filtered = allScholarships.filter {
            it.name.lowercase().contains(q) || it.country.lowercase().contains(q)
        }

        shownScholarships.clear()
        shownScholarships.addAll(filtered)

        adapter.update(shownScholarships)
    }
}
