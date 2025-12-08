package com.example.academicachievers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ScholarshipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scholarships, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnOpenScholarships = view.findViewById<Button>(R.id.btnOpenScholarships)
        btnOpenScholarships.setOnClickListener {
            val intent = Intent(requireContext(), ScholarshipListActivity::class.java)
            startActivity(intent)
        }
    }
}
