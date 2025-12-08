package com.example.academicachievers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnHomeLogin = view.findViewById<Button>(R.id.btnHomeLogin)
        val btnHomeScholarships = view.findViewById<Button>(R.id.btnHomeScholarships)


        btnHomeLogin.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra("path", "/login")
            startActivity(intent)
        }

        btnHomeScholarships.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra("path", "/scholarships")
            startActivity(intent)
        }
    }
}
